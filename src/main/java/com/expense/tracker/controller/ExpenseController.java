package com.expense.tracker.controller;

import com.expense.tracker.model.Expense;
import com.expense.tracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller that exposes API endpoints for managing expenses.
 */
@RestController
@RequestMapping("/expenses")
@Validated
@Tag(name = "Expenses", description = "Operations related to managing tracker expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    // Constructor injection for service dependency
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * POST /expenses
     * Creates a new expense entry.
     */
    @PostMapping
    @Operation(summary = "Create a new expense", description = "Saves a new expense and returns the saved object with its generated UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Expense successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload or validation constraints failed")
    })
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        Expense created = expenseService.addExpense(expense);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * GET /expenses
     * Retrieves all expenses or filters them by a category and/or date range.
     */
    @GetMapping
    @Operation(summary = "Get all or filtered expenses", description = "Retrieves a list of all recorded expenses. Supports optional filtering by category name and/or date range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved expenses list")
    })
    public ResponseEntity<List<Expense>> getExpenses(
            @Parameter(description = "Filter by category name (case-insensitive)")
            @RequestParam(required = false) String category,
            @Parameter(description = "Filter by start date (inclusive, YYYY-MM-DD)")
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Filter by end date (inclusive, YYYY-MM-DD)")
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Expense> expenses = expenseService.getExpensesFiltered(category, startDate, endDate);
        return ResponseEntity.ok(expenses);
    }

    /**
     * GET /expenses/total
     * Calculates the overall sum of all recorded expenses.
     */
    @GetMapping("/total")
    @Operation(summary = "Calculate overall total", description = "Retrieves the sum of all recorded expenses in the tracker database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated total")
    })
    public ResponseEntity<Double> getTotalAmount() {
        double total = expenseService.calculateTotal();
        return ResponseEntity.ok(total);
    }

    /**
     * GET /expenses/total/{category}
     * Calculates the total sum of expenses matching a specific category.
     */
    @GetMapping("/total/{category}")
    @Operation(summary = "Calculate total by category", description = "Retrieves the sum of expenses matching a specific category name (case-insensitive).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated category total")
    })
    public ResponseEntity<Double> getTotalAmountByCategory(
            @Parameter(description = "The category name to sum expenses for")
            @PathVariable String category) {
        double total = expenseService.calculateTotalByCategory(category);
        return ResponseEntity.ok(total);
    }

    /**
     * DELETE /expenses/{id}
     * Deletes the expense associated with the given ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an expense", description = "Removes the expense record with the specified UUID from the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Expense successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Expense with the specified ID was not found")
    })
    public ResponseEntity<Void> deleteExpense(
            @Parameter(description = "The UUID of the expense to be deleted")
            @PathVariable UUID id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
