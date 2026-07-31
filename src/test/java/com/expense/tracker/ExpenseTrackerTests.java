package com.expense.tracker;

import com.expense.tracker.model.Expense;
import com.expense.tracker.repository.ExpenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end integration tests for the Smart Expense Tracker API.
 * Verifies all API endpoints and business validation logic in a real Spring Boot environment.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ExpenseTrackerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpenseRepository repository;

    private ObjectMapper objectMapper;
    private final String dbFilePath = "src/main/resources/expenses.json";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        
        // Clear the data store before each test to guarantee test isolation
        File file = new File(dbFilePath);
        if (file.exists()) {
            file.delete();
        }
        repository.init(); // Reload database as empty
    }

    @AfterEach
    void tearDown() {
        // Clean up the data store after each test
        File file = new File(dbFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 1. Test: Add expense
     * Verifies that a valid expense is saved successfully and returns 201 Created.
     */
    @Test
    void testAddExpense() throws Exception {
        Expense expense = new Expense(null, "Lunch", 15.50, "Food", LocalDate.now());

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Lunch"))
                .andExpect(jsonPath("$.amount").value(15.50))
                .andExpect(jsonPath("$.category").value("Food"));

        assertEquals(1, repository.findAll().size());
    }

    /**
     * 2. Test: Delete expense
     * Verifies that an existing expense can be deleted by ID, returning 204 No Content.
     */
    @Test
    void testDeleteExpense() throws Exception {
        Expense expense = repository.save(new Expense(null, "Coffee", 4.50, "Food", LocalDate.now()));
        UUID id = expense.getId();

        mockMvc.perform(delete("/expenses/{id}", id))
                .andExpect(status().isNoContent());

        assertEquals(0, repository.findAll().size());
    }

    /**
     * 3. Test: Get all expenses
     * Verifies that calling GET /expenses retrieves all saved expense records.
     */
    @Test
    void testGetAllExpenses() throws Exception {
        repository.save(new Expense(null, "Grocery", 45.00, "Food", LocalDate.now()));
        repository.save(new Expense(null, "Uber ride", 18.00, "Transport", LocalDate.now()));

        mockMvc.perform(get("/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Grocery"))
                .andExpect(jsonPath("$[1].title").value("Uber ride"));
    }

    /**
     * 4. Test: Category filter
     * Verifies that the ?category= query parameter filters the expenses list correctly.
     */
    @Test
    void testCategoryFilter() throws Exception {
        repository.save(new Expense(null, "Pizza", 22.00, "Food", LocalDate.now()));
        repository.save(new Expense(null, "Subway ticket", 3.00, "Transport", LocalDate.now()));

        // Filter by "Food"
        mockMvc.perform(get("/expenses").param("category", "Food"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Pizza"))
                .andExpect(jsonPath("$[0].category").value("Food"));
    }

    /**
     * 5. Test: Overall total
     * Verifies that GET /expenses/total correctly calculates the sum of all expenses.
     */
    @Test
    void testOverallTotal() throws Exception {
        repository.save(new Expense(null, "Item A", 10.00, "A", LocalDate.now()));
        repository.save(new Expense(null, "Item B", 25.50, "B", LocalDate.now()));

        mockMvc.perform(get("/expenses/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("35.5"));
    }

    /**
     * 6. Test: Category total
     * Verifies that GET /expenses/total/{category} sums expenses matching that category.
     */
    @Test
    void testCategoryTotal() throws Exception {
        repository.save(new Expense(null, "Burger", 12.00, "Food", LocalDate.now()));
        repository.save(new Expense(null, "Tacos", 8.00, "Food", LocalDate.now()));
        repository.save(new Expense(null, "Bus ride", 2.50, "Transport", LocalDate.now()));

        mockMvc.perform(get("/expenses/total/Food"))
                .andExpect(status().isOk())
                .andExpect(content().string("20.0"));
    }

    /**
     * 7. Test: Invalid amount
     * Verifies that trying to add an expense with a negative amount returns 400 Bad Request.
     */
    @Test
    void testInvalidAmount() throws Exception {
        Expense expense = new Expense(null, "Gas", -45.00, "Transport", LocalDate.now());

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.amount").exists());
    }

    /**
     * 8. Test: Blank title
     * Verifies that sending a blank title returns 400 Bad Request.
     */
    @Test
    void testBlankTitle() throws Exception {
        Expense expense = new Expense(null, "   ", 10.00, "Shopping", LocalDate.now());

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expense)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.title").value("Title is required"));
    }

    /**
     * 9. Test: Delete invalid id
     * Verifies that attempting to delete a non-existent UUID returns 404 Not Found.
     */
    @Test
    void testDeleteInvalidId() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/expenses/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Expense with ID " + id + " not found"));
    }
}
