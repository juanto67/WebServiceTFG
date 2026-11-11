package com.tfg.EPS0375.WebServiceTFG.controller;

import com.tfg.EPS0375.WebServiceTFG.controller.dto.DeviceRequestDto;
import com.tfg.EPS0375.WebServiceTFG.service.ServiceESPInfo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/controllerESP")
public class ControllerESP {

    public final ServiceESPInfo serviceESPInfo;

    @Autowired
    public ControllerESP(ServiceESPInfo serviceESPInfo) {
        this.serviceESPInfo = serviceESPInfo;
    }

    @PostMapping("/newImage")
    public ResponseEntity<String> catchNewImage(
            @RequestBody byte[] jpeg,
            @RequestHeader("X-Width") int width,
            @RequestHeader("X-Height") int height,
            @RequestHeader("X-Format") String format) {
        try {
            if (jpeg == null || jpeg.length == 0) {
                return ResponseEntity.badRequest()
                        .body("Imagen vacía");
            }

            Path output = Path.of("received-image.jpg");

            Files.write(output, jpeg);

            return ResponseEntity.ok("Imagen JPEG guardada correctamente: " + output);

        } catch (IOException exception) {

            return ResponseEntity.internalServerError().body("No se pudo guardar la imagen");
        }
    }

    @PostMapping("/newDevice")
    public ResponseEntity<String> catchNewDevice(@Valid @RequestBody DeviceRequestDto deviceRequestDto) {
        String id = serviceESPInfo.getCameraId(deviceRequestDto);
        return ResponseEntity.ok(id);
    }
}

