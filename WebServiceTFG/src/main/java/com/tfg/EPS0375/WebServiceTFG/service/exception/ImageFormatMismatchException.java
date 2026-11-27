package com.tfg.EPS0375.WebServiceTFG.service.exception;

public class ImageFormatMismatchException extends RuntimeException {
    public ImageFormatMismatchException(String expectedFormat, String receivedFormat) {
        super("Formato de imagen incorrecto. Se esperaba " + expectedFormat + " y se recibió " + receivedFormat);
    }
}
