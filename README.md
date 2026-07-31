# Smart Expense Tracker API

A REST API for managing personal expenses, built with **Java 17**, **Spring Boot 3**, and **Maven** as part of the **Diligent Software Engineering Apprenticeship 2026** assignment.

## Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot 3.3.0
* **Build Tool:** Maven
* **Data Storage:** Local JSON file (`src/main/resources/expenses.json`)
* **Testing:** JUnit 5, Mockito, Spring Boot Test (MockMvc)
* **API Documentation (Bonus):** Swagger/OpenAPI

---

## Features

* Add a new expense
* View all expenses
* Filter expenses by category (case-insensitive)
* Calculate the total of all expenses
* Calculate the total for a specific category
* Delete an expense by ID

### Bonus Feature

* Filter expenses by an optional date range (`startDate` and `endDate`)

---

## Project Structure

```text
smart-expense-tracker-api/
│── README.md
│── AI_NOTES.md
│── pom.xml
│
├── src/
│   ├── main/
│   └── test/
│
└── tests/
```

---

## API Endpoints

| Method | Endpoint                     | Description                                                                         |
| ------ | ---------------------------- | ----------------------------------------------------------------------------------- |
| POST   | `/expenses`                  | Add a new expense                                                                   |
| GET    | `/expenses`                  | Get all expenses (supports optional `category`, `startDate`, and `endDate` filters) |
| GET    | `/expenses/total`            | Get the total of all expenses                                                       |
| GET    | `/expenses/total/{category}` | Get the total for a specific category                                               |
| DELETE | `/expenses/{id}`             | Delete an expense by its ID                                                         |

---

## Example Request

### Create Expense

**POST** `/expenses`

```json
{
  "title": "Lunch",
  "amount": 250,
  "category": "Food",
  "date": "2026-07-31"
}
```

### Example Response

```json
{
  "id": "4d6b8b2e-3f9d-4f3d-8d8a-9d58d1f0f9f2",
  "title": "Lunch",
  "amount": 250,
  "category": "Food",
  "date": "2026-07-31"
}
```

---

## Prerequisites

Before running the project, ensure you have:

* Java 17 or later
* Maven 3.x

---

## Installation

Clone the repository:

```bash
git clone <repository-url>
cd smart-expense-tracker-api
```

Install dependencies and build the project:

```bash
mvn clean install
```

---

## Running the Application

Start the application with:

```bash
mvn spring-boot:run
```

The application will run at:

```
http://localhost:8080
```

---

## Running the Tests

Execute the complete test suite:

```bash
mvn test
```

---

## Swagger Documentation (Bonus)

Once the application is running, open:

* Swagger UI:
  `http://localhost:8080/swagger-ui/index.html`

* OpenAPI Specification:
  `http://localhost:8080/v3/api-docs`

---

## Error Handling

The API returns consistent JSON error responses containing:

* `timestamp`
* `status`
* `message`

Validation errors return appropriate HTTP status codes with descriptive messages.

---

## Notes

* Data is stored in a local JSON file; no database is used.
* The project follows a layered architecture with separate controller, service, repository, model, and exception packages.
* The code is intentionally kept simple, readable, and easy to understand while following standard Spring Boot development practices.
