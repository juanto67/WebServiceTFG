package com.tfg.EPS0375.WebServiceTFG.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alert_image", uniqueConstraints = {
        @UniqueConstraint(name = "alert_image_device_detection_key",
                columnNames = {"device_id", "detection_id"})
})
public class AlertImage {
    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detection_id", nullable = false, unique = true)
    private Detection detection;
    @Column(nullable = false, length = 20)
    private String content;
    @Column(name = "content_type", nullable = false, length = 30)
    private String contentType;
    @Column(name = "saved_at", nullable = false, updatable = false)
    private LocalDateTime savedAt;
    @Column(name = "redis_key", nullable = false, length = 100)
    private String redisKey;
    @Column(name = "image_expires_at")
    private LocalDateTime imageExpiresAt;
}
