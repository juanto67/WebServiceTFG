package com.tfg.EPS0375.WebServiceTFG.controller;

import com.tfg.EPS0375.WebServiceTFG.controller.dto.DeviceRequestDto;
import com.tfg.EPS0375.WebServiceTFG.service.ServiceESPImage;
import com.tfg.EPS0375.WebServiceTFG.service.ServiceESPInfo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/controllerESP")
public class ControllerESP {

    public final ServiceESPInfo serviceESPInfo;
    public final ServiceESPImage serviceESPImage;

    @Autowired
    public ControllerESP(ServiceESPInfo serviceESPInfo, ServiceESPImage serviceESPImage) {
        this.serviceESPInfo = serviceESPInfo;
        this.serviceESPImage = serviceESPImage;
    }

    @PostMapping("/newImage")
    public ResponseEntity<String> catchNewImage(
            @RequestBody byte[] base64Image,
            @RequestHeader("X-Width") int width,
            @RequestHeader("X-Height") int height,
            @RequestHeader("X-Format") String format,
            @RequestHeader("X-Device-Id") Long deviceId) {
        try {
            if (base64Image == null || base64Image.length == 0) {
                return ResponseEntity.badRequest().body("Imagen vacía");
            }
            String imageName = serviceESPImage.newImage(base64Image, format, deviceId, width, height);
            return ResponseEntity.ok("Imagen guardada correctamente: " + imageName);

        } catch (IOException exception) {
            return ResponseEntity.internalServerError().body("No se pudo guardar la imagen");
        }
    }

    @PostMapping("/newDevice")
    public ResponseEntity<Long> catchNewDevice(@Valid @RequestBody DeviceRequestDto deviceRequestDto) {
        Long id = serviceESPInfo.getCameraId(deviceRequestDto);
        return ResponseEntity.ok(id);
    }
}
