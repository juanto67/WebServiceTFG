package com.tfg.EPS0375.WebServiceTFG.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "image")
public class Image {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    @ManyToOne
    @JoinColumn(name = "detection_id", nullable = false)
    private Detection detection;
    @Column(name = "content", nullable = false)
    private String content;
    @Column(name = "content_type", nullable = false)
    private String contentType;
    @Column(name = "saved_at", nullable = false)
    private LocalDateTime savedAt;
    @Column(name = "redis_key", nullable = false)
    private String redisKey;
    @Column(name = "image_expires_at")
    private LocalDateTime imageExpiresAt;
    @Column(name = "width")
    private Integer width;
    @Column(name = "height")
    private Integer height;
}
