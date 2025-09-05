# Reserve It

A web application for managing restaurant reservations.

## Description
There are three user roles in the application:
- **Client**: Can create, edit and cancel reservations.
- **Manager**: Can manage restaurants and reservations.
- **Admin**: Can manage users.

## Technologies
- **Java**
- **Spring Boot** (MVC, JPA, Security)
- **SLF4J + Logback** (logging)
- **Thymeleaf** (server-side rendering)
- **MySQL**
- **i18n** (internationalization: English, Ukrainian, Polish)

## Architecture
Three-tier monolith server architecture:
- **Repository layer**: Communication with a DB using JPA.
- **Service layer**: Business logic.
- **Controller layer**: Handles client HTTP requests and responses

## Installation
### 0. Prerequisites
- Java 17+ installed
- MySQL installed
- Maven installed

### 1. Clone the repository
```
git clone https://github.com/MaksymChernenko04/reserve-it
cd reserveit
```

### 2. Create a MySQL database:
```
CREATE DATABASE reserveit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Provide your database credentials
#### Option 1: If you use an IDE like IntelliJ
- Open the project as a Maven project
- Ensure environment variables are set in Run → Edit Configurations
  - DB_URL=your_db_url
  - DB_USERNAME=your_db_username
  - DB_PASSWORD=your_db_password

#### Option 2: Using environment variables
#### Windows (PowerShell):
```
$env:DB_URL="your_db_url"
$env:DB_USERNAME="your_db_username"
$env:DB_PASSWORD="your_db_password"
```

#### Windows (CMD)
```
set DB_URL=your_db_url
set DB_USERNAME=your_db_username
set DB_PASSWORD=your_db_password
```

#### Linux / macOS (bash/zsh)
```
export DB_URL="your_db_url"
export DB_USERNAME="your_db_username"
export DB_PASSWORD="your_db_password"
```

#### Linux / macOS (fish shell)
```
set -x DB_URL "your_db_url"
set -x DB_USERNAME "your_db_username"
set -x DB_PASSWORD "your_db_password"
```

#### Option 3: Update application.properties file
```
spring.datasource.url=your_db_url
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
```

\* Example of your_db_url: jdbc:mysql://localhost:3306/reserveit

## Running and Initial Setup

Before using the application, you need to manually create the initial roles and an admin, so you can manage other users. You only need to go through steps 1–4 once, before the first run.

### 1. Create role and user tables in your DB:
```
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`email`),
  FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

### 2. Insert roles:
```
INSERT INTO role (name) VALUES ('ROLE_ADMIN'), ('ROLE_MANAGER'), ('ROLE_CLIENT');
```

### 3. Encrypt password for your admin account using Bcrypt. 

You can use an online generator like https://bcrypt-generator.com/

### 4. Insert the admin:
```
INSERT INTO user (active, email, password, role_id)
VALUES (true, 'your@admin.email', 'yourBcryptPassword', 1); -- 1 = ROLE_ADMIN id
```

### 5. Run the Application
#### If you use IDE:
Run ReserveItApplication class main method

#### Using Maven:
```
mvn spring-boot:run
```

After running, the application will be available at http://localhost:8080
