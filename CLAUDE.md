# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Java 17 Spring Boot console application called "Literatura" that allows users to search for books using the Gutendx API (https://gutendx.com/). The application stores book and author data in a PostgreSQL database and provides an interactive command-line menu interface.

## Development Commands

### Database Setup (Required First)
```bash
# Start PostgreSQL database using Docker Compose
docker-compose up -d

# Stop database
docker-compose down
```

### Build and Run
```bash
# Build the project using Maven wrapper (Windows)
mvnw.cmd clean compile

# Build the project using Maven wrapper (Unix/Linux/macOS)
./mvnw clean compile

# Run the application (Windows)
mvnw.cmd spring-boot:run

# Run the application (Unix/Linux/macOS)
./mvnw spring-boot:run

# Run tests (Windows)
mvnw.cmd test

# Run tests (Unix/Linux/macOS)
./mvnw test

# Package the application (Windows)
mvnw.cmd package

# Package the application (Unix/Linux/macOS)
./mvnw package
```

### Database Configuration
- Database: PostgreSQL 15.3
- Default credentials: usuario/172922
- Database name: literatura-db
- Port: 5432
- Connection configured in `src/main/resources/application.properties`

## Architecture

### Package Structure
- `com.library.literatura` - Root package
  - `menu/` - Interactive console interface components
  - `models/` - JPA entities and data transfer objects
  - `repository/` - Spring Data JPA repositories
  - `service/` - Business logic and external API integration

### Core Components

**Main Application (`LiteraturaApplication.java`)**: Spring Boot CommandLineRunner that initializes the Menu component and starts the interactive console interface.

**Menu System (`menu/Menu.java`)**: Interactive console interface that provides:
1. Search books by title
2. List registered books  
3. List registered authors
4. List authors alive in specific year
5. List books by language

**Data Models**:
- `Book`: JPA entity representing books with title, author, language, and download count
- `Person`: JPA entity representing authors with name and birth/death years
- `BookData`, `PersonData`, `Response`: Record classes for API response mapping

**Services**:
- `APIFetcher`: HTTP client for Gutendx API integration
- `DataConversor`: JSON to object conversion using Jackson
- `IDataConversor`: Interface for data conversion service

**Repositories**:
- `BookRepository`: JPA repository with custom queries for book filtering by title and language
- `PersonRepository`: JPA repository with custom queries for author filtering by year

### Key Workflows

**Book Search**: Checks local database first using `findByTitleContainsIgnoreCase()`, then queries Gutendx API if not found. Automatically creates Person entities for new authors and prevents duplicate book entries.

**Data Persistence**: Uses Spring Data JPA with PostgreSQL. Authors are shared across books (many-to-one relationship). DDL auto-update is enabled.

### External Dependencies
- Gutendx API (https://gutendx.com/) for book data
- PostgreSQL for persistence
- Jackson (version 2.16.0) for JSON processing
- Spring Boot 3.3.1 with JPA and DevTools

### Application Flow
The application runs as a console interface where users select menu options through numbered inputs. All data is persisted to PostgreSQL and the application prevents duplicate entries while automatically fetching new data from the external API when needed. The database schema is automatically created/updated on startup.

## Code Optimizations (Latest Updates)

### Refactoring Summary
The codebase has been significantly refactored and optimized with the following improvements:

### Service Layer Improvements
- **BookService**: Extracted business logic from Menu class, added comprehensive error handling and input validation
- **PersonService**: Centralized author-related operations with proper validation
- **APIFetcher**: Enhanced with timeout configuration, proper HTTP status handling, and retry logic
- **DataConversor**: Improved JSON parsing with better error messages and configuration

### Data Model Enhancements
- **Book Entity**: Added database indexes, validation methods, equals/hashCode, and improved constructors
- **Person Entity**: Added `isAliveInYear()` method, validation, and helper methods for book management
- **Database Indexes**: Added performance indexes on commonly queried fields (title, language, name, birth/death years)

### Menu System Refactoring
- **Separation of Concerns**: Menu class now focuses only on UI, business logic moved to services
- **Input Validation**: Created MenuValidator class for robust user input handling
- **Error Handling**: Comprehensive exception handling with user-friendly error messages
- **Spring Integration**: Full dependency injection with @Service and @Component annotations

### Repository Improvements
- **Enhanced Queries**: Improved JPQL queries with proper null handling for author filtering
- **Additional Methods**: Added queries for living authors and ordered results

### Testing
- Added unit tests for core functionality (PersonTest, BookServiceTest)
- Tests verify validation logic, business rules, and error handling
- All tests pass successfully

### Key Benefits
1. **Better Performance**: Database indexes and optimized queries
2. **Improved Maintainability**: Clear separation of concerns and dependency injection
3. **Enhanced Reliability**: Comprehensive error handling and input validation
4. **Better User Experience**: Improved error messages and input validation
5. **Code Quality**: Added proper equals/hashCode, validation methods, and unit tests