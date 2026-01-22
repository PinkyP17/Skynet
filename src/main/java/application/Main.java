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
            // 1. Load the View
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Signin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // 2. SAFETY NET: Try to load the Palette (Theme)
            // If this fails (common error), we catch it so the window still opens.
            try {
                Palette.setDefaultPalette(Palette.LightPalette);
                Palette.getDefaultPalette().usePalette(scene);
            } catch (Exception e) {
                System.out.println("Warning: Palette failed to load. Continuing anyway...");
            }

            // 3. Setup Window
            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.setScene(scene);
            
            // 4. SAFETY NET: Try to load Icon
            try {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/SkynetLogo.png")));
            } catch (Exception e) {
                System.out.println("Warning: Icon missing. Continuing...");
            }

            primaryStage.show();

        } catch (Exception e) {
            // This catches major FXML errors and prints them so you can see them
            System.err.println("CRITICAL STARTUP ERROR:");
            e.printStackTrace();
        }
    }
}