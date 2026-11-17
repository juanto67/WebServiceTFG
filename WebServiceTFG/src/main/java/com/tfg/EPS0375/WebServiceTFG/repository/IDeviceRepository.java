package com.tfg.EPS0375.WebServiceTFG.repository;

import com.tfg.EPS0375.WebServiceTFG.repository.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Repository
public interface IDeviceRepository extends JpaRepository<Device, Long> {
    Device getByMacAddress(String macAddress);

    @Transactional
    @Procedure(procedureName = "device_seen")
    void deviceSeen(@Param("p_device_id") Long deviceId);

    @Transactional
    @Procedure(procedureName = "register_device")
    Map<String, Object> registerDevice(
            @Param("p_board") String board,
            @Param("p_camera_name") String cameraName,
            @Param("p_image_format") String imageFormat,
            @Param("p_mac_address") String macAddress,
            @Param("p_interval_s") Long intervalSeconds,
            @Param("p_device_name") String deviceName,
            @Param("p_latitude") Double latitude,
            @Param("p_longitude") Double longitude,
            @Param("p_device_id") Long deviceId,
            @Param("p_camera_model_id") Short cameraModelId
    );
}
