package com.tfg.EPS0375.WebServiceTFG.repository;

import com.tfg.EPS0375.WebServiceTFG.repository.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IDeviceRepository extends JpaRepository<Device, Long> {
    Device getByMacAddress(String macAddress);

    @Transactional
    @Procedure(procedureName = "device_seen")
    void deviceSeen(@Param("p_device_id") Long deviceId);

    @Transactional
    @Query(value = """
            SELECT register_device(
                :p_board, :p_camera_name, :p_image_format, :p_mac_address,
                :p_interval_s, :p_device_name, :p_latitude, :p_longitude
            )
            """, nativeQuery = true)
    Long registerDevice(
            @Param("p_board") String board,
            @Param("p_camera_name") String cameraName,
            @Param("p_image_format") String imageFormat,
            @Param("p_mac_address") String macAddress,
            @Param("p_interval_s") Long intervalSeconds,
            @Param("p_device_name") String deviceName,
            @Param("p_latitude") Double latitude,
            @Param("p_longitude") Double longitude
    );
}
