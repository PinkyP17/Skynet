package controller;

import data.ReservationDao;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import models.*;
import util.RestClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable {

    // Simple View Fields
    @FXML private TextField amountField;
    @FXML private TextField currencyField;
    @FXML private TextField promoCodeField;
    @FXML private ListView<String> paymentListView;
    @FXML private Label statusLabel;
    @FXML private Label historyStatusLabel;

    // Complex View Fields (Booking Flow)
    @FXML private HBox parent;
    @FXML private VBox creditCardList;
    @FXML private Button btnBack;
    @FXML private Label lblDepCity;
    @FXML private Label lblArrCity;
    @FXML private Label lblDepAirport;
    @FXML private Label lblArrAirport;
    @FXML private Label lblDepDate;
    @FXML private Label lblArrDate;
    @FXML private Label lblDepTime;
    @FXML private Label lblArrTime;
    @FXML private Label lblAirline;
    @FXML private Label lblClassType;
    @FXML private Label lblClassPrice;
    @FXML private Spinner<Integer> spinnerLuggage;
    @FXML private Label lblLuggagesPrice;
    @FXML private Spinner<Integer> spinnerWeight;
    @FXML private Label lblWeightPrice;
    @FXML private Label lblTotalPrice;
    @FXML private Button btnPay;

    private RestClient restClient;
    private Flight flight;
    private Seat seat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restClient = RestClient.getInstance();
        
        // Initialize Simple View if active
        if (paymentListView != null) {
            refreshPaymentHistory();
        }

        // Initialize Complex View if active
        if (spinnerLuggage != null) {
             SpinnerValueFactory<Integer> luggageFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
             spinnerLuggage.setValueFactory(luggageFactory);
             spinnerLuggage.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
        }
        if (spinnerWeight != null) {
             SpinnerValueFactory<Integer> weightFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
             spinnerWeight.setValueFactory(weightFactory);
             spinnerWeight.valueProperty().addListener((obs, oldVal, newVal) -> updateTotalPrice());
        }
    }

    public void setData(Flight flight, Seat seat) {
        this.flight = flight;
        this.seat = seat;
        
        // Populate Simple View
        if (amountField != null && flight != null && seat != null) {
            double price = getSeatPrice(flight, seat);
            amountField.setText(String.valueOf(price));
            currencyField.setText("USD"); 
            statusLabel.setText("Booking for " + flight.getDepAirport().getIATA() + " -> " + flight.getArrAirport().getIATA());
        }

        // Populate Complex View
        if (lblDepCity != null && flight != null && seat != null) {
            populateComplexView();
        }
    }

    private double getSeatPrice(Flight flight, Seat seat) {
        if (seat == null || seat.getType() == null) return 0.0;
        String type = seat.getType().toLowerCase();
        if (type.contains("economy")) return flight.getEconomyPrice();
        if (type.contains("business")) return flight.getBusinessPrice();
        if (type.contains("first")) return flight.getFirstPrice();
        return 0.0;
    }

    private void populateComplexView() {
        System.out.println("PaymentPageController: Populating Complex View");
        lblDepCity.setText(flight.getDepAirport().getCity());
        lblArrCity.setText(flight.getArrAirport().getCity());
        lblDepAirport.setText(flight.getDepAirport().getName());
        lblArrAirport.setText(flight.getArrAirport().getName());
        lblDepDate.setText(flight.getDepDatetime().toLocalDate().toString());
        lblArrDate.setText(flight.getArrDatetime().toLocalDate().toString());
        lblDepTime.setText(flight.getDepDatetime().toLocalTime().toString());
        lblArrTime.setText(flight.getArrDatetime().toLocalTime().toString());
        lblAirline.setText(flight.getAirline().getName());
        lblClassType.setText(seat.getType() + " Class");
        
        double basePrice = getSeatPrice(flight, seat);
        lblClassPrice.setText(basePrice + "$");
        
        // Add Mock Cards to avoid empty list confusion
        if (creditCardList != null && creditCardList.getChildren().isEmpty()) {
            CheckBox card1 = new CheckBox("**** **** **** 1234 (Visa)");
            card1.setSelected(true);
            CheckBox card2 = new CheckBox("**** **** **** 5678 (MasterCard)");
            creditCardList.getChildren().addAll(card1, card2);
        }
        
        updateTotalPrice();
    }

    private void updateTotalPrice() {
         if (lblTotalPrice == null) return;
         
         double basePrice = getSeatPrice(flight, seat);
         int luggages = spinnerLuggage.getValue();
         int weight = spinnerWeight.getValue();
         
         // Mock pricing for extras
         double luggageCost = luggages * 50.0;
         double weightCost = weight * 10.0;
         
         lblLuggagesPrice.setText(luggageCost + "$");
         lblWeightPrice.setText(weightCost + "$");
         
         double total = basePrice + luggageCost + weightCost;
         lblTotalPrice.setText(total + "$");
    }

    // --- Complex View Actions (Booking Flow) ---

    @FXML
    void payNow(ActionEvent event) {
        try {
            double total = Double.parseDouble(lblTotalPrice.getText().replace("$", ""));
            
            // 1. Process Payment (Mock)
            java.util.Map<String, Object> request = new java.util.HashMap<>();
            request.put("amount", total);
            request.put("currency", "USD");
            request.put("method", "CREDIT_CARD");
            request.put("bookingId", 0L); // New booking

            Map response = restClient.post(RestClient.PAYMENT_SERVICE_URL, "/process", request, Map.class);
            
            if (response != null && "COMPLETED".equals(response.get("status"))) {
                // 2. Create Reservation
                Reservation reservation = new Reservation(
                    flight, 
                    Account.getCurrentUser().getId(), 
                    seat, 
                    spinnerLuggage.getValue(), 
                    (double) spinnerWeight.getValue()
                );
                
                ReservationDao reservationDao = new ReservationDao();
                reservationDao.create(reservation);
                
                // Show Success Alert
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Booking Successful");
                alert.setHeaderText(null);
                alert.setContentText("Payment Processed Successfully!\nBooking ID: " + response.get("id"));
                alert.showAndWait();
                
                // 3. Navigate to Ticket Page
                navigateToTicketPage(reservation);
                
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Payment Failed. Please try again.");
                alert.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error processing booking: " + e.getMessage());
            alert.show();
        }
    }
    
    private void navigateToTicketPage(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchPage/TicketPage.fxml"));
            Parent page = loader.load();
            VBox.setVgrow(page, Priority.ALWAYS);
            
            TicketPageController controller = loader.getController();
            controller.setData(reservation);
            
            StackPane content = (StackPane) parent.getScene().lookup("#content");
            content.getChildren().clear();
            content.getChildren().add(page);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML 
    void goBack(ActionEvent event) {
        ApplicationController.navBarController.popPage();
        StackPane content = (StackPane) parent.getScene().lookup("#content");
        content.getChildren().remove(content.getChildren().size() - 1);
    }
    
    @FXML 
    void addCrad(ActionEvent event) {
        // Placeholder
    }

    // --- Simple View Actions (Standalone Tab) ---

    public void refreshPaymentHistory() {
        paymentListView.getItems().clear();
        try {
            List<Map> history = restClient.get(RestClient.PAYMENT_SERVICE_URL, "/history", List.class);
            if (history != null) {
                for (Map payment : history) {
                    paymentListView.getItems().add(
                        "ID: " + payment.get("id") + " | " + payment.get("amount") + " " + payment.get("currency") + " | " + payment.get("status")
                    );
                }
                if (historyStatusLabel != null) historyStatusLabel.setText("History loaded.");
            } else {
                if (historyStatusLabel != null) historyStatusLabel.setText("No history found.");
            }
        } catch (Exception e) {
            if (historyStatusLabel != null) historyStatusLabel.setText("Failed to load history.");
            e.printStackTrace();
        }
    }

    @FXML
    void handleProcessPayment(ActionEvent event) {
        System.out.println("Processing Payment Clicked");
        try {
            double amount = Double.parseDouble(amountField.getText());
            String currency = currencyField.getText();

            java.util.Map<String, Object> request = new java.util.HashMap<>();
            request.put("amount", amount);
            request.put("currency", currency);
            request.put("method", "CREDIT_CARD");
            request.put("bookingId", 999L); // Manual test ID

            Map response = restClient.post(
                RestClient.PAYMENT_SERVICE_URL,
                "/process",
                request,
                Map.class
            );

            if (response != null) {
                System.out.println("Payment Processed: " + response);
                statusLabel.setText("Payment Successful! ID: " + response.get("id"));
                refreshPaymentHistory(); // Refresh list
                
                // Show Success Alert
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                alert.setTitle("Payment Successful");
                alert.setHeaderText(null);
                alert.setContentText("Payment has been successfully processed!\nTransaction ID: " + response.get("id"));
                alert.showAndWait();
            } else {
                System.out.println("Payment Failed (Response null)");
                statusLabel.setText("Payment Failed.");
            }

        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid amount.");
        } catch (Exception e) {
            System.out.println("Error processing payment: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    void handleGenerateInvoice(ActionEvent event) {
        System.out.println("Generate Invoice Clicked");
        String selected = paymentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No payment selected");
            if (historyStatusLabel != null) historyStatusLabel.setText("Select a payment first.");
            return;
        }
        
        try {
            String idStr = selected.split("\\|")[0].replace("ID:", "").trim();
            Long id = Long.parseLong(idStr);
            System.out.println("Requesting Invoice PDF for ID: " + id);
            
            // Note: RestClient.get returns the object directly. 
            // If the response is binary, we map it to byte[].class
            byte[] pdfBytes = restClient.get(
                RestClient.PAYMENT_SERVICE_URL,
                "/invoice/" + id,
                byte[].class
            );
            
            if (pdfBytes != null && pdfBytes.length > 0) {
                System.out.println("Invoice PDF received. Size: " + pdfBytes.length);
                
                // Save to temp file
                java.io.File tempFile = java.io.File.createTempFile("invoice_" + id + "_", ".pdf");
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile)) {
                    fos.write(pdfBytes);
                }
                
                System.out.println("Saved to: " + tempFile.getAbsolutePath());
                if (historyStatusLabel != null) historyStatusLabel.setText("Opened Invoice PDF.");
                
                // Open file
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(tempFile);
                }
                
            } else {
                System.out.println("Invoice API returned null or empty");
                if (historyStatusLabel != null) historyStatusLabel.setText("Invoice not found.");
            }
        } catch (Exception e) {
            System.out.println("Exception in Generate Invoice: " + e.getMessage());
            e.printStackTrace();
            if (historyStatusLabel != null) historyStatusLabel.setText("Error generating invoice.");
        }
    }

    @FXML
    void handleRefund(ActionEvent event) {
        System.out.println("Refund Clicked");
        String selected = paymentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.out.println("No payment selected");
            if (historyStatusLabel != null) historyStatusLabel.setText("Select a payment first.");
            return;
        }

        try {
            String idStr = selected.split("\\|")[0].replace("ID:", "").trim();
            Long id = Long.parseLong(idStr);
            System.out.println("Requesting Refund for ID: " + id);
             
             restClient.post(
                 RestClient.PAYMENT_SERVICE_URL,
                 "/refund/" + id,
                 null,
                 String.class
             );
             
             if (historyStatusLabel != null) historyStatusLabel.setText("Refund processed.");
             refreshPaymentHistory(); // Refresh list to show REFUNDED status
             
        } catch (Exception e) {
             System.out.println("Exception in Refund: " + e.getMessage());
             e.printStackTrace();
             if (historyStatusLabel != null) historyStatusLabel.setText("Refund request sent.");
        }
    }
    
    @FXML
    void handleApplyPromo(ActionEvent event) {
        System.out.println("Apply Promo Clicked");
        try {
            double amount = Double.parseDouble(amountField.getText());
            String code = promoCodeField.getText();

            java.util.Map<String, Object> request = new java.util.HashMap<>();
            request.put("amount", amount);
            request.put("code", code);

            Map response = restClient.post(
                RestClient.PAYMENT_SERVICE_URL,
                "/promo/apply",
                request,
                Map.class
            );

            if (response != null) {
                Double newAmount = (Double) response.get("discountedAmount");
                amountField.setText(String.valueOf(newAmount));
                statusLabel.setText("Promo applied! Old: " + response.get("originalAmount"));
            }

        } catch (Exception e) {
            System.out.println("Exception in Promo: " + e.getMessage());
            statusLabel.setText("Promo failed.");
        }
    }
}
