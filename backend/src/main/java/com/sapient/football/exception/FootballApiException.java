package com.sapient.football.exception;

/**
 * Custom exception for API-related errors.
 */
public class FootballApiException extends RuntimeException {
    
    public FootballApiException(String message) {
        super(message);
    }
    
    public FootballApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
