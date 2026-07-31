package com.expense.tracker.service;

import com.expense.tracker.exception.ExpenseNotFoundException;
import com.expense.tracker.model.Expense;
import com.expense.tracker.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        
        if (expense.getTitle() == null || expense.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (expense.getCategory() == null || expense.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }
        if (expense.getDate() == null) {
            throw new IllegalArgumentException("Date is required");
        }

        return expenseRepository.save(expense);
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    // Filter by category and/or date range
    public List<Expense> getExpensesFiltered(String category, LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findAll().stream()
                .filter(expense -> category == null || category.trim().isEmpty() || expense.getCategory().equalsIgnoreCase(category.trim()))
                .filter(expense -> startDate == null || !expense.getDate().isBefore(startDate))
                .filter(expense -> endDate == null || !expense.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    public List<Expense> getExpensesByCategory(String category) {
        return getExpensesFiltered(category, null, null);
    }

    public double calculateTotal() {
        return expenseRepository.findAll().stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public double calculateTotalByCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            return 0.0;
        }
        
        String categoryName = category.trim();
        return expenseRepository.findAll().stream()
                .filter(expense -> expense.getCategory().equalsIgnoreCase(categoryName))
                .mapToDouble(Expense::getAmount)
                .sum();
    }

    public void deleteExpense(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is required");
        }
        
        boolean isDeleted = expenseRepository.deleteById(id);
        if (!isDeleted) {
            throw new ExpenseNotFoundException("Expense with ID " + id + " not found");
        }
    }
}
