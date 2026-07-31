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

@Repository
public class ExpenseRepository {

    private static final String FILE_PATH = "src/main/resources/expenses.json";
    
    private final ObjectMapper objectMapper;
    private final List<Expense> expenses;

    public ExpenseRepository() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.expenses = Collections.synchronizedList(new ArrayList<>());
    }

    @PostConstruct
    public void init() {
        loadExpenses();
    }

    public List<Expense> findAll() {
        return new ArrayList<>(expenses);
    }

    public Optional<Expense> findById(UUID id) {
        return expenses.stream()
                .filter(expense -> expense.getId().equals(id))
                .findFirst();
    }

    public Expense save(Expense expense) {
        if (expense.getId() == null) {
            expense.setId(UUID.randomUUID());
        }
        
        expenses.removeIf(e -> e.getId().equals(expense.getId()));
        expenses.add(expense);
        
        persistExpenses();
        return expense;
    }

    public boolean deleteById(UUID id) {
        boolean removed = expenses.removeIf(expense -> expense.getId().equals(id));
        if (removed) {
            persistExpenses();
        }
        return removed;
    }

    private void loadExpenses() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                File parentDir = file.getParentFile();
                if (parentDir != null && !parentDir.exists()) {
                    parentDir.mkdirs();
                }
                file.createNewFile();
                objectMapper.writeValue(file, new ArrayList<Expense>());
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize database file", e);
            }
        }

        try {
            List<Expense> loadedExpenses = objectMapper.readValue(file, new TypeReference<List<Expense>>() {});
            expenses.clear();
            if (loadedExpenses != null) {
                expenses.addAll(loadedExpenses);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read expenses", e);
        }
    }

    private void persistExpenses() {
        File file = new File(FILE_PATH);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, expenses);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save expenses", e);
        }
    }
}
