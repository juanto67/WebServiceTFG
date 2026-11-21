package com.tfg.EPS0375.WebServiceTFG.repository.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;
    @Column(name = "probability")
    private BigDecimal probability;
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "indicators")
    private String[] indicators;
    @Column(name = "reason")
    private String reason;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "raw_response")
    private JsonNode rawResponse;
    @Column(name = "inference_ms")
    private Integer inferenceMs;
    @Column(name = "has_alert")
    private Boolean hasAlert;
}
