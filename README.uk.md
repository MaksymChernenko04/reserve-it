[English](README.md) | [Polski](README.pl.md)

# Reserve It

Веб додаток для керування резерваціями столиків у ресторанах.

## Опис
Додаток має три ролі користувачів:
- **Клієнт**: Може створювати, редагувати та скасовувати свої резервації.
- **Менеджер**: Може керувати ресторанами та резерваціями.
- **Адмін**: Може керувати користувачами.

## Технології
- **Java**
- **Spring Boot** (MVC, JPA, Security)
- **SLF4J + Logback** (логування)
- **Thymeleaf** (рендеринг на боці серверу)
- **MySQL** (БД)
- **i18n** (інтернаціоналізація: Англійська, Українська, Польська)

## Архітектура
Трирівнева монолітна архітектура серверу:
- **Рівень Репозиторію**: Взаємодія із БД з використанням JPA.
- **Рівень Сервісу**: Бізнес логіка.
- **Рівень Контролера**: Обробляє клієнтські HTTP запити та відповіді.

## Встановлення
### 0. Передумови
- Java 17+ встановлено
- MySQL встановлено
- Maven встановлено

### 1. Клонувати репозиторій
```
git clone https://github.com/MaksymChernenko04/reserve-it
cd reserveit
```

### 2. Створити базу даних MySQL:
```
CREATE DATABASE reserveit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Надати дані для доступу до бази даних
#### Варіант 1: Якщо ви використовуєте IDE накшталт IntelliJ
- Відкрити як Maven проєкт
- Впевнитися, що змінні оточення встановлено в Run → Edit Configurations
  - DB_URL=адреса_вашої_бд
  - DB_USERNAME=користувач_вашої_бд
  - DB_PASSWORD=пароль_користувача_вашої_бд

#### Варіант 2: Використовуючи змінні оточення
#### Windows (PowerShell):
```
$env:DB_URL="адреса_вашої_бд"
$env:DB_USERNAME="користувач_вашої_бд"
$env:DB_PASSWORD="пароль_користувача_вашої_бд"
```

#### Windows (CMD)
```
set DB_URL=адреса_вашої_бд
set DB_USERNAME=користувач_вашої_бд
set DB_PASSWORD=пароль_користувача_вашої_бд
```

#### Linux / macOS (bash/zsh)
```
export DB_URL="адреса_вашої_бд"
export DB_USERNAME="користувач_вашої_бд"
export DB_PASSWORD="пароль_користувача_вашої_бд"
```

#### Linux / macOS (fish shell)
```
set -x DB_URL "адреса_вашої_бд"
set -x DB_USERNAME "користувач_вашої_бд"
set -x DB_PASSWORD "пароль_користувача_вашої_бд"
```

#### Варіант 3: Оновити файл application.properties
```
spring.datasource.url=адреса_вашої_бд
spring.datasource.username=користувач_вашої_бд
spring.datasource.password=пароль_користувача_вашої_бд
```

\* Приклад для адреса_вашої_бд: jdbc:mysql://localhost:3306/reserveit

## Запуск та початкові налаштування

Перед використанням застосунку, ви маєте створити ролі та адміна вручну для керування іншими користувачами. Вам потрібно пройти кроки 1-4 лише раз, перед першим запуском.

### 1. Створити таблиці ролі та користувача у БД:
```
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role_name` (`name`)
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
  UNIQUE KEY `UK_user_email` (`email`),
  FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```

### 2. Вставити ролі:
```
INSERT INTO role (name) VALUES ('ROLE_ADMIN'), ('ROLE_MANAGER'), ('ROLE_CLIENT');
```

### 3. Зашифрувати пароль для вашого акаунту адміна з використанням Bcrypt.

Ви можете використати онлайн генератор накшталт https://bcrypt-generator.com/

### 4. Вставити адміна:
```
INSERT INTO user (active, email, password, role_id)
VALUES (true, 'your@admin.email', 'yourBcryptPassword', 1); -- 1 = ROLE_ADMIN id
```

### 5. Запустити додаток
#### Якщо ви використовуєте IDE:
Запустіть метод main у класі ReserveItApplication

#### Використовуючи Maven:
```
mvn spring-boot:run
```

Після запуску додаток буде доступний за адресою http://localhost:8080
