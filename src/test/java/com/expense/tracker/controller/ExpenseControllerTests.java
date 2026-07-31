package com.expense.tracker.controller;

import com.expense.tracker.exception.ExpenseNotFoundException;
import com.expense.tracker.model.Expense;
import com.expense.tracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpenseService service;

    private ObjectMapper objectMapper;
    private Expense validExpense;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        validExpense = new Expense(UUID.randomUUID(), "Cinema Ticket", 12.50, "Entertainment", LocalDate.now());
    }

    @Test
    void testCreateExpense_Success() throws Exception {
        when(service.addExpense(any(Expense.class))).thenReturn(validExpense);

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validExpense)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Cinema Ticket"))
                .andExpect(jsonPath("$.amount").value(12.50));
    }

    @Test
    void testCreateExpense_InvalidData_ReturnsBadRequest() throws Exception {
        Expense invalidExpense = new Expense(null, "", -5.0, null, null);

        mockMvc.perform(post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidExpense)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.title").exists())
                .andExpect(jsonPath("$.errors.amount").exists())
                .andExpect(jsonPath("$.errors.category").exists())
                .andExpect(jsonPath("$.errors.date").exists());
    }

    @Test
    void testGetExpenses_NoParams() throws Exception {
        when(service.getAllExpenses()).thenReturn(Arrays.asList(validExpense));

        mockMvc.perform(get("/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Cinema Ticket"));
    }

    @Test
    void testGetExpenses_WithCategoryParam() throws Exception {
        when(service.getExpensesByCategory("Entertainment")).thenReturn(Arrays.asList(validExpense));

        mockMvc.perform(get("/expenses").param("category", "Entertainment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].category").value("Entertainment"));
    }

    @Test
    void testGetTotalAmount() throws Exception {
        when(service.calculateTotal()).thenReturn(150.0);

        mockMvc.perform(get("/expenses/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("150.0"));
    }

    @Test
    void testGetTotalAmountByCategory() throws Exception {
        when(service.calculateTotalByCategory("Entertainment")).thenReturn(12.50);

        mockMvc.perform(get("/expenses/total/Entertainment"))
                .andExpect(status().isOk())
                .andExpect(content().string("12.5"));
    }

    @Test
    void testDeleteExpense_Success() throws Exception {
        UUID id = validExpense.getId();
        doNothing().when(service).deleteExpense(id);

        mockMvc.perform(delete("/expenses/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteExpense_NotFound() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new ExpenseNotFoundException("Expense not found")).when(service).deleteExpense(id);

        mockMvc.perform(delete("/expenses/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Expense not found"));
    }
}
