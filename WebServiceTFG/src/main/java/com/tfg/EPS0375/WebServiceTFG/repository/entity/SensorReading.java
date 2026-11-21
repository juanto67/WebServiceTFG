package com.tfg.EPS0375.WebServiceTFG.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensor_reading")
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "measured_at", nullable = false, updatable = false)
    private LocalDateTime measuredAt;

    @Column(name = "temperature_c", precision = 5, scale = 2)
    private BigDecimal temperatureC;

    @Column(name = "humidity_pct", precision = 5, scale = 2)
    private BigDecimal humidityPct;

    @Column(name = "co2_ppm")
    private Integer co2Ppm;

    @Column(name = "pm25", precision = 6, scale = 2)
    private BigDecimal pm25;

    @Column(name = "pm10", precision = 6, scale = 2)
    private BigDecimal pm10;
}
