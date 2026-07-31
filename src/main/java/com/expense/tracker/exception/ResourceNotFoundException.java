package com.expense.tracker.exception;

/**
 * Base exception thrown when any requested resource is not found in the system.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
