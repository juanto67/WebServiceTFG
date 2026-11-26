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
    public void saveImage(UUID imageId, byte[] base64Image) throws IOException {
        byte[] image = Base64.getDecoder().decode(base64Image);
        Files.createDirectories(IMAGE_DIRECTORY);
        Files.write(
                imagePath(imageId),
                image,
                StandardOpenOption.CREATE_NEW,
                StandardOpenOption.WRITE
        );
    }

    @Override
    public byte[] getImage(UUID imageId) throws IOException {
        return Files.readAllBytes(imagePath(imageId));
    }

    private Path imagePath(UUID imageId) {
        return IMAGE_DIRECTORY.resolve("img_" + imageId);
    }
}
