package com.expense.tracker.service;

import com.expense.tracker.exception.ExpenseNotFoundException;
import com.expense.tracker.model.Expense;
import com.expense.tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class that contains the core business logic for processing expenses.
 */
@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    // Constructor injection for dependency injection
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    /**
     * Adds a new expense or updates an existing one, performing business validation.
     *
     * @param expense The expense to be saved.
     * @return The saved expense.
     * @throws IllegalArgumentException If any required expense properties fail validation.
     */
    public Expense addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense data cannot be null");
        }
        
        // Custom business logic validation
        if (expense.getTitle() == null || expense.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Expense title cannot be empty");
        }
        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }
        if (expense.getCategory() == null || expense.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Expense category cannot be empty");
        }
        if (expense.getDate() == null) {
            throw new IllegalArgumentException("Expense date cannot be null");
        }

        return expenseRepository.save(expense);
    }

    /**
     * Retrieves all recorded expenses.
     *
     * @return A list of all expenses.
     */
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    /**
     * Filters expenses by category and/or date range (bonus feature).
     *
     * @param category The category name to filter by (optional).
     * @param startDate The start date of the range (inclusive, optional).
     * @param endDate The end date of the range (inclusive, optional).
     * @return A list of matching expenses.
     */
    public List<Expense> getExpensesFiltered(String category, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findAll().stream()
                .filter(expense -> category == null || category.trim().isEmpty() || expense.getCategory().equalsIgnoreCase(category.trim()))
                .filter(expense -> startDate == null || !expense.getDate().isBefore(startDate))
                .filter(expense -> endDate == null || !expense.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Filters expenses by their category (case-insensitive search).
     *
     * @param category The category name to filter by.
     * @return A list of expenses matching the specified category.
     */
    public List<Expense> getExpensesByCategory(String category) {
        return getExpensesFiltered(category, null, null);
    }

    /**
     * Calculates the total sum of all recorded expenses.
     *
     * @return The overall sum of all expenses.
     */
    public double calculateTotal() {
        return expenseRepository.findAll().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Calculates the total sum of expenses matching a specific category.
     *
     * @param category The category name to sum expenses for.
     * @return The sum of expenses in the specified category.
     */
    public double calculateTotalByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return 0.0;
        }
        
        String trimmedCategory = category.trim();
        return expenseRepository.findAll().stream()
                .filter(expense -> expense.getCategory().equalsIgnoreCase(trimmedCategory))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    /**
     * Deletes an expense by its unique identifier.
     *
     * @param id The UUID of the expense to delete.
     * @throws ExpenseNotFoundException If no expense is found with the given UUID.
     */
    public void deleteExpense(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }
        
        boolean deleted = expenseRepository.deleteById(id);
        if (!deleted) {
            throw new ExpenseNotFoundException("Expense with ID " + id + " not found");
        }
    }
}
