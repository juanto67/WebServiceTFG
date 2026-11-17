package com.tfg.EPS0375.WebServiceTFG.repository;

import com.tfg.EPS0375.WebServiceTFG.repository.entity.DetectionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDetectionStatusHistoryRepository extends JpaRepository<DetectionStatusHistory, Long> {
}
