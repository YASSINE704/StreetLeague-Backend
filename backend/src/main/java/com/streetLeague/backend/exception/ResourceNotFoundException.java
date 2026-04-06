package com.streetLeague.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested resource is not found in the database.
 * This is a custom exception that returns a 404 NOT_FOUND HTTP status.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    /**
     * Constructs a ResourceNotFoundException with a detailed error message.
     * 
     * @param message The error message describing what resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
