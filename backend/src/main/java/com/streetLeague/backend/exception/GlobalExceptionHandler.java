package com.streetLeague.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for the StreetLeague Backend API.
 * Centralizes exception handling across all REST controllers.
 * 
 * This class provides consistent error responses for various exception types
 * including validation errors, resource not found, and general runtime exceptions.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException when a resource is not found.
     * Returns 404 NOT_FOUND status with error details.
     * 
     * @param ex The ResourceNotFoundException
     * @param request The WebRequest
     * @return ResponseEntity with error details and 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            WebRequest request) {
        
        Map<String, Object> errorDetails = buildErrorResponse(
            "Resource Not Found",
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles validation errors from @Valid annotation.
     * Returns 400 BAD_REQUEST status with field-level error details.
     * 
     * @param ex The MethodArgumentNotValidException
     * @param request The WebRequest
     * @return ResponseEntity with validation errors and 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        
        Map<String, Object> errorDetails = buildErrorResponse(
            "Validation Error",
            "One or more fields are invalid",
            HttpStatus.BAD_REQUEST.value()
        );
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        
        errorDetails.put("fieldErrors", fieldErrors);
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic runtime exceptions.
     * Returns 500 INTERNAL_SERVER_ERROR status.
     * 
     * @param ex The Exception
     * @param request The WebRequest
     * @return ResponseEntity with error details and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGlobalException(
            Exception ex,
            WebRequest request) {
        
        Map<String, Object> errorDetails = buildErrorResponse(
            "Internal Server Error",
            ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Builds a standardized error response structure.
     * 
     * @param message The error message
     * @param details The error details
     * @param status The HTTP status code
     * @return A Map containing formatted error information
     */
    private Map<String, Object> buildErrorResponse(String message, String details, int status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status);
        errorResponse.put("error", message);
        errorResponse.put("message", details);
        return errorResponse;
    }
}
