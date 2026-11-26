package com.tfg.EPS0375.WebServiceTFG.service.impl;

import com.tfg.EPS0375.WebServiceTFG.service.ImageStorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileSystemImageStorageService implements ImageStorageService {
    private static final Path IMAGE_DIRECTORY = Path.of("images");

    @Override
    public void saveImage(UUID imageId, byte[] base64Image, String format) throws IOException {
        byte[] image = Base64.getDecoder().decode(base64Image);
        Files.createDirectories(IMAGE_DIRECTORY);
        Files.write(
                imagePath(imageId, format),
                image,
                StandardOpenOption.CREATE_NEW,
                StandardOpenOption.WRITE
        );
    }

    @Override
    public byte[] getImage(UUID imageId, String format) throws IOException {
        return Files.readAllBytes(imagePath(imageId, format));
    }

    private Path imagePath(UUID imageId, String format) {
        return switch (format.toUpperCase()) {
            case "JPEG", "JPG" -> IMAGE_DIRECTORY.resolve("img_" + imageId + ".jpg");
            case "PNG" -> IMAGE_DIRECTORY.resolve("img_" + imageId + ".png");
            default -> throw new IllegalArgumentException("Unsupported image format: " + format);
        };
    }
}
