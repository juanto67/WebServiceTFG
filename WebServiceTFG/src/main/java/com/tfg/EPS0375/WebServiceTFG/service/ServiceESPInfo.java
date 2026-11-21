package com.tfg.EPS0375.WebServiceTFG.service;

import com.tfg.EPS0375.WebServiceTFG.controller.dto.DeviceRequestDto;
import com.tfg.EPS0375.WebServiceTFG.repository.IDeviceRepository;
import com.tfg.EPS0375.WebServiceTFG.repository.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceESPInfo {
    public final IDeviceRepository repository;

    @Autowired
    public ServiceESPInfo(IDeviceRepository repository) {
        this.repository = repository;
    }

    public String getCameraId(DeviceRequestDto deviceRequestDto) {
        Device camera = new Device(deviceRequestDto.getCameraModel(), deviceRequestDto.getFrequencyImage(), deviceRequestDto.getTransmittedImageFormat(), deviceRequestDto.getMacAddress(), deviceRequestDto.getLatitude(), deviceRequestDto.getLongitude());
        Device cameraFromDb = repository.getByMacAddress(deviceRequestDto.getMacAddress());

        if (cameraFromDb != null) {
            return "CAM-" + cameraFromDb.getId();
        }
        Device savedCamera = repository.save(camera);

        return "CAM-" + savedCamera.getId();
    }
}
