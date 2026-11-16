-- TFG: Sistema inteligente de detección temprana de incendios
-- Esquema PostgreSQL
--
-- Enumerados: los estados de detección y los de las alertas (status, category, alert_type,
-- severity) NO se definen aquí. La base de datos solo guarda el texto y los valores válidos
-- los define el enum del backend (en Spring Boot: @Enumerated(EnumType.STRING)).
-- Los valores que aparecen en los comentarios son solo orientativos.
--
-- UUID: el de detection lo genera el backend (UUID.randomUUID()), porque se necesita antes
-- de insertar para guardar la imagen en Redis.
--
-- Redis: la imagen se guarda en Redis durante 24 h y detection.redis_key guarda la clave
-- exacta (p. ej. img:{device_id}:{id}). Solo las imágenes de alertas de incendio se
-- conservan, de forma permanente, en alert_image.
--

-- ---------------------------------------------------------------
-- Dispositivos
-- ---------------------------------------------------------------

CREATE TABLE camera_model (
    id          smallint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        varchar(60) NOT NULL UNIQUE,      -- p. ej. 'OV2640'
    transmittedImageFormat varchar(20) not null
);

CREATE TABLE device (
    id                  bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name                varchar(80) NOT NULL,                -- p. ej. 'cam-01'
    mac_address         macaddr NOT NULL UNIQUE,
    board               varchar(60),                         -- p. ej. 'AI Thinker ESP32-CAM'
    camera_model_id     smallint NOT NULL REFERENCES camera_model(id),
    capture_interval_s  int NOT NULL DEFAULT 60 CHECK (capture_interval_s > 0), --p ej 180 s
    latitude            double precision CHECK (latitude  BETWEEN  -90 AND  90),
    longitude           double precision CHECK (longitude BETWEEN -180 AND 180),
    active              boolean NOT NULL DEFAULT false,
    created_at          timestamptz NOT NULL DEFAULT now(),
    last_seen_at        timestamptz                          -- device_seen() lo actualiza con cada imagen válida
);

-- Historial de activación: una fila cada vez que active cambia de valor.
-- Lo escriben device_seen() y deactivate_stale_devices(), en la misma sentencia que cambia device.active.
CREATE TABLE device_activation_history (
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    device_id   bigint NOT NULL REFERENCES device(id) ON DELETE CASCADE,
    active      boolean NOT NULL,                            -- estado al que pasó
    changed_at  timestamptz NOT NULL DEFAULT clock_timestamp()
);

-- Lecturas de los sensores ambientales (opcionales). Los CHECK rechazan valores
-- imposibles, que casi siempre indican un sensor averiado o mal leído.
CREATE TABLE sensor_reading (
    id              bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    device_id       bigint NOT NULL REFERENCES device(id) ON DELETE CASCADE,
    measured_at     timestamptz NOT NULL DEFAULT now(),
    temperature_c   numeric(5,2),                                          -- temperatura en °C (de -999,99 a 999,99)
    humidity_pct    numeric(5,2) CHECK (humidity_pct BETWEEN 0 AND 100),   -- humedad relativa en %
    co2_ppm         int CHECK (co2_ppm >= 0),                              -- CO2 en partes por millón (ppm)
    pm25            numeric(6,2) CHECK (pm25 >= 0),                        -- partículas de hasta 2,5 micras, en µg/m³
    pm10            numeric(6,2) CHECK (pm10 >= 0)                         -- partículas de hasta 10 micras, en µg/m³
);

-- ---------------------------------------------------------------
-- Detecciones: una fila por imagen recibida, con su estado actual y su predicción
--
-- id: lo genera el backend, sin valor por defecto.
-- status: lo escribe el backend con su enum (p. ej. PENDING, PROCESSING, DONE, ERROR, DISCARDED).
--         Aquí solo está el estado ACTUAL; el recorrido completo está en detection_status_history.
-- redis_key: clave exacta de la imagen en Redis (caduca a las 24 h; la clave se conserva).
-- received_at: cuándo llegó. Sirve para ordenar la cola sin hacer un join.
-- ---------------------------------------------------------------

CREATE TABLE detection (
    id                uuid PRIMARY KEY,                                -- generado por el backend
    device_id         bigint NOT NULL REFERENCES device(id),
    status            varchar(20) NOT NULL,                            -- valores definidos por el enum del backend
    redis_key         varchar(100) NOT NULL,                           -- p. ej. img:{device_id}:{id}
    received_at       timestamptz NOT NULL DEFAULT clock_timestamp(),
    probability       numeric(4,3) CHECK (probability BETWEEN 0 AND 1),
    indicators        text[],                                          -- p. ej. {humo,llamas}
    reason            text,
    raw_response      jsonb,                                           -- respuesta completa del modelo
    inference_ms      int CHECK (inference_ms >= 0),                   -- solo la llamada a Ollama
    width             int,
    height            int,
    has_alert_image     boolean DEFAULT null,                     -- si se guardó en alert_image
    image_expires_at  timestamptz,                                     -- cuándo caduca en Redis
    UNIQUE (id, device_id)                                             -- permite el FK compuesto del historial
);

-- Historial de estados: una fila por cada cambio de detection.status
--   from_status: NULL en la primera fila (la imagen acaba de llegar)
--   to_status:   estado al que pasó
--   changed_at:  cuándo (hora de la base de datos, con clock_timestamp())
-- Clave primaria: (detection_id, device_id, changed_at). Una detección tiene varias filas, una
-- por cada cambio, así que detection_id y device_id solos no bastan para identificar una fila:
-- changed_at es lo que las distingue.
-- device_id está repetido a propósito; el FK compuesto garantiza que coincida con el de la detección.
-- Al borrar una detección vieja se borra también su historial.
CREATE TABLE detection_status_history (
    detection_id  uuid NOT NULL,
    device_id     bigint NOT NULL,
    from_status   varchar(20),
    to_status     varchar(20) NOT NULL,
    changed_at    timestamptz NOT NULL DEFAULT clock_timestamp(),
    PRIMARY KEY (detection_id, device_id, changed_at),
    FOREIGN KEY (detection_id, device_id) REFERENCES detection (id, device_id) ON DELETE CASCADE
);

-- ---------------------------------------------------------------
-- Alertas: dos categorías en la misma tabla (los valores los define el enum del backend)
--   FIRE   alertas de incendio: llevan detection_id (y su imagen en alert_image)
--   SYSTEM alertas operativas: cámara sin señal (la desactiva el worker), cola saturada...
--          no llevan detection_id. La alerta NO tiene device_id: el dispositivo de una alerta FIRE se obtiene
--          por detection.device_id (así no hay dos caminos hasta device)
-- Todas se guardan aquí; el canal (SSE hacia el panel, Telegram...) es del backend y no se guarda.
-- ---------------------------------------------------------------

CREATE TABLE alert (
    id              bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category        varchar(20) NOT NULL,                             -- p. ej. FIRE, SYSTEM
    alert_type      varchar(30) NOT NULL,                             -- p. ej. FIRE, SMOKE, SUSPECT, DEVICE_OFFLINE, QUEUE_BACKLOG
    severity        varchar(20) NOT NULL,                             -- p. ej. LOW, MEDIUM, HIGH
    title           varchar(120) NOT NULL,
    description     text,
    detection_id    uuid UNIQUE REFERENCES detection(id),             -- solo FIRE; como máximo una alerta por detección
    risk_score      numeric(4,3) CHECK (risk_score BETWEEN 0 AND 1),  -- tras fusionar con sensores (solo FIRE)
    status          varchar(20) NOT NULL,                             -- p. ej. OPEN, ACKNOWLEDGED, RESOLVED, FALSE_POSITIVE
    created_at      timestamptz NOT NULL DEFAULT now(),
    resolved_at     timestamptz,
    notified        boolean NOT NULL DEFAULT false                    -- ya avisada por un canal externo (opcional)
);

-- Imagen que se conserva de forma permanente: la de las detecciones que generaron una alerta
-- de incendio. Se identifica con el mismo UUID de la detección (el de la clave de Redis).
CREATE TABLE alert_image (
    detection_id  uuid PRIMARY KEY REFERENCES detection(id),
    content       bytea NOT NULL,
    content_type  varchar(30) NOT NULL DEFAULT 'image/jpeg',
    size_bytes    int NOT NULL,
    saved_at      timestamptz NOT NULL DEFAULT now()
);

-- ===============================================================
-- Funciones que llama el web service (no hay triggers)
-- Los valores de estado (p. ej. 'PENDING') los pasa siempre el backend desde su enum.
-- ===============================================================

-- Crea la detección y anota su primera fila del historial (NULL -> estado inicial).
-- Devuelve la hora de llegada (received_at).
CREATE FUNCTION create_detection(p_id uuid, p_device_id bigint, p_status varchar, p_redis_key varchar)
RETURNS timestamptz AS $$
DECLARE
    v_received timestamptz;
BEGIN
    INSERT INTO detection (id, device_id, status, redis_key)
    VALUES (p_id, p_device_id, p_status, p_redis_key)
    RETURNING received_at INTO v_received;

    INSERT INTO detection_status_history (detection_id, device_id, from_status, to_status)
    VALUES (p_id, p_device_id, NULL, p_status);

    RETURN v_received;
END;
$$ LANGUAGE plpgsql;

-- Cambia el estado de UNA detección y anota el cambio.
-- Devuelve true si cambió; false si no existe o ya estaba en ese estado (no anota nada).
CREATE FUNCTION change_detection_status(p_id uuid, p_to_status varchar)
RETURNS boolean AS $$
DECLARE
    v_device bigint;
    v_from   varchar;
BEGIN
    SELECT d.device_id, d.status INTO v_device, v_from
    FROM detection d WHERE d.id = p_id FOR UPDATE;

    IF NOT FOUND OR v_from = p_to_status THEN
        RETURN false;
    END IF;

    UPDATE detection SET status = p_to_status WHERE id = p_id;

    INSERT INTO detection_status_history (detection_id, device_id, from_status, to_status)
    VALUES (p_id, v_device, v_from, p_to_status);

    RETURN true;
END;
$$ LANGUAGE plpgsql;

-- La ingesta la llama con cada imagen válida: anota la señal de vida y reactiva la cámara
-- si estaba inactiva (anotándolo en el historial).
-- Devuelve true si la cámara estaba caída y se ha reactivado (entonces hay que cerrar su alerta);
-- false si ya estaba activa o si la cámara no existe.
CREATE FUNCTION device_seen(p_device_id bigint)
RETURNS boolean AS $$
DECLARE
    v_was_inactive boolean;
BEGIN
    SELECT NOT d.active INTO v_was_inactive
    FROM device d WHERE d.id = p_device_id FOR UPDATE;

    IF NOT FOUND THEN
        RETURN false;
    END IF;

    UPDATE device SET last_seen_at = clock_timestamp(), active = true WHERE id = p_device_id;

    IF v_was_inactive THEN
        INSERT INTO device_activation_history (device_id, active) VALUES (p_device_id, true);
    END IF;

    RETURN v_was_inactive;
END;
$$ LANGUAGE plpgsql;

-- El worker de dispositivos la llama cada 5 minutos: desactiva las cámaras activas que dejaron de
-- enviar imágenes (margen de 2 intervalos) y lo anota en el historial. Devuelve el id de cada cámara
-- que acaba de caer (el backend crea una alerta por cada una). Una cámara se desactiva una sola vez
-- por caída. Las que nunca enviaron nada (last_seen_at NULL) no se tocan: ya están inactivas.
CREATE FUNCTION deactivate_stale_devices()
RETURNS TABLE (device_id bigint) AS $$
#variable_conflict use_column
BEGIN
    RETURN QUERY
    WITH caidas AS (
        UPDATE device d SET active = false
        WHERE d.active = true
          AND d.last_seen_at < now() - interval '1 second' * (d.capture_interval_s * 2)
        RETURNING d.id
    ), anotadas AS (
        INSERT INTO device_activation_history (device_id, active)
        SELECT c.id, false FROM caidas c
        RETURNING device_activation_history.device_id
    )
    SELECT a.device_id FROM anotadas a;
END;
$$ LANGUAGE plpgsql;

   -- Registra una cámara y su dispositivo.
--   * camera_model: si ya existe uno con el mismo name y transmittedImageFormat se usa ese;
--     si no, se crea.
--   * device: si ya existe uno con esa mac_address se actualizan sus datos (name, board, modelo de
--     cámara, intervalo, latitud y longitud); si no, se crea. No toca active, created_at ni last_seen_at.
-- El intervalo llega en milisegundos y se guarda en segundos (capture_interval_s), redondeado hacia
-- arriba y con mínimo 1.
-- Devuelve en los parámetros INOUT el id del dispositivo y el del modelo de cámara usados.
-- Uso: CALL register_device('AI Thinker ESP32-CAM', 'OV2640', 'image/jpeg', 'aa:bb:cc:dd:ee:01',
--                           180000, 'cam-01', 41.38, 2.17, NULL, NULL);
CREATE OR REPLACE PROCEDURE register_device(
    p_board        varchar,
    p_camera_name  varchar,
    p_image_format varchar,
    p_mac_address  varchar,
    p_interval_s  bigint,
    p_device_name  varchar,
    p_latitude     double precision,
    p_longitude    double precision,
    INOUT p_device_id        bigint,
    INOUT p_camera_model_id  smallint
) AS $$
BEGIN
    -- Cámara: reutilizar la que tenga el mismo name y formato, o crearla
    SELECT c.id INTO p_camera_model_id
    FROM camera_model c
    WHERE c.name = p_camera_name AND c.transmittedImageFormat = p_image_format;

    IF NOT FOUND THEN
            INSERT INTO camera_model (name, transmittedImageFormat)
            VALUES (p_camera_name, p_image_format)
            ON CONFLICT DO NOTHING
            RETURNING id INTO p_camera_model_id;
    END IF;

    -- Dispositivo: crear o actualizar por mac_address
    INSERT INTO device (name, mac_address, board, camera_model_id, capture_interval_s, latitude, longitude)
    VALUES (p_device_name, p_mac_address::macaddr, p_board, p_camera_model_id, p_interval_s, p_latitude, p_longitude)
        ON CONFLICT (mac_address) DO UPDATE
        SET name               = EXCLUDED.name,
        board              = EXCLUDED.board,
        camera_model_id    = EXCLUDED.camera_model_id,
        capture_interval_s = EXCLUDED.capture_interval_s,
        latitude           = EXCLUDED.latitude,
        longitude          = EXCLUDED.longitude
        RETURNING id INTO p_device_id;
END;
$$ LANGUAGE plpgsql;
