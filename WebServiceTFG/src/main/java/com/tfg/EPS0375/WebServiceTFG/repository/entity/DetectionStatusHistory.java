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
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detection_status_history")
public class DetectionStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "detection_id", nullable = false)
    private Detection detection;
    @Column(name = "from_status")
    private String fromStatus;
    @Column(name = "to_status", nullable = false)
    private String toStatus;
    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
}
