package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import view.Palette;

public class Main extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            /* * TOGGLE FOR TESTING:
             * Change 'showDashboardDirectly' to true to skip the sign-in 
             * and go straight to your new Admin UI.
             */
            boolean showDashboardDirectly = true; 

            if (showDashboardDirectly) {
                openAdminDashboard();
            } else {
                openSigninScreen(primaryStage);
            }

        } catch (Exception e) {
            System.err.println("CRITICAL STARTUP ERROR:");
            e.printStackTrace();
        }
    }

    /**
     * Logic to open the newly implemented Admin Dashboard
     */
    private void openAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            // Apply Palette/Theme logic to the dashboard too
            try {
                Palette.setDefaultPalette(Palette.LightPalette);
                Palette.getDefaultPalette().usePalette(scene);
            } catch (Exception e) {
                System.out.println("Warning: Palette failed for Dashboard.");
            }

            stage.setScene(scene);
            stage.setTitle("Skynet Admin Dashboard");
            
            // Add the Skynet Icon to the new window
            try {
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/SkynetLogo.png")));
            } catch (Exception e) {}

            stage.show();
            System.out.println("✅ Admin Dashboard Launched successfully.");
        } catch (Exception e) {
            System.err.println("Error loading Admin Dashboard:");
            e.printStackTrace();
        }
    }

    /**
     * Original logic to open the Sign-in Screen
     */
    private void openSigninScreen(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Signin.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        try {
            Palette.setDefaultPalette(Palette.LightPalette);
            Palette.getDefaultPalette().usePalette(scene);
        } catch (Exception e) {
            System.out.println("Warning: Palette failed to load.");
        }

        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/SkynetLogo.png")));
        } catch (Exception e) {}

        primaryStage.show();
    }
}