package com.tfg.EPS0375.WebServiceTFG.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 80)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "camera_model_id", nullable = false)
    private CameraModel cameraModel;

    @Column(name = "capture_interval_s", nullable = false)
    private Integer captureIntervalS;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "mac_address", nullable = false, length = 17)
    private String macAddress;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "board", length = 60)
    private String board;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    public Device(String cameraModel, Integer captureIntervalS, String transmittedImageFormat,
                  String macAddress, Double latitude, Double longitude) {
        this.name = "cam-" + macAddress;
        this.cameraModel = new CameraModel(cameraModel, transmittedImageFormat);
        this.captureIntervalS = captureIntervalS;
        this.macAddress = macAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        this.active = true;
    }
}
