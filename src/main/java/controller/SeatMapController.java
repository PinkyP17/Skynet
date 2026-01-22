package controller;

import data.ReservationDao;
import data.SeatDao;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Account;
import models.Flight;
import models.Seat;
import view.Palette;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SeatMapController implements Initializable {

    @FXML
    private Button btnBack;

    @FXML
    private Button btnCancel;

    @FXML
    private ToggleButton btnFavorite;

    @FXML
    private HBox confirmationWindow;

    @FXML
    private Label lblAirline;

    @FXML
    private Label lblArrAirport;

    @FXML
    private Label lblArrCity;

    @FXML
    private Label lblArrDate;

    @FXML
    private Label lblArrTime;

    @FXML
    private Label lblBusinessPrice;

    @FXML
    private Label lblDepAirport;

    @FXML
    private Label lblDepCity;

    @FXML
    private Label lblDepDate;

    @FXML
    private Label lblDepTime;

    @FXML
    private Label lblEconomyPrice;

    @FXML
    private Label lblFirstPrice;

    @FXML
    private Label lblFirstname;

    @FXML
    private Label lblFromCode;

    @FXML
    private Label lblSeat;

    @FXML
    private Label lblSelectedSeat;

    @FXML
    private Label lblToCode;

    @FXML
    private StackPane parent;
    @FXML
    private GridPane seatMap;
    
    @FXML
    private ChoiceBox<String> filterByType;
    
    @FXML
    private TextField seatSearchField;
    
    @FXML
    private Label lblAvailableFirst;
    
    @FXML
    private Label lblAvailableBusiness;
    
    @FXML
    private Label lblAvailableEconomy;
    
    @FXML
    private Label lblTotalSeats;
    
    @FXML
    private Label lblOccupiedSeats;

    private final ToggleGroup seatGroup = new ToggleGroup();
    private Flight flight;
    private Seat selectedSeat;
    private List<Seat> allSeats = new ArrayList<>();
    private List<Seat> displayedSeats = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parent.getStylesheets().add(getClass().getResource("/style/SeatMap.css").toExternalForm());

        btnCancel.setTooltip(new Tooltip("Cancel Reservation"));
        
        // Initialize filter dropdown
        if (filterByType != null) {
            filterByType.setItems(FXCollections.observableArrayList("All", "First", "Business", "Economy"));
            filterByType.setValue("All");
            filterByType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                applyFilters();
            });
        }
        
        // Initialize seat search
        if (seatSearchField != null) {
            seatSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                applyFilters();
            });
        }

        seatGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            //when a seat is selected set the confirmationWindow visibility to true, otherwise set it to false
            if (newValue != null) {
                confirmationWindow.setVisible(true);
                selectedSeat = (Seat) seatGroup.getSelectedToggle();
                lblSeat.setText(selectedSeat.getColumn() + selectedSeat.getRow());
            }
            else {
                confirmationWindow.setVisible(false);
                selectedSeat = null;
            }
        }));
    }

    public void setData(Flight flight) {
        this.flight = flight;

        fillSeatMap();
        updateAvailabilityStatistics();

        lblDepAirport.setText(flight.getDepAirport().getName());
        lblDepCity.setText(flight.getDepAirport().getCity() + " - " + flight.getDepAirport().getCountry());
        lblDepDate.setText(flight.getDepDatetime().toLocalDate().toString());
        lblDepTime.setText(flight.getDepDatetime().toLocalTime().toString());

        lblAirline.setText(flight.getAirline().getName());

        lblArrAirport.setText(flight.getArrAirport().getName());
        lblArrCity.setText(flight.getArrAirport().getCity() + " - " + flight.getArrAirport().getCountry());
        lblArrDate.setText(flight.getArrDatetime().toLocalDate().toString());
        lblArrTime.setText(flight.getArrDatetime().toLocalTime().toString());

        lblSelectedSeat.setText((selectedSeat == null) ? "" : selectedSeat.getColumn() + selectedSeat.getRow());

        lblFirstPrice.setText(flight.getFirstPriceFormatted());
        lblBusinessPrice.setText(flight.getBusinessPriceFormatted());
        lblEconomyPrice.setText(flight.getEconomyPriceFormatted());

        lblFirstname.setText(Account.getCurrentUser().getPassenger().getFirstname());
        Account.getCurrentUser().getPassenger().firstnameProperty().addListener((observable, oldValue, newValue) -> {
            lblFirstname.setText(newValue);
        });
        String depIATA = flight.getDepAirport().getIATA();
        String depICAO = flight.getDepAirport().getICAO();
        lblFromCode.setText((depIATA != null) ? depIATA : depICAO);
        String arrIATA = flight.getArrAirport().getIATA();
        String arrICAO = flight.getArrAirport().getICAO();
        lblToCode.setText((arrIATA != null) ? arrIATA : arrICAO);

        if(Account.getCurrentUser().hasReservation(flight)) {
            btnCancel.setVisible(true);
        }

        btnFavorite.setSelected(flight.isFavorite());
        if (flight.isFavorite()) {
            btnFavorite.setOnAction(event -> flight.removeFavorite());
        }
        else {
            btnFavorite.setOnAction(event -> flight.addFavorite());
        }

        flight.getFavoriteProperty().addListener((observable, oldValue, newValue) -> {
            btnFavorite.setSelected(newValue);
        });

        btnFavorite.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Platform.runLater(() -> btnFavorite.setOnAction(event -> flight.removeFavorite()));
            }
            else {
                Platform.runLater(() -> btnFavorite.setOnAction(event -> flight.addFavorite()));
            }
        });
    }

    @FXML
    private void cancelSelected() {
        seatGroup.selectToggle(null);
    }

    @FXML
    private void confirmSelected() {
        try {
            FXMLLoader paymentLoader = new FXMLLoader(getClass().getResource("/view/SearchPage/PaymentPage.fxml"));
            Parent page = paymentLoader.load();
            VBox.setVgrow(page, Priority.ALWAYS);

            PaymentPageController paymentController = paymentLoader.getController();
            paymentController.setData(flight, selectedSeat);

            ApplicationController.navBarController.pushPage(page);

            StackPane content = (StackPane) parent.getScene().lookup("#content");
            content.getChildren().add(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goBack(ActionEvent event) {
        StackPane content = (StackPane) parent.getScene().lookup("#content");
        int recentChild = content.getChildren().size() - 1;
        content.getChildren().remove(recentChild);
        ApplicationController.navBarController.popPage();
        FlightCardController cardController = (FlightCardController) parent.getUserData();
        cardController.changeActionButtons();
    }

    @FXML
    void cancelReservation(ActionEvent event) {
        parent.getScene().lookup("#overlay-layer").setDisable(false);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you really want to cancel your reservation ?");
        alert.setHeaderText("Confirm the action");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("YES");
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("NO");

        DialogPane dialogPane = alert.getDialogPane();

        Palette.getDefaultPalette().usePalette(dialogPane.getScene());
        dialogPane.getStylesheets().add(getClass().getResource("/style/Application.css").toExternalForm());

        Stage alertWindow = (Stage) dialogPane.getScene().getWindow();
        alertWindow.initStyle(StageStyle.TRANSPARENT);
        dialogPane.getScene().setFill(Color.TRANSPARENT);
        alertWindow.initOwner(parent.getScene().getWindow());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            ReservationDao reservationDao = new ReservationDao();
            reservationDao.delete(Account.getCurrentUser().getReservation(flight).getId());

            alert.close();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            goBack(new ActionEvent());
        } else {
            alert.close();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
        }
    }

    private void fillSeatMap() {
        SeatDao seatDao = new SeatDao();
        allSeats = new ArrayList<>(seatDao.readAll());
        displayedSeats = new ArrayList<>(allSeats);
        
        updateAvailabilityStatistics();
        renderSeatMap();
    }
    
    private void renderSeatMap() {
        // Clear existing seats from map
        seatMap.getChildren().clear();
        seatGroup.getToggles().clear();
        
        if (displayedSeats == null || displayedSeats.isEmpty()) {
            // Show message if no seats match filter
            Label noSeatsLabel = new Label("No seats match the current filter");
            noSeatsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: gray;");
            seatMap.add(noSeatsLabel, 0, 0, 7, 1);
            return;
        }
        
        // Get all possible columns from all seats (to maintain original positions)
        List<String> allPossibleColumns = allSeats.stream()
            .map(Seat::getColumn)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        
        // Get unique columns from displayed seats (filtered)
        java.util.Set<String> displayedColumnsSet = displayedSeats.stream()
            .map(Seat::getColumn)
            .collect(Collectors.toSet());
        
        // Get unique rows from displayed seats, sorted
        List<Integer> uniqueRows = displayedSeats.stream()
            .map(Seat::getRow)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
        
        if (uniqueRows.isEmpty()) {
            return;
        }
        
        // Create a map for quick seat lookup: (column, row) -> Seat
        java.util.Map<String, Seat> seatMapLookup = displayedSeats.stream()
            .collect(Collectors.toMap(
                seat -> seat.getColumn() + "_" + seat.getRow(),
                seat -> seat,
                (existing, replacement) -> existing
            ));
        
        // Check if filters are active (for exit sign visibility)
        String selectedType = filterByType != null ? filterByType.getValue() : "All";
        String searchText = seatSearchField != null ? seatSearchField.getText().trim() : "";
        boolean filtersActive = (selectedType != null && !selectedType.equals("All")) || 
                                (searchText != null && !searchText.isEmpty());
        
        // Calculate row number column position (middle of all possible columns)
        int rowNumberCol = allPossibleColumns.size() / 2;
        
        // Add column headers - maintain original column positions
        // Show headers for displayed columns, add invisible spacers for filtered columns
        for (int i = 0; i < allPossibleColumns.size(); i++) {
            String column = allPossibleColumns.get(i);
            
            // Add row number column header if we're at that position
            if (i == rowNumberCol) {
                Label lbl = new Label("");
                lbl.setMinSize(40, 40);
                seatMap.add(lbl, i, 0);
                continue;
            }
            
            // Calculate header column position (accounting for row number column)
            int headerCol = (i > rowNumberCol) ? i + 1 : i;
            
            if (displayedColumnsSet.contains(column)) {
                // Add visible column header
                Label lbl = new Label(column);
                lbl.setMinSize(40, 40);
                lbl.setAlignment(Pos.CENTER);
                seatMap.add(lbl, headerCol, 0);
            } else {
                // Add invisible spacer to maintain original column positions
                Label spacer = new Label("");
                spacer.setMinSize(40, 40);
                spacer.setOpacity(0);
                spacer.setMouseTransparent(true);
                seatMap.add(spacer, headerCol, 0);
            }
        }
        
        // Add row number column header if not already added
        if (rowNumberCol >= allPossibleColumns.size()) {
            Label lbl = new Label("");
            lbl.setMinSize(40, 40);
            seatMap.add(lbl, allPossibleColumns.size(), 0);
        }
        
        // Add rows with actual row numbers
        int gridRow = 1;
        for (Integer actualRow : uniqueRows) {
            // Add row number (use actual seat row number)
            Label rowLabel = new Label(String.valueOf(actualRow));
            rowLabel.setMinSize(40, 40);
            rowLabel.setAlignment(Pos.CENTER);
            seatMap.add(rowLabel, rowNumberCol, gridRow);
            
            // Add seats for this row - maintain original column positions
            // This ensures that when filtering (e.g., showing only column E), it stays in its original position
            for (int i = 0; i < allPossibleColumns.size(); i++) {
                String column = allPossibleColumns.get(i);
                
                // Skip row number column
                if (i == rowNumberCol) {
                    continue;
                }
                
                // Calculate grid column position maintaining original order
                // Columns before row number: position = i
                // Columns after row number: position = i + 1 (to account for row number column)
                int seatCol = (i > rowNumberCol) ? i + 1 : i;
                
                // Only show seat if it's in the displayed columns set
                if (displayedColumnsSet.contains(column)) {
                    String seatKey = column + "_" + actualRow;
                    Seat seat = seatMapLookup.get(seatKey);
                    
                    if (seat != null) {
                        seatGroup.getToggles().add(seat);
                        
                        // Reset seat state
                        seat.getStyleClass().removeAll("UnavailableSeatIcon");
                        seat.setDisable(false);
                        
                        // Check if reserved
                        if (seat.isReservedBy(Account.getCurrentUser(), flight)) {
                            seatGroup.selectToggle(seat);
                            selectedSeat = seat;
                        } else if (seat.isReserved(flight)) {
                            seat.getStyleClass().add("UnavailableSeatIcon");
                            seat.setDisable(true);
                        }
                        
                        seatMap.add(seat, seatCol, gridRow);
                    } else {
                        // Empty space for seats that don't exist in this row/column
                        Label empty = new Label("");
                        empty.setMinSize(40, 40);
                        seatMap.add(empty, seatCol, gridRow);
                    }
                } else {
                    // Column is filtered out - add invisible spacer to maintain original column positions
                    // This ensures that when searching for "E", it stays in its original position, not shifted to "A"
                    Label spacer = new Label("");
                    spacer.setMinSize(40, 40);
                    spacer.setOpacity(0); // Invisible but maintains space
                    spacer.setMouseTransparent(true); // Don't block mouse events
                    seatMap.add(spacer, seatCol, gridRow);
                }
            }
            
            // Add exit labels at appropriate rows - only if filters are NOT active
            // Place them in positions that don't overlay seats
            if (!filtersActive && (actualRow == 8 || actualRow == 12)) {
                // Check if there is a seat at the leftmost column for this row
                String leftmostColumn = allPossibleColumns.get(0); // First column (e.g., "A")
                boolean hasSeatAtLeft = seatMapLookup.containsKey(leftmostColumn + "_" + actualRow);
                
                // Find the actual grid column positions
                // Leftmost visible column grid position
                int leftmostGridCol = -1;
                for (int i = 0; i < allPossibleColumns.size(); i++) {
                    if (displayedColumnsSet.contains(allPossibleColumns.get(i))) {
                        leftmostGridCol = (i < rowNumberCol) ? i : i + 1;
                        break;
                    }
                }
                
                // Rightmost visible column grid position
                int rightmostGridCol = -1;
                for (int i = allPossibleColumns.size() - 1; i >= 0; i--) {
                    if (displayedColumnsSet.contains(allPossibleColumns.get(i))) {
                        rightmostGridCol = (i > rowNumberCol) ? i + 1 : i;
                        break;
                    }
                }
                
                // Place left exit: before the first seat column, but only if no seat at leftmost position
                if (!hasSeatAtLeft && leftmostGridCol >= 0) {
                    Label leftExit = new Label("◀ EXIT");
                    leftExit.setMinSize(40, 40);
                    int leftExitCol = Math.max(0, leftmostGridCol - 1);
                    seatMap.add(leftExit, leftExitCol, gridRow);
                }
                
                // Place right exit: after the last seat column
                if (rightmostGridCol >= 0) {
                    Label rightExit = new Label("EXIT ▶");
                    rightExit.setMinSize(40, 40);
                    rightExit.setAlignment(Pos.BASELINE_RIGHT);
                    int rightExitCol = rightmostGridCol + 1;
                    seatMap.add(rightExit, rightExitCol, gridRow);
                }
            }
            
            gridRow++;
        }
    }
    
    private void applyFilters() {
        if (allSeats == null || allSeats.isEmpty()) {
            return;
        }
        
        String selectedType = filterByType != null ? filterByType.getValue() : "All";
        String searchText = seatSearchField != null ? seatSearchField.getText().trim() : "";
        
        displayedSeats = allSeats.stream()
            .filter(seat -> {
                // Filter by type
                if (selectedType != null && !selectedType.equals("All")) {
                    if (!seat.getType().equalsIgnoreCase(selectedType)) {
                        return false;
                    }
                }
                
                // Filter by search text (e.g., "A5", "B12", "A", "5")
                if (!searchText.isEmpty()) {
                    String seatLabel = seat.getColumn() + seat.getRow();
                    String seatColumn = seat.getColumn();
                    String seatRow = String.valueOf(seat.getRow());
                    
                    String upperSearch = searchText.toUpperCase().trim();
                    String upperLabel = seatLabel.toUpperCase();
                    String upperColumn = seatColumn.toUpperCase();
                    
                    // Check if search matches:
                    // 1. Exact seat label match (e.g., "A5" matches "A5")
                    // 2. Column only match (e.g., "A" matches all seats in column A)
                    // 3. Row only match (e.g., "5" matches all seats in row 5)
                    // 4. Label contains search (e.g., "A5" contains "A" or contains "5")
                    boolean matches = upperLabel.equals(upperSearch) ||  // Exact match: "A5" = "A5"
                                     upperColumn.equals(upperSearch) ||  // Column match: "A" = "A"
                                     seatRow.equals(searchText.trim()) || // Row match: "5" = "5"
                                     upperLabel.contains(upperSearch);    // Partial: "A5" contains "A" or "5"
                    
                    if (!matches) {
                        return false;
                    }
                }
                
                return true;
            })
            .collect(Collectors.toList());
        
        renderSeatMap();
    }
    
    private void updateAvailabilityStatistics() {
        if (flight == null || allSeats == null || allSeats.isEmpty()) {
            return;
        }
        
        ReservationDao reservationDao = new ReservationDao();
        int totalSeats = allSeats.size();
        int occupiedSeats = reservationDao.countReservations(flight);
        
        // Count available seats by type
        long availableFirst = allSeats.stream()
            .filter(seat -> seat.getType().equalsIgnoreCase("first"))
            .filter(seat -> !seat.isReserved(flight))
            .count();
            
        long availableBusiness = allSeats.stream()
            .filter(seat -> seat.getType().equalsIgnoreCase("business"))
            .filter(seat -> !seat.isReserved(flight))
            .count();
            
        long availableEconomy = allSeats.stream()
            .filter(seat -> seat.getType().equalsIgnoreCase("economy"))
            .filter(seat -> !seat.isReserved(flight))
            .count();
        
        // Update labels if they exist
        if (lblAvailableFirst != null) {
            lblAvailableFirst.setText("First: " + availableFirst + " available");
        }
        if (lblAvailableBusiness != null) {
            lblAvailableBusiness.setText("Business: " + availableBusiness + " available");
        }
        if (lblAvailableEconomy != null) {
            lblAvailableEconomy.setText("Economy: " + availableEconomy + " available");
        }
        if (lblTotalSeats != null) {
            lblTotalSeats.setText("Total: " + totalSeats + " seats");
        }
        if (lblOccupiedSeats != null) {
            lblOccupiedSeats.setText("Occupied: " + occupiedSeats + " seats");
        }
    }
}
