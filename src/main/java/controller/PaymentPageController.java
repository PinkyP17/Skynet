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
import org.controlsfx.control.Notifications; 
import javafx.geometry.Pos;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable {

    // Simple View Fields
    @FXML private TextField amountField;
    @FXML private ComboBox<String> currencyComboBox; 
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
    
    // Booking Flow State
    private boolean isBookingFlow = false;
    private int luggageCount = 0;
    private double luggageWeight = 0.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("PaymentPageController: Initializing...");
        try {
            restClient = RestClient.getInstance();
        
        // Initialize Simple View if active
        if (paymentListView != null) {
            refreshPaymentHistory();
        }
        
        if (currencyComboBox != null) {
            currencyComboBox.getItems().addAll("USD", "EUR", "GBP", "JPY");
            currencyComboBox.getSelectionModel().selectFirst();
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
        System.out.println("PaymentPageController: Initialization Complete.");
        } catch (Exception e) {
            System.out.println("PaymentPageController: Error in initialize!");
            e.printStackTrace();
        }
    }

    public void setData(Flight flight, Seat seat) {
        System.out.println("PaymentPageController: setData called with flight=" + flight);
        try {
        this.flight = flight;
        this.seat = seat;
        
        // Populate Simple View
        if (amountField != null && flight != null && seat != null) {
            double price = getSeatPrice(flight, seat);
            amountField.setText(String.format("%.2f", price)); 
            if (currencyComboBox != null) currencyComboBox.setValue("USD"); 
            statusLabel.setText("Booking: " + flight.getDepAirport().getIATA() + " -> " + flight.getArrAirport().getIATA());
        }

        // Populate Complex View
        if (lblDepCity != null && flight != null && seat != null) {
            populateComplexView();
        }
        } catch (Exception e) {
            System.out.println("PaymentPageController: Error in setData!");
            e.printStackTrace();
        }
    }
    
    public void setBookingData(Flight flight, Seat seat, double totalAmount, int luggageCount, double luggageWeight) {
        this.flight = flight;
        this.seat = seat;
        this.luggageCount = luggageCount;
        this.luggageWeight = luggageWeight;
        this.isBookingFlow = true;

        if (amountField != null) {
            amountField.setText(String.format("%.2f", totalAmount));
        }
        if (statusLabel != null) {
            statusLabel.setText("Booking for " + flight.getDepAirport().getIATA() + " -> " + flight.getArrAirport().getIATA());
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
        // This is called from the Booking Summary Page (Complex View)
        try {
            double total = Double.parseDouble(lblTotalPrice.getText().replace("$", ""));
            
            // Navigate to Payment Page (Premium View)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PaymentPage.fxml"));
            Parent page = loader.load();
            VBox.setVgrow(page, Priority.ALWAYS);
            
            PaymentPageController controller = loader.getController();
            controller.setBookingData(flight, seat, total, spinnerLuggage.getValue(), (double) spinnerWeight.getValue());
            
            ApplicationController.navBarController.pushPage(page);
            
            StackPane content = (StackPane) parent.getScene().lookup("#content");
            content.getChildren().add(page);

        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error navigating to payment: " + e.getMessage());
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
            
            StackPane content = null;
             if (parent != null) {
                 content = (StackPane) parent.getScene().lookup("#content");
             } else if (amountField != null) {
                 content = (StackPane) amountField.getScene().lookup("#content");
             }
             
             if (content != null) {
                 content.getChildren().clear();
                 content.getChildren().add(page);
             }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML 
    void goBack(ActionEvent event) {
        ApplicationController.navBarController.popPage();
        StackPane content = null;
         if (parent != null) {
             content = (StackPane) parent.getScene().lookup("#content");
         } else if (amountField != null) {
             content = (StackPane) amountField.getScene().lookup("#content");
         }
         
         if (content != null) {
             content.getChildren().remove(content.getChildren().size() - 1);
         }
    }
    
    @FXML 
    void addCrad(ActionEvent event) { }

    // --- Simple View Actions (Standalone Tab) ---

    public void refreshPaymentHistory() {
        paymentListView.getItems().clear();
        try {
            List<Map> history = restClient.get(RestClient.PAYMENT_SERVICE_URL, "/history", List.class);
            if (history != null) {
                // Sort by ID Descending (Latest First)
                history.sort((m1, m2) -> {
                    try {
                        Long id1 = Long.parseLong(String.valueOf(m1.get("id")));
                        Long id2 = Long.parseLong(String.valueOf(m2.get("id")));
                        return id2.compareTo(id1);
                    } catch (Exception e) {
                        return 0;
                    }
                });

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
        // This is called from the Payment Page (Simple View)
        if (statusLabel != null) {
            statusLabel.setText(""); 
            statusLabel.getStyleClass().removeAll("success-text", "error-text");
        }

        try {
            double amount = 0.0;
            String currency = "USD";

            if (lblTotalPrice != null && !lblTotalPrice.getText().isEmpty()) {
                 amount = Double.parseDouble(lblTotalPrice.getText().replace("$", ""));
            } else if (amountField != null) {
                 if (amountField.getText().isEmpty()) {
                    showNotification("Input Error", "Please enter an amount.", true);
                    amountField.getStyleClass().add("error-border");
                    return;
                 }
                 amount = Double.parseDouble(amountField.getText());
                 if (currencyComboBox != null) currency = currencyComboBox.getValue();
            }

            java.util.Map<String, Object> request = new java.util.HashMap<>();
            request.put("amount", amount);
            request.put("currency", currency);
            request.put("method", "CREDIT_CARD");
            request.put("bookingId", 999L); 

            Map response = restClient.post(
                RestClient.PAYMENT_SERVICE_URL,
                "/process",
                request,
                Map.class
            );

            if (response != null) {
                if (statusLabel != null) {
                    statusLabel.setText("Payment Successful! ID: " + response.get("id"));
                    statusLabel.getStyleClass().add("success-text");
                }
                if (paymentListView != null) refreshPaymentHistory(); 
                
                showNotification("Payment Successful", "Transaction ID: " + response.get("id"), false);
                
                // If in Booking Flow, Create Reservation and Redirect
                if (isBookingFlow && flight != null) {
                     Reservation reservation = new Reservation(
                        flight, 
                        Account.getCurrentUser().getId(), 
                        seat, 
                        luggageCount, 
                        luggageWeight
                    );
                    
                    ReservationDao reservationDao = new ReservationDao();
                    reservationDao.create(reservation);
                    
                    // Show booking success alert before redirect
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle("Booking Successful");
                    alert.setHeaderText(null);
                    alert.setContentText("Reservation Confirmed!\nBooking ID: " + response.get("id"));
                    alert.showAndWait();
                    
                    navigateToTicketPage(reservation);
                }

            } else {
                if (statusLabel != null) {
                    statusLabel.setText("Payment Failed.");
                    statusLabel.getStyleClass().add("error-text");
                }
                showNotification("Payment Failed", "The server returned no response.", true);
            }

        } catch (NumberFormatException e) {
            if (statusLabel != null) statusLabel.setText("Invalid amount format.");
            if (amountField != null) amountField.getStyleClass().add("error-border");
            showNotification("Input Error", "Please enter a valid number.", true);
        } catch (Exception e) {
            e.printStackTrace();
            if (statusLabel != null) statusLabel.setText("Error: " + e.getMessage());
             showNotification("System Error", e.getMessage(), true);
        }
    }

    private void showNotification(String title, String text, boolean isError) {
        Notifications notification = Notifications.create()
                .title(title)
                .text(text)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT);
        
        if (isError) {
            notification.showError();
        } else {
            notification.showInformation();
        }
    }

    @FXML
    void handleGenerateInvoice(ActionEvent event) {
        String selected = paymentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showNotification("Selection Required", "Please select a payment to generate invoice.", true);
            return;
        }
        
        try {
            String idStr = selected.split("\\|")[0].replace("ID:", "").trim();
            Long id = Long.parseLong(idStr);
            
            showNotification("Generating Invoice", "Please wait...", false);

            byte[] pdfBytes = restClient.get(
                RestClient.PAYMENT_SERVICE_URL,
                "/invoice/" + id,
                byte[].class
            );
            
            if (pdfBytes != null && pdfBytes.length > 0) {
                java.io.File tempFile = java.io.File.createTempFile("invoice_" + id + "_", ".pdf");
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile)) {
                    fos.write(pdfBytes);
                }
                
                showNotification("Invoice Ready", "Opening PDF...", false);
                
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(tempFile);
                }
                
            } else {
                showNotification("Invoice Error", "Could not retrieve invoice data.", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Error", "Failed to generate invoice: " + e.getMessage(), true);
        }
    }

    @FXML
    void handleRefund(ActionEvent event) {
        String selected = paymentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showNotification("Selection Required", "Please select a payment to refund.", true);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Refund");
        confirm.setHeaderText("Are you sure you want to refund this payment?");
        confirm.setContentText(selected);
        
        if (confirm.showAndWait().get() != ButtonType.OK) {
            return;
        }

        try {
            String idStr = selected.split("\\|")[0].replace("ID:", "").trim();
            Long id = Long.parseLong(idStr);
             
             restClient.post(
                 RestClient.PAYMENT_SERVICE_URL,
                 "/refund/" + id,
                 null,
                 String.class
             );
             
             showNotification("Refund Processed", "Refund request sent successfully.", false);
             refreshPaymentHistory(); 
             
        } catch (Exception e) {
             e.printStackTrace();
             showNotification("Refund Error", "Failed to process refund: " + e.getMessage(), true);
        }
    }
    
    @FXML
    void handleApplyPromo(ActionEvent event) {
        String code = promoCodeField.getText();
        if (code == null || code.trim().isEmpty()) {
             promoCodeField.getStyleClass().add("error-border");
             showNotification("Input Error", "Please enter a promo code.", true);
             return;
        }
        promoCodeField.getStyleClass().remove("error-border");

        try {
            double amount = Double.parseDouble(amountField.getText());

            java.util.Map<String, Object> request = new java.util.HashMap<>();
            request.put("amount", amount);
            request.put("code", code);

            Map response = restClient.post(
                RestClient.PAYMENT_SERVICE_URL,
                "/promo/apply",
                request,
                Map.class
            );

            if (response != null && response.containsKey("discountedAmount")) {
                Double newAmount = (Double) response.get("discountedAmount");
                amountField.setText(String.format("%.2f", newAmount));
                if (statusLabel != null) {
                    statusLabel.setText("Promo applied! Old: " + response.get("originalAmount"));
                    statusLabel.getStyleClass().add("success-text");
                }
                showNotification("Success", "Promo code applied!", false);
            } else {
                 promoCodeField.getStyleClass().add("error-border");
                 showNotification("Invalid Code", "Promo code invalid or expired.", true);
            }

        } catch (Exception e) {
            if (statusLabel != null) statusLabel.setText("Promo failed.");
            showNotification("Error", "Could not apply promo.", true);
        }
    }
}
