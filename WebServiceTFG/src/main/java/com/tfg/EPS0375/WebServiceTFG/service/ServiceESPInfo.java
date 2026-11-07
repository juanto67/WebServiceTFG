package com.tfg.EPS0375.WebServiceTFG.service;

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

    public String getCameraId(String cameraModel,
                              Integer frequencyImage,
                              String transmittedImageFormat) {
        CameraDevice camera = new CameraDevice(cameraModel, frequencyImage, transmittedImageFormat);

        CameraDevice savedCamera = repository.save(camera);

        return "CAM-" + savedCamera.getId();
    }
}
