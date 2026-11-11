package com.tfg.EPS0375.WebServiceTFG.service;

import com.tfg.EPS0375.WebServiceTFG.controller.dto.DeviceRequestDto;
import com.tfg.EPS0375.WebServiceTFG.repository.ICameraDeviceRepository;
import com.tfg.EPS0375.WebServiceTFG.repository.entity.CameraDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceESPInfo {
    public final ICameraDeviceRepository repository;

    @Autowired
    public ServiceESPInfo(ICameraDeviceRepository repository) {
        this.repository = repository;
    }

    public String getCameraId(DeviceRequestDto deviceRequestDto) {
        CameraDevice camera = new CameraDevice(deviceRequestDto.getCameraModel(), deviceRequestDto.getFrequencyImage(), deviceRequestDto.getTransmittedImageFormat(), deviceRequestDto.getMacAddress(), deviceRequestDto.getLatitude(), deviceRequestDto.getLongitude());
        CameraDevice cameraFromDb = repository.getByMacAddress(deviceRequestDto.getMacAddress());

        if (cameraFromDb != null) {
            return "CAM-" + cameraFromDb.getId();
        }
        CameraDevice savedCamera = repository.save(camera);

        return "CAM-" + savedCamera.getId();
    }
}
