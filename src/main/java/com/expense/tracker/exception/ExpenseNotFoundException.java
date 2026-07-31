package com.expense.tracker.exception;

/**
 * Exception thrown when a requested expense cannot be found in the repository.
 */
public class ExpenseNotFoundException extends ResourceNotFoundException {

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
