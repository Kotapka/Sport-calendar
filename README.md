# SportHub - Sports Event Calendar

A full-stack web application designed to manage and display sports events. This project was developed as part of the Sportradar Coding Academy exercise.

## 🚀 Project Overview

SportHub allows users to browse a schedule of sports events, manage team databases, and add new matches with real-time validation. It features a responsive UI and a Spring Boot backend.

### Key Features
- **Event Management**: View, add, edit, and delete sports events.
- **Club Database**: Manage registered teams.
- **Client-Side Sorting**: Sort events by date (newest/oldest) or status.
- **Validation**: Business logic to prevent impossible match configurations (e.g., a team playing against itself).
- **RESTful API**: Clean separation between frontend and backend.

## 🛠️ Tech Stack

- **Backend**: Java 21, Spring Boot 3.4.x, Spring Data JPA.
- **Database**: PostgreSQL.
- **Frontend**: HTML5, JavaScript (ES6+), Tailwind CSS 4.0.
- **Testing**: JUnit 5, Mockito.
- **API Documentation**: SpringDoc OpenAPI (Swagger UI).

## 📊 Database Design (ERD)

The database follows the **Third Normal Form (3NF)** to ensure data integrity and reduce redundancy.

- **Teams**: Stores official names, abbreviations, and country codes.
- **Events**: Stores match details, linking to home and away teams via foreign keys (`_home_team_id`, `_away_team_id`).
- **Stages/Sports**: (Optional entities) Categorize events by competition level or sport type.

> **Note on Naming**: Following the requirements, all foreign keys in the database schema are prefixed with an underscore (e.g., `_home_team_id`).

## ⚙️ Setup and Installation

### Prerequisites
- JDK 21
- Maven 3.x
- PostgreSQL (running on port 5432)

### 1. Clone the Repository
### 2. Setup Database
- The application requires a PostgreSQL database to store data.
1. Create database sportradar
2. Open application.properties:
- spring.datasource.url=jdbc:postgresql://localhost:5432/sportradar
- spring.datasource.username=YOUR_USERNAME_HERE
- spring.datasource.password=YOUR_PASSWORD_HERE
- spring.jpa.hibernate.ddl-auto=create-drop
- spring.sql.init.mode=always
- spring.jpa.defer-datasource-initialization=true

### 3. Launch Backend
1. Open your terminal in the backend folder.
2. Run the appropriate command:
   - Windows: mvnw.cmd spring-boot:run
   - Linux / macOS: chmod +x mvnw && ./mvnw spring-boot:run
3. Success Check: Look for Started SportradarApplication in the logs. The API will be live at http://localhost:8080

### 4. Launch Frontend
1. Navigate to the frontend folder.
2. Open index.html in any modern web browser.

### Optional Swagger
1. Swagger will be on http://localhost:8080/swagger-ui/index.html#/


## 🧠 Development Decisions & Assumptions

During the development of this project, several strategic decisions were made to balance the requirements with future scalability:

### ⚙️ Backend & Architecture
- **Scalable Database Schema**: I designed a more comprehensive database schema that took time. This was done to ensure the system is "future-proof" and ready for complex features like player statistics, venue management, and detailed match events (cards, goals).
- **Clean Code vs. Comments**: My philosophy is that **well-structured code is self-documenting**. I focused on meaningful naming conventions for classes and methods to minimize the need for excessive comments.
- **Robust Foundations**: The backend was built with a focus on **Separation of Concerns**. By using DTOs and clear Service/Repository layers, the API is easy to extend with new features (e.g., advanced filtering or user authentication).

### 🎨 Frontend & Tools
- **AI-Assisted Development**: As a backend-focused developer, I leveraged **AI collaboration tools** to build a modern, responsive frontend. This allowed me to deliver a UI while focusing my primary energy on the complex business logic and database integrity of the backend.
- **Vanilla JS Efficiency**: I chose Vanilla JavaScript over heavy frameworks to keep the frontend lightweight and ensure it works "out of the box" without a complex build pipeline.

### 🛠️ Room for Improvement (Future Scope)
- **Security**: Due to the time constraints of this exercise, advanced security measures (like JWT or OAuth2) were not implemented. In a production environment, adding a robust security layer and comprehensive input sanitization would be the next priority.
- **Testing**: I implemented core tests to verify critical logic. Given more time, I would expand the test to have higher code coverage.

---