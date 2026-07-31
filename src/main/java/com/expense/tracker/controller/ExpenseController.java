package com.expense.tracker.controller;

import com.expense.tracker.model.Expense;
import com.expense.tracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller that exposes API endpoints for managing expenses.
 */
@RestController
@RequestMapping("/expenses")
@Validated
public class ExpenseController {

    private final ExpenseService expenseService;

    // Constructor injection for service dependency
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * POST /expenses
     * Creates a new expense entry.
     *
     * @param expense The expense payload validated by @Valid annotations on the model.
     * @return The created expense with HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        Expense created = expenseService.addExpense(expense);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * GET /expenses (with optional ?category=Category filter)
     * Retrieves all expenses or filters them by a specific category.
     *
     * @param category The category name to filter by (optional).
     * @return A list of matching expenses and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(@RequestParam(required = false) String category) {
        List<Expense> expenses;
        if (category != null && !category.trim().isEmpty()) {
            expenses = expenseService.getExpensesByCategory(category);
        } else {
            expenses = expenseService.getAllExpenses();
        }
        return ResponseEntity.ok(expenses);
    }

    /**
     * GET /expenses/total
     * Calculates the overall sum of all recorded expenses.
     *
     * @return The overall sum and HTTP status 200 (OK).
     */
    @GetMapping("/total")
    public ResponseEntity<Double> getTotalAmount() {
        double total = expenseService.calculateTotal();
        return ResponseEntity.ok(total);
    }

    /**
     * GET /expenses/total/{category}
     * Calculates the total sum of expenses matching a specific category.
     *
     * @param category The category name.
     * @return The category total and HTTP status 200 (OK).
     */
    @GetMapping("/total/{category}")
    public ResponseEntity<Double> getTotalAmountByCategory(@PathVariable String category) {
        double total = expenseService.calculateTotalByCategory(category);
        return ResponseEntity.ok(total);
    }

    /**
     * DELETE /expenses/{id}
     * Deletes the expense associated with the given ID.
     *
     * @param id The UUID of the expense to delete.
     * @return HTTP status 204 (No Content) upon successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
