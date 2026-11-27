package com.tfg.EPS0375.WebServiceTFG.service;

import com.tfg.EPS0375.WebServiceTFG.repository.IDeviceRepository;
import com.tfg.EPS0375.WebServiceTFG.repository.IDetectionRepository;
import com.tfg.EPS0375.WebServiceTFG.repository.entity.Device;
import com.tfg.EPS0375.WebServiceTFG.service.enums.DetectionStatus;
import com.tfg.EPS0375.WebServiceTFG.service.exception.ImageFormatMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class ServiceESPImage {

    private final IDeviceRepository deviceRepository;
    private final IDetectionRepository detectionRepository;
    private final ImageStorageService imageStorageService;

    @Autowired
    public ServiceESPImage(
            IDeviceRepository deviceRepository,
            IDetectionRepository detectionRepository,
            ImageStorageService imageStorageService) {
        this.deviceRepository = deviceRepository;
        this.detectionRepository = detectionRepository;
        this.imageStorageService = imageStorageService;
    }

    public String newImage(byte[] base64Image, String format, Long deviceId, int width, int height) throws IOException {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dispositivo no encontrado"));

        String expectedFormat = device.getCameraModel().getTransmittedImageFormat();
        if (!expectedFormat.equals(format)) {
            throw new ImageFormatMismatchException(expectedFormat, format);
        }

        deviceRepository.deviceSeen(deviceId);

        UUID imageId = UUID.randomUUID();
        String imageName = "img_" + imageId;
        String redisKey = deviceId + ":" + imageId;

        imageStorageService.saveImage(imageId, base64Image, format);

        detectionRepository.createDetection(
                deviceId,
                DetectionStatus.PENDING.name(),
                redisKey,
                width,
                height,
                imageId,
                imageName,
                LocalDateTime.now(ZoneId.of("UTC")).plusHours(24)
        );

        return imageName;
    }
}
