package com.expense.tracker.service;

import com.expense.tracker.exception.ExpenseNotFoundException;
import com.expense.tracker.model.Expense;
import com.expense.tracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTests {

    @Mock
    private ExpenseRepository repository;

    @InjectMocks
    private ExpenseService service;

    private Expense foodExpense;
    private Expense utilitiesExpense;

    @BeforeEach
    void setUp() {
        foodExpense = new Expense(UUID.randomUUID(), "Grocery", 50.0, "Food", LocalDate.now());
        utilitiesExpense = new Expense(UUID.randomUUID(), "Electricity", 100.0, "Utilities", LocalDate.now());
    }

    @Test
    void testAddExpense_Success() {
        when(repository.save(any(Expense.class))).thenReturn(foodExpense);

        Expense saved = service.addExpense(foodExpense);

        assertNotNull(saved);
        assertEquals("Grocery", saved.getTitle());
        verify(repository, times(1)).save(foodExpense);
    }

    @Test
    void testAddExpense_InvalidData_ThrowsException() {
        Expense invalid = new Expense(null, "", -10.0, null, null);

        assertThrows(IllegalArgumentException.class, () -> service.addExpense(invalid));
        verify(repository, never()).save(any());
    }

    @Test
    void testGetAllExpenses() {
        when(repository.findAll()).thenReturn(Arrays.asList(foodExpense, utilitiesExpense));

        List<Expense> result = service.getAllExpenses();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetExpensesByCategory() {
        when(repository.findAll()).thenReturn(Arrays.asList(foodExpense, utilitiesExpense));

        List<Expense> result = service.getExpensesByCategory("food");

        assertEquals(1, result.size());
        assertEquals("Food", result.get(0).getCategory());
    }

    @Test
    void testCalculateTotal() {
        when(repository.findAll()).thenReturn(Arrays.asList(foodExpense, utilitiesExpense));

        double total = service.calculateTotal();

        assertEquals(150.0, total);
    }

    @Test
    void testCalculateTotalByCategory() {
        when(repository.findAll()).thenReturn(Arrays.asList(foodExpense, utilitiesExpense));

        double totalFood = service.calculateTotalByCategory("Food");
        double totalUtilities = service.calculateTotalByCategory("Utilities");

        assertEquals(50.0, totalFood);
        assertEquals(100.0, totalUtilities);
    }

    @Test
    void testDeleteExpense_Success() {
        UUID id = foodExpense.getId();
        when(repository.deleteById(id)).thenReturn(true);

        assertDoesNotThrow(() -> service.deleteExpense(id));
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteExpense_NotFound_ThrowsException() {
        UUID randomId = UUID.randomUUID();
        when(repository.deleteById(randomId)).thenReturn(false);

        assertThrows(ExpenseNotFoundException.class, () -> service.deleteExpense(randomId));
        verify(repository, times(1)).deleteById(randomId);
    }
}
