package com.tfg.EPS0375.WebServiceTFG.repository;

import com.tfg.EPS0375.WebServiceTFG.repository.entity.CameraDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICameraDeviceRepository extends JpaRepository<CameraDevice, Long> {
    CameraDevice getByMacAddress(String macAddress);
}