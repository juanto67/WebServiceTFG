package com.tfg.EPS0375.WebServiceTFG.repository;

import com.tfg.EPS0375.WebServiceTFG.repository.entity.Detection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IDetectionRepository extends JpaRepository<Detection, Long> {
    @Transactional
    @Procedure(procedureName = "create_detection")
    Long createDetection(
            @Param("p_id") Long id,
            @Param("p_device_id") Long deviceId,
            @Param("p_status") String status,
            @Param("p_redis_key") String redisKey,
            @Param("p_witdh") Integer width,
            @Param("p_height") Integer height,
            @Param("p_alert_image_id") java.util.UUID alertImageId,
            @Param("p_filename") String filename
    );

    @Transactional
    @Query(value = "SELECT change_detection_status(:p_id, :p_to_status)", nativeQuery = true)
    boolean changeDetectionStatus(
            @Param("p_id") Long id,
            @Param("p_to_status") String toStatus
    );
}
