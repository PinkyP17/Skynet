# Gemini Instructions - Skynet Flight Booking System

This file provides context and instructions for Gemini to maintain consistency and quality in the Skynet project.

## Project Overview
Skynet is a JavaFX-based desktop application for flight booking, allowing passengers to book flights and airlines to manage them.

## Tech Stack
- **Language:** Java 17
- **UI Framework:** JavaFX 18
- **Build Tool:** Maven
- **Database:** SQLite (via JDBC)
- **UI Libraries:** 
  - [Ikonli](https://github.com/kordamp/ikonli) for Material Design and Payment icons.
  - [ControlsFX](https://controlsfx.github.io/) for advanced UI controls.

## Architecture: MVC Pattern
- **Model:** Located in `src/main/java/models/`. POJOs representing database entities.
- **View:** 
  - FXML files in `src/main/resources/view/`.
  - CSS files in `src/main/resources/style/`.
  - Palette/Theme logic in `src/main/java/view/`.
- **Controller:** Located in `src/main/java/controller/`. Handles UI events and connects Model with View.
- **Data Access:** DAO pattern used in `src/main/java/data/` for database interactions.

## Coding Standards & Preferences
- **Conventions:** Follow standard Java naming conventions (PascalCase for classes, camelCase for methods/variables).
- **FXML/CSS:** Keep styling separate from layout. Use CSS for all visual styling.
- **Icons:** Always use Ikonli font icons instead of image-based icons where possible.
- **Database:** Use `DataSource.java` for obtaining connections. Use PreparedStatements to prevent SQL injection.
- **Theme Support:** Maintain both Light and Dark mode compatibility. Check `TextLightVersion.css` and `TextDarkVersion.css`.

## Common Commands
- **Build:** `mvn clean install`
- **Run:** `mvn javafx:run`
- **Package:** `mvn package` (creates a shaded JAR)

## Key Files
- `pom.xml`: Project dependencies and build configuration.
- `src/main/java/module-info.java`: Java module system configuration.
- `src/main/resources/dataBase/applicationDataBase.db`: The SQLite database.
