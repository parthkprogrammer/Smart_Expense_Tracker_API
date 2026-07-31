# AI Generation and Implementation Notes

This project was developed with the assistance of AI tools. I used AI as a development assistant to speed up implementation, while reviewing, testing, and refining the generated code before including it in the final submission.

## 1. What the AI Assisted With

The AI helped generate the initial project structure and boilerplate code, including:

* Maven project setup using Java 17 and Spring Boot 3
* Initial package structure (`controller`, `service`, `repository`, `model`, `exception`, and `config`)
* Initial implementations of the model, repository, service, and controller
* Global exception handling
* Initial unit and integration test templates
* Swagger/OpenAPI configuration
* Documentation drafts for `README.md` and `AI_NOTES.md`

## 2. What I Reviewed, Tested, and Changed

After generating the initial implementation, I performed a final review of the entire project to improve code quality and readability without changing any functionality.

During this review, I:

* Removed unnecessary JavaDoc comments that only described obvious methods.
* Removed redundant inline comments that repeated what the code already expressed.
* Simplified the remaining comments so they only explain non-obvious logic.
* Improved variable names to make the code more descriptive and easier to understand.
* Simplified Swagger/OpenAPI annotations while preserving the endpoint documentation.
* Shortened and clarified exception messages to make API responses more user-friendly.
* Removed wording that sounded overly verbose or repetitive.
* Cleaned up formatting and unnecessary blank lines to improve consistency.
* Verified that these changes did not modify the API behavior, business logic, project structure, or test results.

After completing these improvements, I rebuilt the project, ran the full test suite, and manually verified the REST endpoints to ensure the application behaved exactly as before.

## 3. AI Suggestions I Chose Not to Use

### Lombok

I chose not to use Lombok because I wanted to keep the project in plain Java with explicit constructors, getters, and setters.

### Database Integration

I rejected suggestions to use H2, JPA, or any relational database because the assignment explicitly requested using in-memory storage or a local JSON file.

### Spring Security

I did not include authentication because it was outside the scope of the assignment.

### Over-Engineered Architecture

I chose not to introduce DTOs, MapStruct, CQRS, or other advanced architectural patterns since they were unnecessary for the assignment and would have increased complexity.

## 4. Final Verification

Before submission, I:

* Built the project using `mvn clean install`.
* Ran the complete test suite using `mvn test`.
* Verified all REST endpoints using Swagger and Postman.
* Confirmed JSON persistence worked correctly.
* Reviewed the code for readability and consistency.
* Ensured the repository structure matched the assignment requirements.
