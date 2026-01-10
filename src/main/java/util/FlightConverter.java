package util;

import com.fasterxml.jackson.databind.JsonNode;
import models.Flight;
import models.Airport;
import data.AirportDao;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FlightConverter {
    
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    /**
     * Convert Spring Boot Flight JSON to main app Flight model
     */
    public static Flight fromJsonNode(JsonNode jsonNode) {
        Flight flight = new Flight();
        
        if (jsonNode.has("id")) {
            flight.setId(jsonNode.get("id").asInt());
        }
        
        if (jsonNode.has("airlineId")) {
            flight.setAirline(jsonNode.get("airlineId").asInt());
        }
        
        if (jsonNode.has("depAirportId")) {
            int depAirportId = jsonNode.get("depAirportId").asInt();
            AirportDao airportDao = new AirportDao();
            Airport depAirport = airportDao.read(depAirportId);
            flight.setDepAirport(depAirport);
        }
        
        if (jsonNode.has("arrAirportId")) {
            int arrAirportId = jsonNode.get("arrAirportId").asInt();
            AirportDao airportDao = new AirportDao();
            Airport arrAirport = airportDao.read(arrAirportId);
            flight.setArrAirport(arrAirport);
        }
        
        if (jsonNode.has("depDatetime")) {
            String depDateTimeStr = jsonNode.get("depDatetime").asText();
            flight.setDepDatetime(LocalDateTime.parse(depDateTimeStr, FORMATTER));
        }
        
        if (jsonNode.has("arrDatetime")) {
            String arrDateTimeStr = jsonNode.get("arrDatetime").asText();
            flight.setArrDatetime(LocalDateTime.parse(arrDateTimeStr, FORMATTER));
        }
        
        if (jsonNode.has("firstPrice")) {
            flight.setFirstPrice(jsonNode.get("firstPrice").asDouble());
        }
        
        if (jsonNode.has("businessPrice")) {
            flight.setBusinessPrice(jsonNode.get("businessPrice").asDouble());
        }
        
        if (jsonNode.has("economyPrice")) {
            flight.setEconomyPrice(jsonNode.get("economyPrice").asDouble());
        }
        
        if (jsonNode.has("luggagePrice")) {
            flight.setLuggagePrice(jsonNode.get("luggagePrice").asDouble());
        }
        
        if (jsonNode.has("weightPrice")) {
            flight.setWeightPrice(jsonNode.get("weightPrice").asDouble());
        }
        
        if (jsonNode.has("status")) {
            flight.setStatus(jsonNode.get("status").asText());
        }
        
        return flight;
    }
    
    /**
     * Convert main app Flight to Spring Boot Flight JSON format
     */
    public static String toJsonString(Flight flight) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (flight.getId() > 0) {
            json.append("\"id\":").append(flight.getId()).append(",");
        }
        json.append("\"airlineId\":").append(flight.getAirline().getId()).append(",");
        json.append("\"depAirportId\":").append(flight.getDepAirport().getId()).append(",");
        json.append("\"arrAirportId\":").append(flight.getArrAirport().getId()).append(",");
        json.append("\"depDatetime\":\"").append(flight.getDepDatetime().format(FORMATTER)).append("\",");
        json.append("\"arrDatetime\":\"").append(flight.getArrDatetime().format(FORMATTER)).append("\",");
        json.append("\"firstPrice\":").append(flight.getFirstPrice()).append(",");
        json.append("\"businessPrice\":").append(flight.getBusinessPrice()).append(",");
        json.append("\"economyPrice\":").append(flight.getEconomyPrice()).append(",");
        json.append("\"luggagePrice\":").append(flight.getLuggagePrice()).append(",");
        json.append("\"weightPrice\":").append(flight.getWeightPrice()).append(",");
        json.append("\"status\":\"").append(flight.getStatus() != null ? flight.getStatus() : "On Time").append("\"");
        json.append("}");
        return json.toString();
    }
}
