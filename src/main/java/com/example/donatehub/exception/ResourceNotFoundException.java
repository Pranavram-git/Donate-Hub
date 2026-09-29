package com.example.donatehub.exception;

/**
 * Custom exception for resource not found scenarios (404).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
