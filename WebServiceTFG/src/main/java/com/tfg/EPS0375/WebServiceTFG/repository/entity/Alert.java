package com.tfg.EPS0375.WebServiceTFG.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "alert")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String category;
    @Column(name = "alert_type", nullable = false, length = 30)
    private String alertType;
    @Column(nullable = false, length = 20)
    private String severity;
    @Column(nullable = false, length = 120)
    private String title;
    private String description;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detection_id", unique = true)
    private Detection detection;
    @Column(name = "risk_score", precision = 4, scale = 3)
    private BigDecimal riskScore;
    @Column(nullable = false, length = 20)
    private String status;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    @Column(nullable = false)
    private boolean notified;
}
