package com.tfg.EPS0375.WebServiceTFG.service;

import java.io.IOException;
import java.util.UUID;

public interface ImageStorageService {
    void saveImage(UUID imageId, byte[] base64Image, String format) throws IOException;

    byte[] getImage(UUID imageId, String format) throws IOException;
}
