package com.tfg.EPS0375.WebServiceTFG.exception;

import com.tfg.EPS0375.WebServiceTFG.service.exception.ImageFormatMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ImageFormatMismatchException.class)
    public ResponseEntity<String> handleImageFormatMismatch(ImageFormatMismatchException exception) {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
