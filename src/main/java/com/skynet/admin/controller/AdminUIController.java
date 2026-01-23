package com.skynet.admin.controller;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class AdminUIController {

    @FXML private TextArea reportArea;
    
    // Set a timeout to prevent the UI from hanging
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
            
    private final String BASE_URL = "http://localhost:8083/api/admin";

    @FXML
    public void handleGenerateReport() {
        sendRequest("/reports/sales");
    }

    @FXML
    public void handleViewLogs() {
        sendRequest("/logs");
    }

    @FXML
    public void handleRefreshPassengers() {
        sendRequest("/passengers");
    }

    @FXML
    public void handleViewFeedback() {
        sendRequest("/feedback/category/General");
    }

    private void sendRequest(String endpoint) {
        try {
            reportArea.setText("Connecting to Skynet Services...");
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Accept", "application/json")
                .GET()
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.body() == null || response.body().isEmpty()) {
                reportArea.setText("Error: Server returned an empty response.");
            } else if (response.statusCode() != 200) {
                reportArea.setText("Server Error (" + response.statusCode() + "): " + response.body());
            } else {
                reportArea.setText(response.body());
            }
            
        } catch (ConnectException e) {
            reportArea.setText("API Error: Cannot connect to Backend. \nMake sure AdminService is running on port 8083.");
        } catch (Exception e) {
            reportArea.setText("API Error: " + (e.getMessage() != null ? e.getMessage() : "Unknown Connection Issue"));
            e.printStackTrace();
        }
    }
}