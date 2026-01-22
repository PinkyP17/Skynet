package data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.Objects;

public class DataSource {
    private static Connection connection;

    private DataSource() {
        System.out.println(">>> DEBUG [DB]: DataSource Constructor Called");
        try {
            // 1. Determine the path (C:\Users\ilsat\.Skynet)
            String userHome = System.getProperty("user.home");
            Path folderPath = Path.of(userHome, ".Skynet");
            Path dataBasePath = folderPath.resolve("applicationDataBase.db");
            
            System.out.println(">>> DEBUG [DB]: Target Path: " + dataBasePath.toAbsolutePath());
            
            // 2. FIX: Create the directory if it is missing!
            if (!Files.exists(folderPath)) {
                System.out.println(">>> DEBUG [DB]: Creating missing folder: " + folderPath);
                Files.createDirectories(folderPath); // <--- THIS LINE FIXES THE CRASH
            }

            // 3. Extract DB if missing
            String resourceName = "/dataBase/applicationDataBase.db";
            if (!Files.exists(dataBasePath)) {
                System.out.println(">>> DEBUG [DB]: Extracting database from resources...");
                try (InputStream inputStream = getClass().getResourceAsStream(resourceName)) {
                    if (inputStream == null) throw new IOException("DB Resource not found!");
                    Files.copy(inputStream, dataBasePath);
                }
            } else {
                System.out.println(">>> DEBUG [DB]: Database file already exists.");
            }

            // 4. Connect
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataBasePath.toAbsolutePath());
            System.out.println(">>> DEBUG [DB]: Connection Established!");

        } catch (Exception e) {
            System.err.println(">>> DEBUG [DB CRASH]: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
           new DataSource();
        }
        return connection;
    }
}