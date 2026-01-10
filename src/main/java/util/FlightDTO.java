package util;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.format.DateTimeFormatter;

public class FlightDTO {
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
    
    // Constructor
    public FlightDTO() {
    }
    
    // Factory method from Flight model
    public static FlightDTO fromFlight(models.Flight flight) {
        FlightDTO dto = new FlightDTO();
        if (flight.getId() > 0) {
            dto.id = flight.getId();
        }
        dto.airlineId = flight.getAirline().getId();
        dto.depAirportId = flight.getDepAirport().getId();
        dto.arrAirportId = flight.getArrAirport().getId();
        dto.depDatetime = flight.getDepDatetime().format(FORMATTER);
        dto.arrDatetime = flight.getArrDatetime().format(FORMATTER);
        dto.firstPrice = flight.getFirstPrice();
        dto.businessPrice = flight.getBusinessPrice();
        dto.economyPrice = flight.getEconomyPrice();
        dto.luggagePrice = flight.getLuggagePrice();
        dto.weightPrice = flight.getWeightPrice();
        dto.status = flight.getStatus() != null ? flight.getStatus() : "On Time";
        return dto;
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
