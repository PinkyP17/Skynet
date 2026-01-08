package com.skynet.flightcatalog.osgi.model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Flight Model for Flight Catalog OSGi Bundle
 */
public class Flight {
    private Integer id;
    private LocalDateTime depDatetime;
    private LocalDateTime arrDatetime;
    private Double firstPrice;
    private Double businessPrice;
    private Double economyPrice;
    private Double luggagePrice;
    private Double weightPrice;
    private Integer depAirportId;
    private Integer arrAirportId;
    private Integer airlineId;
    private String status; // ON_TIME, DELAYED, CANCELLED, BOARDING, DEPARTED, ARRIVED

    public Flight() {
        this.status = "ON_TIME";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Integer airlineId) {
        this.airlineId = airlineId;
    }

    public String getStatus() {
        return status != null ? status : "ON_TIME";
    }

    public void setStatus(String status) {
        this.status = (status == null || status.isBlank()) ? "ON_TIME" : status;
    }

    /**
     * Get minimum price across all classes
     */
    public Double getMinPrice() {
        double min = Double.MAX_VALUE;
        if (economyPrice != null && economyPrice > 0) min = Math.min(min, economyPrice);
        if (businessPrice != null && businessPrice > 0) min = Math.min(min, businessPrice);
        if (firstPrice != null && firstPrice > 0) min = Math.min(min, firstPrice);
        return min == Double.MAX_VALUE ? 0.0 : min;
    }

    /**
     * Get flight duration in minutes
     */
    public Long getDurationMinutes() {
        if (depDatetime == null || arrDatetime == null) return 0L;
        return Duration.between(depDatetime, arrDatetime).toMinutes();
    }

    /**
     * Get status color code (for UI)
     */
    public String getStatusColor() {
        String s = getStatus().toUpperCase();
        switch (s) {
            case "ON_TIME": return "#4CAF50";     // Green
            case "DELAYED": return "#FF9800";     // Orange
            case "CANCELLED": return "#F44336";   // Red
            case "BOARDING": return "#2196F3";    // Blue
            case "DEPARTED": return "#9C27B0";    // Purple
            case "ARRIVED": return "#00BCD4";     // Cyan
            default: return "#808080";            // Gray
        }
    }
}
