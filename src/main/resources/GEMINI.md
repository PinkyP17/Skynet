# Gemini Resources Instructions - Skynet

This file provides specific instructions for handling resources in the Skynet project.

## UI/UX Guidelines
- **FXML:** All FXML files should be placed in `src/main/resources/view/` or its subdirectories.
- **CSS:** All CSS files should be placed in `src/main/resources/style/`.
- **Images:** Static images should be in `src/main/resources/images/`.
- **Database:** The SQLite database is located in `src/main/resources/dataBase/`.

## Styling
- Use `Application.css` for global styles.
- Use component-specific CSS (e.g., `FlightCard.css`) for specific views.
- Ensure all new UI components support both light and dark themes by referencing the appropriate theme CSS files.
