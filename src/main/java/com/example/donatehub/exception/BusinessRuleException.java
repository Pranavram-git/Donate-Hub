package com.example.donatehub.exception;

/**
 * Custom exception for business rule violations (400 Bad Request).
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
