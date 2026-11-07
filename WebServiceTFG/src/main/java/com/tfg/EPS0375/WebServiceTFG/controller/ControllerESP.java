package com.tfg.EPS0375.WebServiceTFG.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController()
@RequestMapping("/controllerESP")
public class ControllerESP {
    public int idEsp = 1;
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

            return ResponseEntity.ok(
                    "Imagen JPEG guardada correctamente: " + output
            );

        } catch (IOException exception) {

            return ResponseEntity.internalServerError()
                    .body("No se pudo guardar la imagen");
        }
    }

    @GetMapping("/getInfo")
    public ResponseEntity<String> getInfo() {
        String id = "CAM-ESP"+idEsp;
        idEsp++;
        return ResponseEntity.ok(id);
    }
}

