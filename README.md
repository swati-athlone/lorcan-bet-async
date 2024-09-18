# Inventory and Order Management System

## Overview

This project is an Inventory and Order Management System built using Spring Boot 3.2.9, Java 17, and PostgreSQL. It supports asynchronous order processing, concurrency handling, and resiliency in distributed systems. The project is designed with the following key features:

- **Product Management**: Add, update, and delete products.
- **Inventory Management**: Track product inventory and handle concurrent updates.
- **Order Processing**: Process orders asynchronously and handle payment service failures.
- **Distributed Consistency**: Ensures eventual consistency across inventory and payment.
- **Resiliency**: Includes retry mechanisms for failed orders and eventual consistency.

---

## Features

- **Product Management**: CRUD operations for products.
- **Inventory Management**: Manage stock levels with concurrency control.
- **Asynchronous Order Processing**: Orders are processed asynchronously, checking stock levels and integrating payment service calls.
- **Retry Mechanism**: Orders that fail due to insufficient stock or payment failures are retried automatically with delay.
- **Concurrency Handling**: Prevents race conditions with inventory updates.
- **Payment Service Simulation**: Simulates an external payment service with delays, failures, and retries.
- **Order Logs**: Logs are maintained for each order to track processing history.

---

## Table of Contents

- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [API Endpoints](#api-endpoints)
- [Concurrency and Distributed System Design](#concurrency-and-distributed-system-design)
- [Future Improvements](#future-improvements)
- [Conclusion](#conclusion)

---

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.9**
- **PostgreSQL** (as the primary database)
- **Hibernate** (for ORM and transaction management)
- **Lombok** (for simplifying Java code)
- **Maven** (for dependency management and build automation)

---

## Prerequisites

1. **Java 17**

   Ensure that Java 17 is installed on your machine. If not, you can download and install it from the official [Oracle website](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html).

2. **PostgreSQL Installation**

   To install PostgreSQL locally on a Mac, follow these steps:

   **Using Homebrew (Recommended)**

    1. Install Homebrew (if not installed):
       ```bash
       /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
       ```

    2. Install PostgreSQL:
       ```bash
       brew install postgresql
       ```

    3. Start PostgreSQL service:
       ```bash
       brew services start postgresql
       ```

    4. Create a new PostgreSQL user and database:
       ```bash
       psql postgres
       CREATE USER your_username WITH PASSWORD 'your_password';
       CREATE DATABASE your_database;
       GRANT ALL PRIVILEGES ON DATABASE your_database TO your_username;
       ```

    5. Update application properties in the Spring Boot project:
       ```yaml
       spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
       spring.datasource.username=your_username
       spring.datasource.password=your_password
       ```

---

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/swati-athlone/lorcan-bet-async.git
   cd lorcan-bet-async

Configure PostgreSQL: Ensure PostgreSQL is running and update the application.properties or application.yml file with your database configuration:

2. modify application.properties

Copy code
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

3. Build the project:

mvn clean install

4. Run the application:

mvn spring-boot:run

or you can go to the target folder and run as 

java -jar <<name of the jar generated>>

## Project Structure

src
├── main
│   ├── java
│   │   └── com.lorcan.bet.asyn
│   │       ├── controller   # Rest controllers for API endpoints
│   │       ├── dto          # Data Transfer Objects for product, order, inventory
│   │       ├── entity       # JPA entities for Product, Order, Inventory
│   │       ├── repository   # Spring Data JPA repositories
│   │       ├── service      # Service layer for business logic
│   │       ├── util         # Utilities for conversions and status enums
│   └── resources
│       ├── application.properties  # Application configuration


## How to Run

Ensure PostgreSQL is running and configured correctly.

Run the application using Maven:

mvn spring-boot:run


### Access the API at:

http://localhost:8080

The API specification can be accessed using Swagger at:

http://localhost:8080/swagger-ui.html

## API Endpoints

### Product Management
1. Create a Product:

    POST /app/v1/product
    
    Request Body:

   {
   "name": "Automatic Fan",
   "description": "Automatic Fan for Home",
   "price": 15000
   }


2. Update a Product:
    
    PUT /app/v1/product/update/{id}
    
    Request Body:

{
"description": "Table Fan for Home and Office",
"price": 1890
}


3. Delete a Product:

    DELETE /app/v1/product/remove/{id}

4. Get All Products:

    GET /app/v1/product/get


### Order Management
1. Create and Process an Order Asynchronously:

    POST /app/v1/order/process

    Request Body:

   {
   "quantity": 22,
   "product": {
   "id": 2
   }
   }

## Inventory Syncing API

The system runs a background job that periodically syncs inventory levels with an external warehouse system. The external warehouse system provides an API to fetch current stock levels.

API exposed at: http://localhost:8083/api/inventory


## Concurrency and Distributed System Design

### Concurrency Handling

#### Inventory Locking: 
Inventory updates are handled using database-level locking to ensure data integrity when multiple orders are processed at the same time. The repository uses @Lock to prevent race conditions.

#### Idempotent Order Processing: 
Orders are processed only once, even if retries are triggered due to failure. This ensures the same order isn’t deducted multiple times.

#### Asynchronous Processing
Asynchronous Order and Payment Handling: Orders are processed asynchronously, ensuring the system remains responsive while waiting for external services like payment gateways.
Retry Mechanism: If an order fails due to payment failure or insufficient inventory, the system retries the order processing after a delay.

### Future Improvements

#### External Inventory Syncing: 
Integrate with an external warehouse to sync inventory levels periodically.

#### Payment Gateway Integration: 
Replace the simulated payment service with real-world payment APIs.

#### Notifications: 
Implement email or SMS notifications for customers after order processing.

#### Testing: 
Add more comprehensive unit and integration tests.


## Conclusion
This project demonstrates a robust inventory and order management system, handling concurrency and distributed consistency challenges. By incorporating asynchronous processing and retry mechanisms, it ensures smooth operations even in failure scenarios. """
