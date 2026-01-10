package models;

import data.AirlineDao;
import data.FavoriteDao;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDateTime;

public class Flight {
    private int id;
    private LocalDateTime depDatetime;
    private LocalDateTime arrDatetime;
    private double firstPrice;
    private double businessPrice;
    private double economyPrice;
    private double luggagePrice;
    private double weightPrice;
    private Airport depAirport;
    private Airport arrAirport;
    private int airline;
    private SimpleBooleanProperty favorite;
    private String status;

    public Flight() {
        this.firstPrice = -1;
        this.businessPrice = -1;
        this.economyPrice = -1;
        this.luggagePrice = -1;
        this.weightPrice = -1;
        this.favorite = new SimpleBooleanProperty(false);
        this.status = "On Time";
    }

    public boolean isFavorite() {
        FavoriteDao favoriteDao = new FavoriteDao();
        Favorite favorite = favoriteDao.read(this, Account.getCurrentUser());
        if (favorite != null) {
            this.getFavoriteProperty().set(true);
            return true;
        }
        else {
            this.getFavoriteProperty().set(false);
            return false;
        }
    }

    public SimpleBooleanProperty getFavoriteProperty() {
        return favorite;
    }

    public void addFavorite() {
        FavoriteDao favoriteDao = new FavoriteDao();
        favoriteDao.create(new Favorite(this, Account.getCurrentUser()));
    }

    public void removeFavorite() {
        FavoriteDao favoriteDao = new FavoriteDao();
        Favorite favorite = favoriteDao.read(this, Account.getCurrentUser());
        favoriteDao.delete(favorite.getId());
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDepDatetime() {
        return depDatetime;
    }
    public void setDepDatetime(LocalDateTime dep_datetime) {
        this.depDatetime = dep_datetime;
    }

    public LocalDateTime getArrDatetime() {
        return arrDatetime;
    }
    public void setArrDatetime(LocalDateTime arr_datetime) {
        this.arrDatetime = arr_datetime;
    }

    public double getFirstPrice() {
        return firstPrice;
    }
    public Airline getAirline() {
        return AirlineDao.airlinesMap.get(airline);
    }
    public void setAirline(int airline) {
        this.airline = airline;
        AirlineDao airlineDao = new AirlineDao();
        airlineDao.updateAirlinesMap(airline);
    }
    public Airport getDepAirport() {
        return depAirport;
    }

    public void setDepAirport(Airport dep_airport) {
        this.depAirport = dep_airport;
    }
    public Airport getArrAirport() {
        return arrAirport;
    }

    public void setArrAirport(Airport arr_airport) {
        this.arrAirport = arr_airport;
    }
    public String getFirstPriceFormatted() {
        return String.format("%.02f$",firstPrice);
    }
    public void setFirstPrice(double first_price) {
        this.firstPrice = first_price;
    }

    public double getBusinessPrice() {
        return businessPrice;
    }
    public String getBusinessPriceFormatted() {
        return String.format("%.02f$",businessPrice);
    }
    public void setBusinessPrice(double businessPrice) {
        this.businessPrice = businessPrice;
    }

    public double getEconomyPrice() {
        return economyPrice;
    }
    public String getEconomyPriceFormatted() {
        return String.format("%.02f$",economyPrice);
    }
    public void setEconomyPrice(double economy_price) {
        this.economyPrice = economy_price;
    }

    public double getLuggagePrice() {
        return luggagePrice;
    }
    public void setLuggagePrice(double luggage_price) {
        this.luggagePrice = luggage_price;
    }

    public double getWeightPrice() {
        return weightPrice;
    }
    public void setWeightPrice(double weight_price) {
        this.weightPrice = weight_price;
    }
    
    public String getStatus() {
        return status == null ? "On Time" : status;
    }
    
    public void setStatus(String status) {
        this.status = status == null ? "On Time" : status;
    }
    
    public String getStatusColor() {
        if (status == null) return "#4CAF50"; // Green for On Time
        switch (status.toLowerCase()) {
            case "delayed":
                return "#F44336"; // Red
            case "on time":
                return "#4CAF50"; // Green
            case "cancelled":
                return "#9E9E9E"; // Grey
            default:
                return "#4CAF50"; // Default green
        }
    }
    
    public Double getMinPrice() {
        double min = economyPrice;
        if (businessPrice > 0 && businessPrice < min) min = businessPrice;
        if (firstPrice > 0 && firstPrice < min) min = firstPrice;
        return min;
    }
    
    public Long getDurationMinutes() {
        if (depDatetime == null || arrDatetime == null) return null;
        return java.time.Duration.between(depDatetime, arrDatetime).toMinutes();
    }
}
