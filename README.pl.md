[English](README.md) | [Українська](README.uk.md)

# Reserve It

Aplikacja internetowa do zarządzania rezerwacjami stolików w restauracjach.

## Opis
Aplikacja ma trzy role użytkowników:
- **Klient**: Może tworzyć, edytować i anulować swoje rezerwacje.
- **Menedżer**: może zarządzać restauracjami i rezerwacjami.
- **Administrator**: może zarządzać użytkownikami.

## Technologie
- **Java**
- **Spring Boot** (MVC, JPA, Security)
- **SLF4J + Logback** (logowanie)
- **Thymeleaf** (renderowanie po stronie serwera)
- **MySQL** (baza danych)
- **i18n** (internacjonalizacja: Angielski, Ukraiński, Polski)

## Architektura
Trójpoziomowa monolityczna architektura serwera:
- **Poziom repozytorium**: Interakcja z bazą danych przy użyciu JPA.
- **Poziom usługi**: Logika biznesowa.
- **Poziom kontrolera**: Przetwarza żądania i odpowiedzi HTTP klientów.

## Instalacja
### 0. Wymagania wstępne
- Java 17+ zainstalowana
- MySQL zainstalowany
- Maven zainstalowany

### 1. Sklonuj repozytorium
```
git clone https://github.com/MaksymChernenko04/reserve-it
cd reserveit
```

### 2. Utwórz bazę danych MySQL:
```
CREATE DATABASE reserveit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Podaj dane dostępowe do bazy danych
#### Opcja 1: Jeśli korzystasz z IDE takiego jak IntelliJ
- Otwórz jako projekt Maven
- Upewnij się, że zmienne środowiskowe są ustawione w Run → Edit Configurations
  - DB_URL=adres_twojej_bazy_danych
  - DB_USERNAME=użytkownik_twojej_bazy_danych
  - DB_PASSWORD=hasło_użytkownika_twojej_bazy_danych

#### Opcja 2: Korzystanie ze zmiennych środowiskowych
#### Windows (PowerShell):
```
$env:DB_URL="adres_twojej_bazy_danych"
$env:DB_USERNAME="użytkownik_twojej_bazy_danych"
$env:DB_PASSWORD="hasło_użytkownika_twojej_bazy_danych"
```

#### Windows (CMD)
```
set DB_URL=adres_twojej_bazy_danych
set DB_USERNAME=użytkownik_twojej_bazy_danych
set DB_PASSWORD=hasło_użytkownika_twojej_bazy_danych
```

#### Linux / macOS (bash/zsh)
```
export DB_URL="adres_twojej_bazy_danych"
export DB_USERNAME="użytkownik_twojej_bazy_danych"
export DB_PASSWORD="hasło_użytkownika_twojej_bazy_danych"
```

#### Linux / macOS (fish shell)
```
set -x DB_URL "adres_twojej_bazy_danych"
set -x DB_USERNAME "użytkownik_twojej_bazy_danych"
set -x DB_PASSWORD "hasło_użytkownika_twojej_bazy_danych"
```

#### Opcja 3: Zaktualizuj plik application.properties
```
spring.datasource.url=adres_twojej_bazy_danych
spring.datasource.username=użytkownik_twojej_bazy_danych
spring.datasource.password=hasło_użytkownika_twojej_bazy_danych
```

\* Przykład dla adres_twojej_bazy_danych: jdbc:mysql://localhost:3306/reserveit

## Uruchomienie i wstępna konfiguracja

Przed rozpoczęciem korzystania z aplikacji należy ręcznie utworzyć role i administratora do zarządzania innymi użytkownikami. Kroki 1-4 należy wykonać tylko raz, przed pierwszym uruchomieniem.

### 1. Utwórz tabele ról i użytkowników w bazie danych:
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

### 2. Wstaw role:
```
INSERT INTO role (name) VALUES ('ROLE_ADMIN'), ('ROLE_MANAGER'), ('ROLE_CLIENT');
```

### 3. Zaszyfruj hasło do swojego konta administratora za pomocą Bcrypt.

Możesz użyć generatora online, takiego jak https://bcrypt-generator.com/

### 4. Wstaw administratora:
```
INSERT INTO user (active, email, password, role_id)
VALUES (true, 'your@admin.email', 'yourBcryptPassword', 1); -- 1 = ROLE_ADMIN id
```

### 5. Uruchom aplikację
#### Jeśli korzystasz z IDE:
Uruchom metodę main w klasie ReserveItApplication

#### Korzystając z Maven:
```
mvn spring-boot:run
```

Po uruchomieniu aplikacja będzie dostępna pod adresem http://localhost:8080
