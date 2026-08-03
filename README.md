# Secure Employee Management System

A backend project built from scratch in Java to understand how modern backend systems work internally instead of relying directly on frameworks.

This project focuses on implementing the core building blocks manually such as HTTP handling, database operations, authentication, token generation, dynamic query building, and REST APIs.

---

## Project Goal

The objective of this project is to understand how backend frameworks like Spring Boot and Spring Security work internally by implementing every major component manually.

Instead of using ready-made authentication libraries or ORM frameworks, this project builds each layer step by step.

---

## Tech Stack

- Java
- Spring Boot
- JDBC
- MySQL
- Jackson ObjectMapper
- RSA KeyPairGenerator
- REST APIs
- Postman

---

## Features Implemented

### Employee CRUD APIs

- Create Employee
- Read Employee(s)
- Update Employee
- Delete Employee

---

### Dynamic Query Builder

A generic query builder capable of generating SQL queries dynamically.

Implemented queries:

- INSERT
- SELECT
- UPDATE
- DELETE

Instead of hardcoding SQL statements, queries are generated based on provided data structures.

---

### JDBC Layer

Implemented manually using

- DriverManager
- Connection
- Statement
- PreparedStatement
- ResultSet

without using any ORM framework.

---

### JSON Processing

Jackson ObjectMapper is used for

- Java Object → JSON
- JSON → Java Object

---

### Authentication

Basic Authentication has been implemented.

Flow:

```
Client

↓

Username + Password

↓

Authentication

↓

Valid ?

↓

YES

↓

Continue
```

---

### Token Management

A custom token manager has been created.

Current implementation:

- Token Creation
- Token Decoding
- Base64 Encoding
- Base64 Decoding

Token currently stores

- Username
- Password
- Public Key

---

### RSA Key Generation

Implemented using Java Security API.

Generates

- Public Key
- Private Key

using

- RSA
- 2048-bit Key Pair

---

### REST APIs

Implemented endpoints

```
GET    /employees

POST   /employees

PUT    /employees

DELETE /employees

POST   /login
```

---

## Project Architecture

```
                Client
                   │
                   ▼
          EmployeeController
                   │
      ┌────────────┼─────────────┐
      ▼            ▼             ▼
Authentication  TokenManager  QueryBuilder
      │                          │
      ▼                          ▼
KeyGeneratorUtil             DBHandler
      │                          │
      └──────────────┬───────────┘
                     ▼
                   MySQL
```

---

## Folder Responsibilities

### Authentication

Responsible for verifying user credentials.

---

### TokenManager

Responsible for

- Creating Tokens
- Decoding Tokens

---

### KeyGeneratorUtil

Responsible for generating RSA Public and Private Keys.

---

### QueryBuilder

Responsible for dynamically creating SQL queries.

---

### DBHandler

Responsible for database execution.

Handles

- INSERT
- UPDATE
- DELETE
- SELECT

---

### EmployeeController

Acts as the entry point for HTTP requests and coordinates all backend components.

---

## Learning Objectives

This project was built to understand

- REST APIs
- JDBC
- HTTP Request Flow
- Authentication
- Authorization
- Token Generation
- SQL Query Building
- JSON Processing
- Cryptography Basics
- Layered Backend Architecture

---

## Upcoming Features

- Token Validation
- Authorization using Public & Private Key Pair
- Expiry-based Access Tokens
- Role Based Access Control (RBAC)
- AI Integration using Ollama
- Natural Language to CRUD Operations
- Docker Support
- Prometheus & Grafana Monitoring

---

## Long Term Vision

Allow users to interact with the system using natural language.

Example:

```
Add a new employee named Devansh Pundir.
```

↓

AI (Ollama)

↓

Generate JSON

↓

Employee API

↓

Database

No manual JSON creation will be required from the user.

---

## Status

Project is currently under active development.
New features are being implemented incrementally while focusing on understanding the internal working of backend systems.
