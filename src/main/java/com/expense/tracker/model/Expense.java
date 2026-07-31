package com.expense.tracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents an expense entry in the tracker.
 */
public class Expense {

    // Unique identifier for the expense (automatically generated or assigned)
    private UUID id;

    // The name/title of the expense (must not be blank and within reasonable length)
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    // The monetary value of the expense (must be a positive number)
    @Positive(message = "Amount must be greater than zero")
    private double amount;

    // The category the expense belongs to (must not be blank)
    @NotBlank(message = "Category is required")
    private String category;

    // The date when the expense was incurred (cannot be null)
    @NotNull(message = "Date is required")
    private LocalDate date;

    // Default constructor (required for JSON deserialization)
    public Expense() {
    }

    // Full constructor
    public Expense(UUID id, String title, double amount, String category, LocalDate date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
