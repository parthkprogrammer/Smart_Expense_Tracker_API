# Smart Expense Tracker API

A production-quality, clean, and beginner-friendly Java Spring Boot 3 REST API for tracking expenses, built using Java 17 and Maven.

---

## Technical Stack
- **Language**: Java 17
- **Framework**: Spring Boot 3.3.0 (Spring Web, Validation)
- **Build Tool**: Maven 3.x
- **Database**: Local JSON File Database (`src/main/resources/expenses.json`)
- **Testing**: JUnit 5, Mockito, Spring Boot Test (MockMvc)

---

## Features
- **Add Expense**: Create new expense entries with title, amount, category, and date.
- **View All Expenses**: Retrieve all recorded expenses.
- **Filter by Category**: Search and retrieve expenses belonging to a specific category (case-insensitive).
- **Calculate Total**:
  - Overall sum of all expenses.
  - Category-specific sum of expenses (case-insensitive).
- **Delete Expense**: Delete expense entries by ID (returns proper status codes or custom errors).
- **Bonus Feature - Date Range Filter**: Retrieve expenses filtered by an optional inclusive start date and end date.

---

## API Endpoints Reference

| HTTP Method | Endpoint | Query Parameters | Description | Status Codes |
| :--- | :--- | :--- | :--- | :--- |
| **POST** | `/expenses` | None | Create a new expense. | `201 Created`, `400 Bad Request` |
| **GET** | `/expenses` | `category`, `startDate`, `endDate` | Retrieve all expenses (supports category & date filtering). | `200 OK` |
| **GET** | `/expenses/total` | None | Get the overall sum of all expenses. | `200 OK` |
| **GET** | `/expenses/total/{category}` | None (category in path) | Get the total sum for a specific category. | `200 OK` |
| **DELETE** | `/expenses/{id}` | None (UUID in path) | Delete an expense by ID. | `204 No Content`, `404 Not Found` |

---

## Installation & Running Instructions

### Prerequisite
Ensure you have **Java 17 (or newer)** and **Maven** installed and configured in your system path.

### 1. Install & Build
To clean the project and compile it from source, run the following command in the project root:
```bash
mvn clean install
```

### 2. Run the Application
To run the Spring Boot dev server locally:
```bash
mvn spring-boot:run
```
The server will start on [http://localhost:8080](http://localhost:8080).

### 3. Run the Test Suite
To execute all JUnit 5 unit and integration tests (30 tests):
```bash
mvn test
```

---

## Swagger OpenAPI Documentation
Once the application is running, you can access the interactive API docs at:
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON Docs**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

