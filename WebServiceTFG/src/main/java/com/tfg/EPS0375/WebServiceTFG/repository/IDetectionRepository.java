package com.tfg.EPS0375.WebServiceTFG.repository;

import com.tfg.EPS0375.WebServiceTFG.repository.entity.Detection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Repository
public interface IDetectionRepository extends JpaRepository<Detection, Long> {
    @Transactional
    @Query(value = """
            SELECT create_detection(
                :p_device_id, :p_status, :p_redis_key, :p_witdh, :p_height,
                :p_alert_image_id, :p_filename, :p_image_expires_at
            )
            """, nativeQuery = true)
    Long createDetection(
            @Param("p_device_id") Long deviceId,
            @Param("p_status") String status,
            @Param("p_redis_key") String redisKey,
            @Param("p_witdh") Integer width,
            @Param("p_height") Integer height,
            @Param("p_alert_image_id") java.util.UUID alertImageId,
            @Param("p_filename") String filename,
            @Param("p_image_expires_at") LocalDateTime imageExpiresAt
    );

    @Transactional
    @Procedure(procedureName = "change_detection_status")
    void changeDetectionStatus(
            @Param("p_id") Long id,
            @Param("p_to_status") String toStatus
    );
}
