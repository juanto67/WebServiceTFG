package com.tfg.EPS0375.WebServiceTFG.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController()
@RequestMapping("/controllerESP")
public class ControllerESP {

    @PostMapping("/newImage")
    public ResponseEntity<String> getESP(
            @RequestBody byte[] rgb565,
            @RequestHeader("X-Width") int width,
            @RequestHeader("X-Height") int height,
            @RequestHeader("X-Format") String format) {
        if (!"RGB565".equalsIgnoreCase(format)) {
            return ResponseEntity.badRequest().body("X-Format debe ser RGB565");
        }
        if (width <= 0 || height <= 0 || width > 4096 || height > 4096) {
            return ResponseEntity.badRequest().body("Dimensiones no validas");
        }

        long expectedLength = (long) width * height * 2;
        if (rgb565.length != expectedLength) {
            return ResponseEntity.badRequest()
                    .body("El tamano del cuerpo no coincide con las dimensiones RGB565");
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0, offset = 0; y < height; y++) {
            for (int x = 0; x < width; x++, offset += 2) {
                int pixel = ((rgb565[offset] & 0xFF) << 8) | (rgb565[offset + 1] & 0xFF);
                int red = ((pixel >> 11) & 0x1F) * 255 / 31;
                int green = ((pixel >> 5) & 0x3F) * 255 / 63;
                int blue = (pixel & 0x1F) * 255 / 31;
                image.setRGB(x, y, (red << 16) | (green << 8) | blue);
            }
        }

        Path output = Path.of("src", "main", "resources", "received-image.jpg");
        try {
            Files.createDirectories(output.getParent());
            if (!ImageIO.write(image, "jpg", output.toFile())) {
                return ResponseEntity.internalServerError().body("No se pudo codificar la imagen como JPEG");
            }
        } catch (IOException exception) {
            return ResponseEntity.internalServerError().body("No se pudo guardar la imagen");
        }

        return ResponseEntity.ok("Imagen guardada en " + output);
    }
}
