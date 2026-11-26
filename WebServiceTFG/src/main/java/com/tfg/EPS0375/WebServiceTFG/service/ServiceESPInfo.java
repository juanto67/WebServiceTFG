package com.tfg.EPS0375.WebServiceTFG.service;

import com.tfg.EPS0375.WebServiceTFG.controller.dto.DeviceRequestDto;
import com.tfg.EPS0375.WebServiceTFG.repository.IDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceESPInfo {
    private final IDeviceRepository repository;

    @Autowired
    public ServiceESPInfo(IDeviceRepository repository) {
        this.repository = repository;
    }

    public Long getCameraId(DeviceRequestDto deviceRequestDto) {
        return repository.registerDevice(
                deviceRequestDto.getBoard(),
                deviceRequestDto.getCameraName(),
                deviceRequestDto.getTransmittedImageFormat(),
                deviceRequestDto.getMacAddress(),
                (long) deviceRequestDto.getFrequencyImage(),
                deviceRequestDto.getDeviceName(),
                deviceRequestDto.getLatitude(),
                deviceRequestDto.getLongitude()
        );
    }
}
