package com.expense.tracker.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.expense.tracker.model.Expense;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing Expense persistence in a local JSON file.
 */
@Repository
public class ExpenseRepository {

    private static final String FILE_PATH = "src/main/resources/expenses.json";
    
    private final ObjectMapper objectMapper;
    private final List<Expense> expenses;

    // Constructor initializes ObjectMapper and empty memory list
    public ExpenseRepository() {
        this.objectMapper = new ObjectMapper();
        // Register JavaTimeModule to handle LocalDate serialization/deserialization
        this.objectMapper.registerModule(new JavaTimeModule());
        this.expenses = Collections.synchronizedList(new ArrayList<>());
    }

    // Load data from JSON file on application startup
    @PostConstruct
    public void init() {
        loadExpenses();
    }

    // Retrieve all expenses
    public List<Expense> findAll() {
        return new ArrayList<>(expenses);
    }

    // Find expense by ID
    public Optional<Expense> findById(UUID id) {
        return expenses.stream()
                .filter(expense -> expense.getId().equals(id))
                .findFirst();
    }

    // Save a new or existing expense
    public Expense save(Expense expense) {
        if (expense.getId() == null) {
            expense.setId(UUID.randomUUID());
        }
        
        // Remove existing entry if it's an update
        expenses.removeIf(e -> e.getId().equals(expense.getId()));
        expenses.add(expense);
        
        persistExpenses();
        return expense;
    }

    // Delete expense by ID
    public boolean deleteById(UUID id) {
        boolean removed = expenses.removeIf(expense -> expense.getId().equals(id));
        if (removed) {
            persistExpenses();
        }
        return removed;
    }

    // Read expenses from the JSON file
    private void loadExpenses() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // Create directories and empty JSON file if it does not exist
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
                // Initialize with empty array
                objectMapper.writeValue(file, new ArrayList<Expense>());
            } catch (IOException e) {
                throw new RuntimeException("Could not initialize expenses.json file", e);
            }
        }

        try {
            // Read list of expenses from JSON file
            List<Expense> loaded = objectMapper.readValue(file, new TypeReference<List<Expense>>() {});
            expenses.clear();
            if (loaded != null) {
                expenses.addAll(loaded);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read expenses from JSON file", e);
        }
    }

    // Write current list of expenses to the JSON file
    private void persistExpenses() {
        File file = new File(FILE_PATH);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, expenses);
        } catch (IOException e) {
            throw new RuntimeException("Could not write expenses to JSON file", e);
        }
    }
}
