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
        try {

            Path dataBasePath = Path.of(System.getProperty("user.home"), ".Skynet", "applicationDataBase.db");
            String resourceName= "/dataBase/applicationDataBase.db";

            if (!Files.exists(dataBasePath)) {
                System.out.println("INFO: Database not found, creating from template: " + dataBasePath);
                Files.createDirectories(dataBasePath.getParent());
                try(InputStream inputStream = getClass().getResourceAsStream(resourceName)) {
                    Objects.requireNonNull(inputStream, "Not found resource: " + resourceName);
                    Files.copy(inputStream, dataBasePath);
                    System.out.println("INFO: Database created successfully from template");
                }
            } else {
                System.out.println("INFO: Using existing database: " + dataBasePath);
            }

            String jdbcUrl = "jdbc:sqlite:" + dataBasePath.toAbsolutePath();
            System.out.println("INFO: Connecting to database: " + jdbcUrl);
            connection = DriverManager.getConnection(jdbcUrl);
            System.out.println("INFO: Database connection established successfully");

        } catch (SQLException | IOException e) {
            System.err.println("ERROR: Failed to initialize database connection: " + e.getMessage());
            e.printStackTrace();
            connection = null; // Ensure connection is null on error
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
           new DataSource();
        }
        return connection;
    }
}