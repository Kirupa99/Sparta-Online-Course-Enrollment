# Sparta Academy REST API

## Project Overview

This project is a production-ready REST API built using Spring Boot for Sparta Global Academy.

The API manages:

* Trainers
* Trainees
* Courses

It supports CRUD operations, trainee enrolment, trainer-course assignments, and database persistence using MySQL.

---

# Technologies Used

* Java
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Maven
* Swagger / OpenAPI
* JUnit
* Mockito

---

# Database Design

## Tables

### Trainers

| Column         | Type         |
|----------------|--------------|
| trainer_id     | INT (PK)     |
| first_name     | VARCHAR      |
| last_name      | VARCHAR      |
| email          | VARCHAR      |
| specialisation | VARCHAR      |
| phone_number   | VARCHAR      |
| courses        | VARCHAR (FK) |

---

### Trainees

| Column        | Type     |
|---------------| -------- |
| trainee_id    | INT (PK) |
| first_name    | VARCHAR  |
| last_name     | VARCHAR  |
| email         | VARCHAR  |
| city          | VARCHAR  |
| course_id     | INT (FK) |

---

### Courses

| Column             | Type     |
|--------------------|----------|
| course_id          | INT (PK) |
| course_name        | VARCHAR  |
| course_description | VARCHAR  |
| start_date         | DATE     |
| end_date           | DATE     |
| max_students       | INT      |
| trainer_id         | INT (FK) |
| trainee_id         | INT (FK) |

---

# Relationships

- One Trainer can teach many Courses (ManyToMany via course_trainer join table)
- One Course can have many Trainees (OneToMany)
- One Trainee belongs to one Course (ManyToOne)

---

# Project Structure

```text
src/main/java/com/sparta/spartaacademy
│
├── controllers
├── dto
├── entities
├── repositories
├── services
└── SpartaAcademyApplication.java
```

---

# Setup Instructions

## 1. Clone Repository

```bash
git clone https://github.com/AishwaryaGitay/sparta-academy.git
```

---

## 2. Configure MySQL


Make sure MySQL is running on your machine.

The database will be created automatically by Spring Boot using the `createDatabaseIfNotExist=true` setting in `application.properties`.

## 3. Configure application.properties

```properties
spring.application.name=SpartaAcademy

spring.datasource.url=jdbc:mysql://localhost:3306/sparta-academy?createDatabaseIfNotExist=true
spring.datasource.username= yourusername
spring.datasource.password= yourpassword
server.port=8081

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## 4. Run Application

Run:

```bash
mvn spring-boot:run
```

or run `SpartaAcademyApplication.java` directly from IntelliJ.

---

# Swagger Documentation

Swagger UI:

```text
http://localhost:8091/swagger-ui.html
```

OpenAPI Docs:

```text
http://localhost:8091/v3/api-docs
```

---

# Testing

The project includes:

* Unit testing using JUnit
* Service layer testing using Mockito

Run tests using:

```bash
mvn test
```
Run a specific test class:

```bash
mvn test -Dtest=CourseServiceTest
```

---
