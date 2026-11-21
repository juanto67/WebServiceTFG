package com.tfg.EPS0375.WebServiceTFG.repository.entity;

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
    @Column(name = "id")
    private Long id;
    @Column(name = "category", nullable = false)
    private String category;
    @Column(name = "alert_type", nullable = false)
    private String alertType;
    @Column(name = "severity", nullable = false)
    private String severity;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "detection_id")
    private Detection detection;
    @Column(name = "risk_score")
    private BigDecimal riskScore;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    @Column(name = "notified", nullable = false)
    private boolean notified;
}
