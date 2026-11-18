package com.tfg.EPS0375.WebServiceTFG.repository.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detection")
public class Detection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(nullable = false, length = 20)
    private String status;
    @Column(name = "received_at", nullable = false, updatable = false)
    private LocalDateTime receivedAt;
    @Column(precision = 4, scale = 3)
    private BigDecimal probability;
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "indicators", columnDefinition = "text[]")
    private String[] indicators;
    @Column(name = "reason", columnDefinition = "text")
    private String reason;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_response", columnDefinition = "jsonb")
    private JsonNode rawResponse;
    @Column(name = "inference_ms")
    private Integer inferenceMs;
    private Integer width;
    private Integer height;
    @Column(name = "has_alert_image")
    private Boolean hasAlertImage;
}
