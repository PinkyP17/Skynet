package com.skynet.flightcatalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlightRequestDTO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("airlineId")
    private Integer airlineId;
    
    @JsonProperty("depAirportId")
    private Integer depAirportId;
    
    @JsonProperty("arrAirportId")
    private Integer arrAirportId;
    
    @JsonProperty("depDatetime")
    private String depDatetime;
    
    @JsonProperty("arrDatetime")
    private String arrDatetime;
    
    @JsonProperty("firstPrice")
    private Double firstPrice;
    
    @JsonProperty("businessPrice")
    private Double businessPrice;
    
    @JsonProperty("economyPrice")
    private Double economyPrice;
    
    @JsonProperty("luggagePrice")
    private Double luggagePrice;
    
    @JsonProperty("weightPrice")
    private Double weightPrice;
    
    @JsonProperty("status")
    private String status;
    
    // Convert to Flight entity
    public com.skynet.flightcatalog.model.Flight toFlight() {
        com.skynet.flightcatalog.model.Flight flight = new com.skynet.flightcatalog.model.Flight();
        if (id != null && id > 0) {
            flight.setId(id);
        }
        flight.setAirlineId(airlineId);
        flight.setDepAirportId(depAirportId);
        flight.setArrAirportId(arrAirportId);
        
        if (depDatetime != null) {
            flight.setDepDatetime(LocalDateTime.parse(depDatetime, FORMATTER));
        }
        if (arrDatetime != null) {
            flight.setArrDatetime(LocalDateTime.parse(arrDatetime, FORMATTER));
        }
        
        flight.setFirstPrice(firstPrice);
        flight.setBusinessPrice(businessPrice);
        flight.setEconomyPrice(economyPrice);
        flight.setLuggagePrice(luggagePrice);
        flight.setWeightPrice(weightPrice);
        flight.setStatus(status != null ? status : "On Time");
        
        return flight;
    }
    
    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public Integer getAirlineId() { return airlineId; }
    public void setAirlineId(Integer airlineId) { this.airlineId = airlineId; }
    
    public Integer getDepAirportId() { return depAirportId; }
    public void setDepAirportId(Integer depAirportId) { this.depAirportId = depAirportId; }
    
    public Integer getArrAirportId() { return arrAirportId; }
    public void setArrAirportId(Integer arrAirportId) { this.arrAirportId = arrAirportId; }
    
    public String getDepDatetime() { return depDatetime; }
    public void setDepDatetime(String depDatetime) { this.depDatetime = depDatetime; }
    
    public String getArrDatetime() { return arrDatetime; }
    public void setArrDatetime(String arrDatetime) { this.arrDatetime = arrDatetime; }
    
    public Double getFirstPrice() { return firstPrice; }
    public void setFirstPrice(Double firstPrice) { this.firstPrice = firstPrice; }
    
    public Double getBusinessPrice() { return businessPrice; }
    public void setBusinessPrice(Double businessPrice) { this.businessPrice = businessPrice; }
    
    public Double getEconomyPrice() { return economyPrice; }
    public void setEconomyPrice(Double economyPrice) { this.economyPrice = economyPrice; }
    
    public Double getLuggagePrice() { return luggagePrice; }
    public void setLuggagePrice(Double luggagePrice) { this.luggagePrice = luggagePrice; }
    
    public Double getWeightPrice() { return weightPrice; }
    public void setWeightPrice(Double weightPrice) { this.weightPrice = weightPrice; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
