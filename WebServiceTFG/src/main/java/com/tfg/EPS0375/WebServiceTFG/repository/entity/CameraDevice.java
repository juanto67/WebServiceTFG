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
@Table(name = "camera_device")
public class CameraDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "camera_model", nullable = false, length = 50)
    private String cameraModel;

    @Column(name = "capture_interval_ms", nullable = false)
    private Integer captureIntervalMs;

    @Column(name = "transmitted_image_format", nullable = false, length = 10)
    private String transmittedImageFormat;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime lastUpdatedAt;

    @Column(name = "mac_address", nullable = false, length = 17)
    private String macAddress;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "active", nullable = false)
    private boolean active;

    public CameraDevice(String cameraModel, Integer captureIntervalMs, String transmittedImageFormat, String macAddress, Double latitude, Double longitude) {
        this.cameraModel = cameraModel;
        this.captureIntervalMs = captureIntervalMs;
        this.transmittedImageFormat = transmittedImageFormat;
        this.macAddress = macAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = LocalDateTime.now(ZoneId.of("UTC"));
        this.lastUpdatedAt = LocalDateTime.now(ZoneId.of("UTC"));
        this.active = true;

    }
}

