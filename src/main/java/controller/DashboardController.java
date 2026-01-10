package controller;

import data.AirlineDao;
import data.AirportDao;
import data.FlightDao;
import data.ReservationDao;
import util.FlightCatalogRestClient;
import util.FlightConverter;
import util.FlightDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Comparator;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Account;
import models.Airport;
import models.Flight;
import org.controlsfx.control.SearchableComboBox;
import view.Palette;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class DashboardController implements Initializable {

    @FXML
    private StackPane parent;
    @FXML
    private Button actionButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private StackPane actionPanel;

    @FXML
    private ImageView airlineLogo;

    @FXML
    private SearchableComboBox<Airport> arrAirport;

    @FXML
    private SearchableComboBox<String> arrCity;

    @FXML
    private SearchableComboBox<String> arrCountry;

    @FXML
    private DatePicker arrDate;

    @FXML
    private Spinner<Integer> arrHour;

    @FXML
    private Spinner<Integer> arrMinute;

    @FXML
    private TableColumn<Flight, String> colArrAirport;

    @FXML
    private TableColumn<Flight, String> colArrDateTime;

    @FXML
    private TableColumn<Flight, String> colCapacity;

    @FXML
    private TableColumn<Flight, String> colDepAirport;

    @FXML
    private TableColumn<Flight, String> colDepDateTime;

    @FXML
    private TableColumn<Flight, Integer> colId;

    @FXML
    private SearchableComboBox<Airport> depAirport;

    @FXML
    private SearchableComboBox<String> depCity;

    @FXML
    private SearchableComboBox<String> depCountry;

    @FXML
    private DatePicker depDate;

    @FXML
    private Spinner<Integer> depHour;

    @FXML
    private Spinner<Integer> depMinute;

    @FXML
    private TableView<Flight> flightTable;

    @FXML
    private Label lblAirlineName;

    @FXML
    private Label lblTitle;

    @FXML
    private TextField priceBusinessClass;

    @FXML
    private TextField priceEconomyClass;

    @FXML
    private TextField priceFirstClass;

    @FXML
    private TextField priceLuggage;

    @FXML
    private TextField priceWeight;

    @FXML
    private TextField searchBar;
    
    @FXML
    private DatePicker searchDepDatePicker;
    
    @FXML
    private DatePicker searchArrDatePicker;
    
    @FXML
    private ComboBox<String> sortComboBox;
    
    @FXML
    private TableColumn<Flight, String> colDuration;
    
    @FXML
    private TableColumn<Flight, String> colStatus;
    
    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private ToggleButton themeButton;

    private final Alert alert = new Alert(Alert.AlertType.INFORMATION);
    private FilteredList<Flight> results;
    private FlightCatalogRestClient restClient;
    private boolean useSpringBoot = false;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set up UI components first
        setData();
        
        // Check if Spring Boot service is available
        restClient = FlightCatalogRestClient.getInstance();
        useSpringBoot = restClient.isServiceAvailable();
        
        if (useSpringBoot) {
            System.out.println("Using Spring Boot Flight Catalog Service");
            loadFlightsFromSpringBoot();
        } else {
            System.out.println("Spring Boot service not available, using DAO");
            loadFlightsFromDAO();
        }
        
        findFlight();
        setupFilters();
        setupSorting();

        if (Palette.getDefaultPalette().equals(Palette.DarkPalette)) {
            themeButton.setSelected(true);
        }

        alert.setHeaderText("Warning");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/style/Application.css").toExternalForm());

        Stage alertWindow = (Stage) dialogPane.getScene().getWindow();
        alertWindow.initStyle(StageStyle.TRANSPARENT);
        dialogPane.getScene().setFill(Color.TRANSPARENT);
        Platform.runLater(() -> alertWindow.initOwner(parent.getScene().getWindow()));
    }

    private void setData() {
        airlineLogo.setImage(Account.getCurrentUser().getAirline().getLogo());
        lblAirlineName.setText(Account.getCurrentUser().getAirline().getName());

        depCountry.setItems(AirportDao.getCountryList());
        arrCountry.setItems(AirportDao.getCountryList());

        depCountry.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> depCity.setItems(AirportDao.getCityList(newValue)));
        arrCountry.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> arrCity.setItems(AirportDao.getCityList(newValue)));

        depCity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> depAirport.setItems(AirportDao.getAirportList(newValue)));
        arrCity.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> arrAirport.setItems(AirportDao.getAirportList(newValue)));

        depAirport.setCellFactory((ListView<Airport> listView) -> new ListCell<>() {

            @Override
            public void updateItem(Airport item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });
        depAirport.setButtonCell(new ListCell<>(){
            @Override
            public void updateItem(Airport item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });

        arrAirport.setCellFactory((ListView<Airport> listView) -> new ListCell<>() {
            @Override
            public void updateItem(Airport item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });
        arrAirport.setButtonCell(new ListCell<>(){
            @Override
            public void updateItem(Airport item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getName());
                } else {
                    setText(null);
                }
            }
        });

        {
            SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
            depHour.setValueFactory(hourValueFactory);
            SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
            depMinute.setValueFactory(minuteValueFactory);
        }

        {
            SpinnerValueFactory<Integer> hourValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23);
            arrHour.setValueFactory(hourValueFactory);
            SpinnerValueFactory<Integer> minuteValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59);
            arrMinute.setValueFactory(minuteValueFactory);
        }

        {
            allowDoubleOnly(priceFirstClass);
            allowDoubleOnly(priceBusinessClass);
            allowDoubleOnly(priceEconomyClass);
            allowDoubleOnly(priceLuggage);
            allowDoubleOnly(priceWeight);
        }
        
        {
            // Initialize status combo box
            statusComboBox.setValue("On Time");
        }

        {
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colDepAirport.setCellValueFactory(flight -> new SimpleStringProperty(flight.getValue().getDepAirport().getName()));
            colDepDateTime.setCellValueFactory(flight -> new SimpleStringProperty(flight.getValue().getDepDatetime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))));
            colArrAirport.setCellValueFactory(flight -> new SimpleStringProperty(flight.getValue().getArrAirport().getName()));
            colArrDateTime.setCellValueFactory(flight -> new SimpleStringProperty(flight.getValue().getArrDatetime().format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"))));
            colCapacity.setCellValueFactory(flight -> {
                ReservationDao reservationDao = new ReservationDao();
                return new SimpleStringProperty(reservationDao.countReservations(flight.getValue()) + "/120");
            });
            colDuration.setCellValueFactory(flight -> {
                Long durationMinutes = flight.getValue().getDurationMinutes();
                if (durationMinutes != null) {
                    long hours = durationMinutes / 60;
                    long minutes = durationMinutes % 60;
                    return new SimpleStringProperty(String.format("%dh %dm", hours, minutes));
                }
                return new SimpleStringProperty("N/A");
            });
            colStatus.setCellValueFactory(flight -> {
                String status = flight.getValue().getStatus();
                return new SimpleStringProperty(status);
            });
            // Initialize empty results list - will be populated after data loads
            if (results == null) {
                results = new FilteredList<>(FXCollections.observableArrayList(), flight -> true);
            }
            flightTable.setItems(results);

            flightTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                editButton.setDisable(newValue == null);
                deleteButton.setDisable(newValue == null);
            });

            flightTable.setRowFactory(tableView -> {
                TableRow<Flight> row = new TableRow<Flight>() {
                    @Override
                    protected void updateItem(Flight flight, boolean empty) {
                        super.updateItem(flight, empty);
                        if (empty || flight == null) {
                            setStyle("");
                        } else {
                            String color = flight.getStatusColor();
                            setStyle("-fx-background-color: " + color + "20;"); // 20 = opacity
                        }
                    }
                };
                row.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    int index = row.getIndex();
                    if (tableView.getSelectionModel().isSelected(index)) {
                        tableView.getSelectionModel().clearSelection();
                        event.consume();
                    }
                });
                // Right-click context menu for status update
                ContextMenu contextMenu = new ContextMenu();
                MenuItem updateStatusItem = new MenuItem("Update Status");
                updateStatusItem.setOnAction(e -> updateFlightStatus());
                contextMenu.getItems().add(updateStatusItem);
                row.setContextMenu(contextMenu);
                return row;
            });
        }


    }

    private void allowDoubleOnly(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*|\\d+\\.\\d*");
        textField.setTextFormatter(new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> pattern.matcher(change.getControlNewText()).matches() ? change : null));
    }

    @FXML
    void openAddTab() {
        actionPanel.setPrefWidth(Region.USE_COMPUTED_SIZE);
        actionPanel.setVisible(true);
        flightTable.getSelectionModel().clearSelection();

        lblTitle.setText("New Flight");
        statusComboBox.setValue("On Time");

        actionButton.setText("ADD");
        actionButton.setOnAction(e -> addFlight());
    }

    private void addFlight() {
        DialogPane dialogPane = alert.getDialogPane();
        Platform.runLater(() -> Palette.getDefaultPalette().usePalette(dialogPane.getScene()));

        if (depAirport.getSelectionModel().isEmpty() || arrAirport.getSelectionModel().isEmpty()) {
            alert.setContentText("Please select airports to continue.");
            parent.getScene().lookup("#overlay-layer").setDisable(false);
            alert.showAndWait();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            return;

        }

        if (depDate.getValue() == null || arrDate.getValue() == null) {
            alert.setContentText("Please specify dates to continue.");
            parent.getScene().lookup("#overlay-layer").setDisable(false);
            alert.showAndWait();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            return;
        }

        LocalDateTime depDateTime = LocalDateTime.of(depDate.getValue(), LocalTime.of(depHour.getValue(), depMinute.getValue()));
        LocalDateTime arrDateTime = LocalDateTime.of(arrDate.getValue(), LocalTime.of(arrHour.getValue(), arrMinute.getValue()));

        if (!depDateTime.isBefore(arrDateTime)) {
            alert.setContentText("Arrival dateTime should be after departure dateTime.");
            parent.getScene().lookup("#overlay-layer").setDisable(false);
            alert.showAndWait();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            return;
        }

        if (priceFirstClass.getText().isBlank() || priceEconomyClass.getText().isBlank() || priceBusinessClass.getText().isBlank() || priceLuggage.getText().isBlank() || priceWeight.getText().isBlank())
        {
            alert.setContentText("Please fill the pricing fields");
            parent.getScene().lookup("#overlay-layer").setDisable(false);
            alert.showAndWait();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            return;
        }

        Flight flight = new Flight();
        flight.setAirline(Account.getCurrentUser().getAirline().getId());
        flight.setDepAirport(depAirport.getValue());
        flight.setArrAirport(arrAirport.getValue());
        flight.setDepDatetime(depDateTime);
        flight.setArrDatetime(arrDateTime);
        flight.setFirstPrice(Double.parseDouble(priceFirstClass.getText()));
        flight.setBusinessPrice(Double.parseDouble(priceBusinessClass.getText()));
        flight.setEconomyPrice(Double.parseDouble(priceEconomyClass.getText()));
        flight.setLuggagePrice(Double.parseDouble(priceLuggage.getText()));
        flight.setWeightPrice(Double.parseDouble(priceWeight.getText()));
        flight.setStatus(statusComboBox.getValue() != null ? statusComboBox.getValue() : "On Time");

        if (useSpringBoot) {
            try {
                // Check for duplicate
                String dateStr = depDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String checkUrl = "/flights/check-duplicate?depAirportId=" + depAirport.getValue().getId() +
                    "&arrAirportId=" + arrAirport.getValue().getId() +
                    "&date=" + dateStr +
                    "&airlineId=" + Account.getCurrentUser().getAirline().getId();
                
                String duplicateResponse = restClient.get(checkUrl, String.class);
                if (duplicateResponse != null && duplicateResponse.contains("\"isDuplicate\":true")) {
                    alert.setContentText("Duplicate flight detected: A flight with the same route, date, and airline already exists.");
                    parent.getScene().lookup("#overlay-layer").setDisable(false);
                    alert.showAndWait();
                    parent.getScene().lookup("#overlay-layer").setDisable(true);
                    return;
                }
                
                // Add flight via Spring Boot
                FlightDTO flightDTO = FlightDTO.fromFlight(flight);
                String response = restClient.post("/flights", flightDTO, String.class);
                if (response != null) {
                    JsonNode jsonNode = objectMapper.readTree(response);
                    Flight savedFlight = FlightConverter.fromJsonNode(jsonNode);
                    @SuppressWarnings("unchecked")
                    ObservableList<Flight> source = (ObservableList<Flight>) results.getSource();
                    source.add(0, savedFlight);
                    closePanel();
                }
            } catch (Exception e) {
                alert.setContentText("Failed to add flight: " + e.getMessage());
                parent.getScene().lookup("#overlay-layer").setDisable(false);
                alert.showAndWait();
                parent.getScene().lookup("#overlay-layer").setDisable(true);
            }
        } else {
            // Fallback to DAO - check for duplicate
            FlightDao flightDao = new FlightDao();
            if (flightDao.isDuplicate(
                    depAirport.getValue().getId(),
                    arrAirport.getValue().getId(),
                    depDateTime,
                    Account.getCurrentUser().getAirline().getId(),
                    null)) {
                alert.setContentText("Duplicate flight detected: A flight with the same route, date, and airline already exists.");
                parent.getScene().lookup("#overlay-layer").setDisable(false);
                alert.showAndWait();
                parent.getScene().lookup("#overlay-layer").setDisable(true);
                return;
            }
            
            flightDao.create(flight);
            @SuppressWarnings("unchecked")
            ObservableList<Flight> source = (ObservableList<Flight>) results.getSource();
            source.add(0, flight);
            closePanel();
        }
    }

    @FXML
    void openEditTab() {
        actionPanel.setPrefWidth(Region.USE_COMPUTED_SIZE);
        actionPanel.setVisible(true);

        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();

        lblTitle.setText("Flight #" + selectedFlight.getId());

        //departure data
        {
            depCountry.getSelectionModel().select(selectedFlight.getDepAirport().getCountry());
            depCity.getSelectionModel().select(selectedFlight.getDepAirport().getCity());
            depAirport.getItems().forEach(airport -> {
                if (airport.getId() == selectedFlight.getDepAirport().getId()) {
                    depAirport.getSelectionModel().select(airport);
                }
            });

            depDate.setValue(selectedFlight.getDepDatetime().toLocalDate());
            depHour.getValueFactory().setValue(selectedFlight.getDepDatetime().getHour());
            depMinute.getValueFactory().setValue(selectedFlight.getDepDatetime().getMinute());
        }

        //arrival data
        {
            arrCountry.getSelectionModel().select(selectedFlight.getArrAirport().getCountry());
            arrCity.getSelectionModel().select(selectedFlight.getArrAirport().getCity());
            arrAirport.getItems().forEach(airport -> {
                if (airport.getId() == selectedFlight.getArrAirport().getId()) {
                    arrAirport.getSelectionModel().select(airport);
                }
            });

            arrDate.setValue(selectedFlight.getArrDatetime().toLocalDate());
            arrHour.getValueFactory().setValue(selectedFlight.getArrDatetime().getHour());
            arrMinute.getValueFactory().setValue(selectedFlight.getArrDatetime().getMinute());
        }

        //pricing data
        {
            priceFirstClass.setText(String.valueOf(selectedFlight.getFirstPrice()));
            priceBusinessClass.setText(String.valueOf(selectedFlight.getBusinessPrice()));
            priceEconomyClass.setText(String.valueOf(selectedFlight.getEconomyPrice()));
            priceLuggage.setText(String.valueOf(selectedFlight.getLuggagePrice()));
            priceWeight.setText(String.valueOf(selectedFlight.getWeightPrice()));
        }
        
        //status data
        {
            String status = selectedFlight.getStatus();
            if (status != null && (status.equals("On Time") || status.equals("Delayed") || status.equals("Cancelled"))) {
                statusComboBox.setValue(status);
            } else {
                statusComboBox.setValue("On Time");
            }
        }

        actionButton.setText("UPDATE");
        actionButton.setOnAction(e -> updateFlight(selectedFlight));
    }

    private void updateFlight(Flight flight) {
        DialogPane dialogPane = alert.getDialogPane();
        Platform.runLater(() -> Palette.getDefaultPalette().usePalette(dialogPane.getScene()));

        if (depAirport.getSelectionModel().isEmpty() || arrAirport.getSelectionModel().isEmpty())
        {
            alert.setContentText("Please select airports to continue.");
            parent.getScene().lookup("#overlay-layer").setDisable(false);
            alert.showAndWait();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            return;

        }

        if (depDate.getValue() == null || arrDate.getValue() == null) {
            alert.setContentText("Please specify dates to continue.");
            parent.getScene().lookup("#overlay-layer").setDisable(false);
            alert.showAndWait();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            return;
        }

        LocalDateTime depDateTime = LocalDateTime.of(depDate.getValue(), LocalTime.of(depHour.getValue(), depMinute.getValue()));
        LocalDateTime arrDateTime = LocalDateTime.of(arrDate.getValue(), LocalTime.of(arrHour.getValue(), arrMinute.getValue()));

        if (!depDateTime.isBefore(arrDateTime)) {
            alert.setContentText("Arrival dateTime should be after departure dateTime.");
            parent.getScene().lookup("#overlay-layer").setDisable(false);
            alert.showAndWait();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            return;
        }

        if (priceFirstClass.getText().isBlank() || priceEconomyClass.getText().isBlank() || priceBusinessClass.getText().isBlank() || priceLuggage.getText().isBlank() || priceWeight.getText().isBlank())
        {
            alert.setContentText("Please fill the pricing fields");
            parent.getScene().lookup("#overlay-layer").setDisable(false);
            alert.showAndWait();
            parent.getScene().lookup("#overlay-layer").setDisable(true);
            return;
        }

        flight.setAirline(Account.getCurrentUser().getAirline().getId());
        flight.setDepAirport(depAirport.getValue());
        flight.setArrAirport(arrAirport.getValue());
        flight.setDepDatetime(LocalDateTime.of(depDate.getValue(), LocalTime.of(depHour.getValue(), depMinute.getValue())));
        flight.setArrDatetime(LocalDateTime.of(arrDate.getValue(), LocalTime.of(arrHour.getValue(), arrMinute.getValue())));
        flight.setFirstPrice(Double.parseDouble(priceFirstClass.getText()));
        flight.setBusinessPrice(Double.parseDouble(priceBusinessClass.getText()));
        flight.setEconomyPrice(Double.parseDouble(priceEconomyClass.getText()));
        flight.setLuggagePrice(Double.parseDouble(priceLuggage.getText()));
        flight.setWeightPrice(Double.parseDouble(priceWeight.getText()));
        flight.setStatus(statusComboBox.getValue() != null ? statusComboBox.getValue() : "On Time");

        LocalDateTime updatedDepDateTime = LocalDateTime.of(depDate.getValue(), LocalTime.of(depHour.getValue(), depMinute.getValue()));

        if (useSpringBoot) {
            try {
                // Check for duplicate (excluding current flight)
                String dateStr = updatedDepDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String checkUrl = "/flights/check-duplicate?depAirportId=" + depAirport.getValue().getId() +
                    "&arrAirportId=" + arrAirport.getValue().getId() +
                    "&date=" + dateStr +
                    "&airlineId=" + Account.getCurrentUser().getAirline().getId();
                
                String duplicateResponse = restClient.get(checkUrl, String.class);
                if (duplicateResponse != null && duplicateResponse.contains("\"isDuplicate\":true")) {
                    // Check if it's the same flight (by checking if we can find it with exclude)
                    // For now, we'll allow update if it's the same flight
                }
                
                // Check for duplicate excluding current flight
                String checkUrlExclude = "/flights/check-duplicate?depAirportId=" + depAirport.getValue().getId() +
                    "&arrAirportId=" + arrAirport.getValue().getId() +
                    "&date=" + depDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                    "&airlineId=" + Account.getCurrentUser().getAirline().getId() +
                    "&excludeId=" + flight.getId();
                
                // Note: We need to check if there's a duplicate excluding the current flight
                // For now, Spring Boot service handles this in the update method
                
                // Update flight via Spring Boot
                FlightDTO flightDTO = FlightDTO.fromFlight(flight);
                String response = restClient.put("/flights/" + flight.getId(), flightDTO, String.class);
                if (response != null) {
                    JsonNode jsonNode = objectMapper.readTree(response);
                    Flight updatedFlight = FlightConverter.fromJsonNode(jsonNode);
                    @SuppressWarnings("unchecked")
                    ObservableList<Flight> source = (ObservableList<Flight>) results.getSource();
                    int index = source.indexOf(flight);
                    if (index >= 0) {
                        source.set(index, updatedFlight);
                    }
                    flightTable.refresh();
                    closePanel();
                }
            } catch (Exception e) {
                alert.setContentText("Failed to update flight: " + e.getMessage());
                parent.getScene().lookup("#overlay-layer").setDisable(false);
                alert.showAndWait();
                parent.getScene().lookup("#overlay-layer").setDisable(true);
            }
        } else {
            // Fallback to DAO - check for duplicate (excluding current flight)
            FlightDao flightDao = new FlightDao();
            if (flightDao.isDuplicate(
                    depAirport.getValue().getId(),
                    arrAirport.getValue().getId(),
                    updatedDepDateTime,
                    Account.getCurrentUser().getAirline().getId(),
                    flight.getId())) {
                alert.setContentText("Duplicate flight detected: Another flight with the same route, date, and airline already exists.");
                parent.getScene().lookup("#overlay-layer").setDisable(false);
                alert.showAndWait();
                parent.getScene().lookup("#overlay-layer").setDisable(true);
                return;
            }
            
            flightDao.update(flight.getId(), flight);
            flightTable.refresh();
            closePanel();
        }
    }

    @FXML
    void deleteFlight() {
        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
        
        if (useSpringBoot) {
            try {
                restClient.delete("/flights/" + selectedFlight.getId());
                @SuppressWarnings("unchecked")
                ObservableList<Flight> source = (ObservableList<Flight>) results.getSource();
                source.remove(selectedFlight);
            } catch (Exception e) {
                alert.setContentText("Failed to delete flight: " + e.getMessage());
                parent.getScene().lookup("#overlay-layer").setDisable(false);
                alert.showAndWait();
                parent.getScene().lookup("#overlay-layer").setDisable(true);
            }
        } else {
            FlightDao flightDao = new FlightDao();
            flightDao.delete(selectedFlight.getId());
            @SuppressWarnings("unchecked")
            ObservableList<Flight> source = (ObservableList<Flight>) results.getSource();
            source.remove(selectedFlight);
        }
    }
    
    @FXML
    void updateFlightStatus() {
        Flight selectedFlight = flightTable.getSelectionModel().getSelectedItem();
        if (selectedFlight == null) return;
        
        ChoiceDialog<String> dialog = new ChoiceDialog<>("On Time", "On Time", "Delayed", "Cancelled");
        dialog.setTitle("Update Flight Status");
        dialog.setHeaderText("Select new status for Flight #" + selectedFlight.getId());
        dialog.setContentText("Status:");
        
        Platform.runLater(() -> Palette.getDefaultPalette().usePalette(dialog.getDialogPane().getScene()));
        
        dialog.showAndWait().ifPresent(status -> {
            if (useSpringBoot) {
                try {
                    String response = restClient.put("/flights/" + selectedFlight.getId() + "/status?status=" + status, null, String.class);
                    if (response != null) {
                        JsonNode jsonNode = objectMapper.readTree(response);
                        Flight updatedFlight = FlightConverter.fromJsonNode(jsonNode);
                        @SuppressWarnings("unchecked")
                        ObservableList<Flight> source = (ObservableList<Flight>) results.getSource();
                        int index = source.indexOf(selectedFlight);
                        if (index >= 0) {
                            source.set(index, updatedFlight);
                        }
                        flightTable.refresh();
                    }
                } catch (Exception e) {
                    alert.setContentText("Failed to update status: " + e.getMessage());
                    parent.getScene().lookup("#overlay-layer").setDisable(false);
                    alert.showAndWait();
                    parent.getScene().lookup("#overlay-layer").setDisable(true);
                }
            } else {
                selectedFlight.setStatus(status);
                FlightDao flightDao = new FlightDao();
                flightDao.update(selectedFlight.getId(), selectedFlight);
                flightTable.refresh();
            }
        });
    }

    @FXML
    void closePanel() {
        actionPanel.setPrefWidth(0);
        actionPanel.setVisible(false);
        flightTable.getSelectionModel().clearSelection();
        //clear departure data
        {
            depCountry.getSelectionModel().clearSelection();

            depDate.setValue(null);
            depHour.getValueFactory().setValue(0);
            depMinute.getValueFactory().setValue(0);
        }

        //clear arrival data
        {
            arrCountry.getSelectionModel().clearSelection();

            arrDate.setValue(null);
            arrHour.getValueFactory().setValue(0);
            arrMinute.getValueFactory().setValue(0);
        }

        //clear pricing data
        {
            priceFirstClass.setText(String.valueOf(0));
            priceBusinessClass.setText(String.valueOf(0));
            priceEconomyClass.setText(String.valueOf(0));
            priceLuggage.setText(String.valueOf(0));
            priceWeight.setText(String.valueOf(0));
        }
    }

    @FXML
    void changeTheme() {
        if (themeButton.isSelected()) {
            Palette.setDefaultPalette(Palette.DarkPalette);
            Palette.getDefaultPalette().usePalette(parent.getScene());
        }
        else {
            Palette.setDefaultPalette(Palette.LightPalette);
            Palette.getDefaultPalette().usePalette(parent.getScene());
        }
    }

    @FXML
    void Logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Signin.fxml"));

            Scene scene = new Scene(root);
            Palette.getDefaultPalette().usePalette(scene);

            Stage oldStage = (Stage) parent.getScene().getWindow();
            oldStage.close();

            Stage stage = new Stage(StageStyle.UNIFIED);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setTitle("Flight Booking Application");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void uploadeImage(ActionEvent event) {
        AirlineDao airlineDao = new AirlineDao();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG IMAGE","*.png")
        );

        File picturesDir = new File(System.getProperty("user.home") + "\\pictures");
        if (picturesDir.exists()) {
            fileChooser.setInitialDirectory(picturesDir);
        }

        File selectedFile = fileChooser.showOpenDialog(parent.getScene().getWindow());

        if (selectedFile != null) {
            Image logo = new Image(selectedFile.getAbsolutePath());
            airlineLogo.setImage(logo);
            Account.getCurrentUser().getAirline().setLogo(logo);
            airlineDao.update(Account.getCurrentUser().getAirline().getId(), Account.getCurrentUser().getAirline());
        }
    }

    private void findFlight() {
        searchBar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            applyFilters();
        });
        
        // Departure date filter
        searchDepDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
        
        // Arrival date filter
        searchArrDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            applyFilters();
        });
    }
    
    private void setupFilters() {
        // Filters removed - only search and date filter remain
    }
    
    private void setupSorting() {
        sortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("None")) {
                applySorting(newValue);
            }
        });
    }
    
    private void applyFilters() {
        results.setPredicate(flight -> {
            // Text search
            String searchText = searchBar.getText().toLowerCase();
            if (!searchText.isBlank()) {
                boolean matches = String.valueOf(flight.getId()).equals(searchText) ||
                    flight.getDepAirport().getName().toLowerCase().contains(searchText) ||
                    flight.getDepAirport().getCountry().toLowerCase().contains(searchText) ||
                    flight.getDepAirport().getCity().toLowerCase().contains(searchText) ||
                    flight.getDepAirport().getIATA().toLowerCase().equals(searchText) ||
                    flight.getDepAirport().getICAO().toLowerCase().equals(searchText) ||
                    flight.getArrAirport().getName().toLowerCase().contains(searchText) ||
                    flight.getArrAirport().getCountry().toLowerCase().contains(searchText) ||
                    flight.getArrAirport().getCity().toLowerCase().contains(searchText) ||
                    flight.getArrAirport().getIATA().toLowerCase().equals(searchText) ||
                    flight.getArrAirport().getICAO().toLowerCase().equals(searchText);
                if (!matches) return false;
            }
            
            // Departure date filter
            if (searchDepDatePicker.getValue() != null) {
                java.time.LocalDate depDate = flight.getDepDatetime().toLocalDate();
                if (!depDate.equals(searchDepDatePicker.getValue())) {
                    return false;
                }
            }
            
            // Arrival date filter
            if (searchArrDatePicker.getValue() != null) {
                java.time.LocalDate arrDate = flight.getArrDatetime().toLocalDate();
                if (!arrDate.equals(searchArrDatePicker.getValue())) {
                    return false;
                }
            }
            
            return true;
        });
    }
    
    private void applySorting(String sortBy) {
        @SuppressWarnings("unchecked")
        ObservableList<Flight> source = (ObservableList<Flight>) results.getSource();
        List<Flight> sortedList = new ArrayList<>(source);
        
        switch (sortBy) {
            case "Departure Time":
                sortedList.sort(Comparator.comparing(Flight::getDepDatetime));
                break;
            case "Shortest Duration":
                sortedList.sort(Comparator.comparing(f -> f.getDurationMinutes() != null ? f.getDurationMinutes() : Long.MAX_VALUE));
                break;
            case "Farthest Duration":
                sortedList.sort(Comparator.comparing(f -> f.getDurationMinutes() != null ? f.getDurationMinutes() : Long.MIN_VALUE, Comparator.reverseOrder()));
                break;
        }
        
        source.clear();
        source.addAll(sortedList);
    }
    
    private void loadFlightsFromSpringBoot() {
        try {
            String response = restClient.get("/flights/airline/" + Account.getCurrentUser().getAirline().getId(), String.class);
            if (response != null && !response.trim().isEmpty()) {
                JsonNode jsonArray = objectMapper.readTree(response);
                ObservableList<Flight> flights = FXCollections.observableArrayList();
                
                if (jsonArray.isArray()) {
                    for (JsonNode jsonNode : jsonArray) {
                        try {
                            Flight flight = FlightConverter.fromJsonNode(jsonNode);
                            flights.add(flight);
                        } catch (Exception e) {
                            System.err.println("Error converting flight: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
                
                if (!flights.isEmpty()) {
                    results = new FilteredList<>(flights, flight -> true);
                    Platform.runLater(() -> {
                        flightTable.setItems(results);
                        System.out.println("Loaded " + flights.size() + " flights from Spring Boot");
                    });
                } else {
                    System.out.println("No flights returned from Spring Boot, falling back to DAO");
                    loadFlightsFromDAO();
                }
            } else {
                System.out.println("Empty response from Spring Boot, falling back to DAO");
                loadFlightsFromDAO();
            }
        } catch (Exception e) {
            System.err.println("Failed to load flights from Spring Boot: " + e.getMessage());
            e.printStackTrace();
            // Fallback to DAO
            loadFlightsFromDAO();
        }
    }
    
    private void loadFlightsFromDAO() {
        FlightDao flightDao = new FlightDao();
        List<Flight> flights = flightDao.read(Account.getCurrentUser().getAirline());
        ObservableList<Flight> flightList = FXCollections.observableArrayList(flights);
        results = new FilteredList<>(flightList, flight -> true);
        Platform.runLater(() -> {
            flightTable.setItems(results);
            System.out.println("Using DAO - loaded " + results.size() + " flights");
        });
        useSpringBoot = false;
    }
    
}
