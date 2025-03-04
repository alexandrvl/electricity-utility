# Electricity Utility System

## Project Structure

```
electricity-utility/
├── src/
│   └── main/
│       ├── kotlin/
│       │   └── com/electricity/utility/
│       │       ├── domain/          # Domain model classes
│       │       ├── infrastructure/  # Technical implementations
│       │       ├── application/     # Application services
│       │       └── interfaces/      # API interfaces
│       └── resources/
│           └── config/             # Configuration files
├── test/
│   ├── spec/
│   │   └── com/electricity/utility/
│   │       └── steps/             # Cucumber step definitions
│   └── resources/
│       ├── features/             # Cucumber feature files
│       ├── cucumber.properties   # Cucumber configuration
│       └── logback-test.xml     # Test logging configuration
└── build.gradle.kts             # Gradle build configuration
```

## Development Setup

### Prerequisites
- JDK 17 or later
- Kotlin 1.8.20 or later
- Gradle 7.x or later

### Build and Test
```bash
# Build the project
./gradlew build

# Run tests
./gradlew test

# Generate documentation
./gradlew dokkaHtml
```

### Test Reports
After running tests, reports can be found at:
- Cucumber HTML Report: `build/reports/cucumber/report.html`
- Cucumber JSON Report: `build/reports/cucumber/report.json`
- JUnit XML Report: `build/reports/cucumber/junit.xml`
- Test Logs: `build/test.log`

## Project Architecture

### Domain-Driven Design
The project follows DDD principles with the following key areas:
1. Authentication
2. Billing
3. Grid Monitoring
4. Consumption Monitoring
5. Data Security
6. Online Payment
7. Role Based Access
8. Service Request
9. Usage Analytics

### Testing Strategy
- BDD with Cucumber
- Unit tests with JUnit 5
- Integration tests
- Parallel test execution enabled

### Security
- Role-based access control
- Data encryption
- Audit logging
- JWT authentication

## Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use meaningful names
- Write comprehensive KDoc comments
- Keep methods focused and small

### Testing
- Write tests first (TDD)
- Cover edge cases
- Use meaningful scenario descriptions
- Keep scenarios focused and atomic

### Documentation
- Update README.md for significant changes
- Maintain up-to-date KDoc
- Document architectural decisions
- Keep feature files clear and well-organized