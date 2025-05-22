
# RRA Vehicle Tracking Management System

This is a Spring Boot backend application for Rwanda Revenue Authority (RRA) to track vehicle ownership history and manage vehicle transfers.

## Features

- User Authentication and Authorization with JWT
- Vehicle Owner Management
- Plate Number Management
- Vehicle Registration
- Vehicle Transfer Management
- Vehicle Ownership History Tracking

## Technical Stack

- Java 17
- Spring Boot 3.1.5
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL Database
- Swagger/OpenAPI Documentation
- Thymeleaf for Email Templates

## Getting Started

### Prerequisites

- JDK 17 or higher
- PostgreSQL 12 or higher
- Maven 3.6.x or higher

### Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE rra_db;
```

2. Make sure database connection details in `application.properties` match your environment:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rra_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Build and run the application:

```bash
mvn clean install
mvn spring-boot:run
```

4. The application will be available at `http://localhost:8080`
5. Swagger API documentation will be available at `http://localhost:8080/swagger-ui.html`

## API Documentation

The API documentation is generated using Swagger/OpenAPI and can be accessed at:
```
http://localhost:8080/swagger-ui.html
```

## Security

- JWT Authentication is implemented for securing the APIs
- Roles: ROLE_ADMIN, ROLE_STANDARD
- Most endpoints require ROLE_ADMIN privileges
- User accounts must be verified via email before login

## Email Configuration

Email functionality is configured to send:
- Account verification emails
- Password reset emails
- Success notification emails

Update the email configuration in `application.properties` if needed.
