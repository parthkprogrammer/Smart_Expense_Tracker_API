# AI Generation and Implementation Notes

This document describes the collaboration between the AI coding assistant and the engineer, detailing generated code, developer modifications, and rejected options.

---

## What the AI Generated
1. **Maven Project Structure (`pom.xml`)**:
   - Initialized Java 17 and Spring Boot 3.3.0 dependencies strictly limiting imports to Spring Web, Jakarta Validation, and Spring Boot Test.
2. **Standard Packages**:
   - Created packages for `controller`, `service`, `repository`, `model`, `exception`, and `config`.
3. **Core API Elements**:
   - **Model**: `Expense.java` domain model containing standard constructors, getters/setters, and Jakarta Bean Validation constraints (`@NotBlank`, `@Positive`, `@NotNull`, `@Size`).
   - **Repository**: `ExpenseRepository.java` which manages data storage using a synchronized in-memory list and persists updates to `src/main/resources/expenses.json` using Jackson `ObjectMapper` + `JavaTimeModule` for native `LocalDate` support.
   - **Service**: `ExpenseService.java` wrapping core business operations (add, list, delete, calculate totals) with custom business validation checking.
   - **Controller**: `ExpenseController.java` mapping routing and validation annotations to the corresponding service layer.
   - **Exception Mappings**: `GlobalExceptionHandler.java` translating runtime faults (`ResourceNotFoundException`, `IllegalArgumentException`, `MethodArgumentNotValidException`) into standard JSON response bodies.
4. **Unit & Integration Tests**:
   - Wrote isolated repository unit tests (`ExpenseRepositoryTests`), Mockito service unit tests (`ExpenseServiceTests`), MockMvc controller tests (`ExpenseControllerTests`), and full end-to-end integration tests (`ExpenseTrackerTests`).

---

## What We Changed
1. **Date Range Filter (Bonus Feature)**:
   - Extended the `GET /expenses` endpoint to accept optional `startDate` and `endDate` query parameters. Filters expenses in-memory matching the range (inclusive) case-insensitively, and added related integration tests.
2. **Unified Error Response Structure**:
   - Updated the global error handlers to return consistent REST payload fields: `timestamp`, `status`, and `message`. Formatted JSR-380 validation binding errors to map fields directly within a structured `errors` dictionary.
3. **Rebase & Git Conflict Resolution**:
   - Resolved merge conflicts on `.gitignore` between the remote's template file and the local custom ignore patterns, preserving all IDE and Maven target directories.
4. **Mock Stubbing Correction**:
   - Updated the controller tests mock stubbing to mock `getExpensesFiltered()` instead of `getAllExpenses()` once the controller's implementation transitioned to the new filtering method.

---

## What We Rejected and Why
1. **Lombok Dependency**:
   - **Rejected**: Avoided Lombok annotations like `@Data` or `@NoArgsConstructor` because the requirement specified "Do not use Lombok." Written traditional getters, setters, and constructors to keep the code plain, clean, and developer-friendly.
2. **Relational Database (H2 / SQLite / JPA)**:
   - **Rejected**: Avoided integrating H2 in-memory DB or Spring Data JPA because the prompt specified "No database (use memory or local JSON file)." Used Jackson serialization to read and write records directly from/to `src/main/resources/expenses.json`.
3. **Authentication & Security Starters**:
   - **Rejected**: Kept Spring Security out of the project dependencies because the requirement strictly instructed "No authentication."
4. **Over-engineered Architecture (e.g. MapStruct, DTO layers, CQRS)**:
   - **Rejected**: Avoided mapping models to extra DTOs or using query command splitting to keep the code readable and maintainable for beginners, adhering to "do not over-engineer."
