package com.expense.tracker.repository;

import com.expense.tracker.model.Expense;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseRepositoryTests {

    private ExpenseRepository repository;
    private final String testFilePath = "src/main/resources/expenses.json";

    @BeforeEach
    void setUp() {
        // Clean up or delete the test file before setup to ensure clean test environment
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
        repository = new ExpenseRepository();
        repository.init();
    }

    @AfterEach
    void tearDown() {
        // Clean up after tests
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveAndFindAll() {
        Expense expense = new Expense(UUID.randomUUID(), "Grocery Shopping", 54.20, "Food", LocalDate.now());
        repository.save(expense);

        List<Expense> all = repository.findAll();
        assertEquals(1, all.size());
        assertEquals("Grocery Shopping", all.get(0).getTitle());
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Expense expense = new Expense(id, "Internet Bill", 49.99, "Utilities", LocalDate.now());
        repository.save(expense);

        Optional<Expense> found = repository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(49.99, found.get().getAmount());
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();
        Expense expense = new Expense(id, "Bus Ticket", 2.50, "Transport", LocalDate.now());
        repository.save(expense);

        boolean deleted = repository.deleteById(id);
        assertTrue(deleted);
        
        Optional<Expense> found = repository.findById(id);
        assertFalse(found.isPresent());
    }
}
