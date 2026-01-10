package com.skynet.flightcatalog.model;

import jakarta.persistence.*;
import com.skynet.flightcatalog.config.SqliteDateTimeConverter;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
public class Flight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "id_airline")
    private Integer airlineId;
    
    @Column(name = "dep_airport")
    private Integer depAirportId;
    
    @Column(name = "arr_airport")
    private Integer arrAirportId;
    
    @Convert(converter = SqliteDateTimeConverter.class)
    @Column(name = "dep_datetime")
    private LocalDateTime depDatetime;
    
    @Convert(converter = SqliteDateTimeConverter.class)
    @Column(name = "arr_datetime")
    private LocalDateTime arrDatetime;
    
    @Column(name = "first_price")
    private Double firstPrice;
    
    @Column(name = "business_price")
    private Double businessPrice;
    
    @Column(name = "economy_price")
    private Double economyPrice;
    
    @Column(name = "luggage_price")
    private Double luggagePrice;
    
    @Column(name = "weight_price")
    private Double weightPrice;
    
    @Column(name = "status")
    private String status;
    
    // Default constructor
    public Flight() {
        this.status = "On Time"; // Default status
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getAirlineId() {
        return airlineId;
    }
    
    public void setAirlineId(Integer airlineId) {
        this.airlineId = airlineId;
    }
    
    public Integer getDepAirportId() {
        return depAirportId;
    }
    
    public void setDepAirportId(Integer depAirportId) {
        this.depAirportId = depAirportId;
    }
    
    public Integer getArrAirportId() {
        return arrAirportId;
    }
    
    public void setArrAirportId(Integer arrAirportId) {
        this.arrAirportId = arrAirportId;
    }
    
    public LocalDateTime getDepDatetime() {
        return depDatetime;
    }
    
    public void setDepDatetime(LocalDateTime depDatetime) {
        this.depDatetime = depDatetime;
    }
    
    public LocalDateTime getArrDatetime() {
        return arrDatetime;
    }
    
    public void setArrDatetime(LocalDateTime arrDatetime) {
        this.arrDatetime = arrDatetime;
    }
    
    public Double getFirstPrice() {
        return firstPrice;
    }
    
    public void setFirstPrice(Double firstPrice) {
        this.firstPrice = firstPrice;
    }
    
    public Double getBusinessPrice() {
        return businessPrice;
    }
    
    public void setBusinessPrice(Double businessPrice) {
        this.businessPrice = businessPrice;
    }
    
    public Double getEconomyPrice() {
        return economyPrice;
    }
    
    public void setEconomyPrice(Double economyPrice) {
        this.economyPrice = economyPrice;
    }
    
    public Double getLuggagePrice() {
        return luggagePrice;
    }
    
    public void setLuggagePrice(Double luggagePrice) {
        this.luggagePrice = luggagePrice;
    }
    
    public Double getWeightPrice() {
        return weightPrice;
    }
    
    public void setWeightPrice(Double weightPrice) {
        this.weightPrice = weightPrice;
    }
    
    public String getStatus() {
        return status == null ? "On Time" : status;
    }
    
    public void setStatus(String status) {
        this.status = status == null ? "On Time" : status;
    }
    
    // Helper methods
    public Double getMinPrice() {
        if (economyPrice == null) return null;
        double min = economyPrice;
        if (businessPrice != null && businessPrice < min) min = businessPrice;
        if (firstPrice != null && firstPrice < min) min = firstPrice;
        return min;
    }
    
    public Long getDurationMinutes() {
        if (depDatetime == null || arrDatetime == null) return null;
        return java.time.Duration.between(depDatetime, arrDatetime).toMinutes();
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
    
    @PrePersist
    @PreUpdate
    public void setDefaultStatus() {
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "On Time";
        }
    }
}
