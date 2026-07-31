# AI Generation and Implementation Notes

This project was developed with the assistance of an AI coding tool. I used AI as a development assistant to speed up implementation, but I reviewed, tested, and modified the generated code before including it in the final project.

## 1. What the AI Assisted With

The AI helped generate the initial project structure and boilerplate code, including:

* Maven project setup using Java 17 and Spring Boot 3
* Basic package structure (`controller`, `service`, `repository`, `model`, `exception`, and `config`)
* Initial `Expense` model with validation annotations
* Initial implementations of the controller, service, and repository classes
* Global exception handling structure
* Initial unit and integration test templates
* Swagger/OpenAPI configuration

## 2. What I Reviewed, Tested, and Changed

I reviewed all generated code and made several improvements before finalizing the project.

### Business Logic

* Verified that all required REST endpoints worked correctly.
* Improved validation to ensure invalid expenses cannot be added.
* Tested expense addition, deletion, filtering, and total calculations.

### JSON Persistence

* Verified that expenses are correctly loaded from and saved to the local JSON file.
* Tested persistence after adding and deleting expenses.

### Error Handling

* Standardized API error responses to include:

  * `timestamp`
  * `status`
  * `message`
* Improved validation error messages to make them more meaningful.

### Testing

* Updated and corrected unit and integration tests.
* Fixed mock configurations after service method changes.
* Added tests for validation failures and filtering functionality.

### Bonus Feature

* Added Swagger/OpenAPI documentation so the API can be explored through the browser.

## 3. AI Suggestions I Chose Not to Use

### Lombok

I did not use Lombok because the project requirements specified using plain Java classes with explicit constructors, getters, and setters.

### Database Integration

I rejected suggestions to use H2, JPA, or any relational database because the assignment explicitly allowed using in-memory storage or a local JSON file.

### Spring Security

I chose not to include authentication since it was outside the scope of the assignment and would have added unnecessary complexity.

### Over-Engineered Design

I avoided introducing DTOs, MapStruct, CQRS, or other advanced architectural patterns. For a project of this size, keeping the code simple and easy to understand was a better fit for the assignment requirements.

## 4. Final Verification

Before submission, I:

* Built the project using Maven.
* Ran the complete test suite.
* Verified each REST endpoint using Swagger/Postman.
* Confirmed that the JSON file persisted data correctly.
* Reviewed the code for readability and consistency.
* Ensured the project structure matched the submission requirements.
