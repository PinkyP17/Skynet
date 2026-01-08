package com.skynet.flightcatalog.osgi.dao;

import com.skynet.flightcatalog.osgi.model.Flight;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Flight Data Access Object (DAO)
 * 
 * Handles database operations for Flight Catalog OSGi Bundle
 */
public class FlightDao {
    
    // Use absolute path to database file
    private static final String DB_URL = "jdbc:sqlite:D:/UM DEGREE/SEM 7/CBSE GA/AA/Skynet/ogsi-implentation/flight-catalog-osgi/applicationDataBase.db";
    
    /**
     * Get database connection
     */
    private Connection getConnection() throws SQLException {
        try {
            // Explicitly load SQLite driver (embedded in bundle)
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("[FlightDao] SQLite driver not found: " + e.getMessage());
            throw new SQLException("SQLite JDBC driver not found", e);
        }
        return DriverManager.getConnection(DB_URL);
    }
    
    /**
     * Initialize flight status column if it doesn't exist
     * This ensures backward compatibility with existing database
     */
    private void ensureStatusColumnExists() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Check if status column exists
            ResultSet rs = conn.getMetaData().getColumns(null, null, "flights", "status");
            if (!rs.next()) {
                // Column doesn't exist, add it
                stmt.executeUpdate("ALTER TABLE flights ADD COLUMN status TEXT DEFAULT 'ON_TIME'");
                System.out.println("[FlightDao] Added status column to flights table");
            }
        } catch (SQLException e) {
            // Column might already exist or table doesn't exist - ignore
            System.out.println("[FlightDao] Status column check: " + e.getMessage());
        }
    }
    
    /**
     * Get flight by ID
     */
    public Flight findById(Integer id) {
        ensureStatusColumnExists();
        String sql = "SELECT * FROM flights WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToFlight(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error finding flight: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all flights
     */
    public List<Flight> findAll() {
        ensureStatusColumnExists();
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights ORDER BY dep_datetime DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                flights.add(mapResultSetToFlight(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error finding all flights: " + e.getMessage());
        }
        
        return flights;
    }
    
    /**
     * Create new flight
     */
    public Flight save(Flight flight) {
        ensureStatusColumnExists();
        String sql = "INSERT INTO flights (dep_datetime, arr_datetime, first_price, business_price, " +
                     "economy_price, luggage_price, weight_price, id_airline, dep_airport, arr_airport, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, flight.getDepDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            stmt.setString(2, flight.getArrDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            stmt.setDouble(3, flight.getFirstPrice() != null ? flight.getFirstPrice() : 0.0);
            stmt.setDouble(4, flight.getBusinessPrice() != null ? flight.getBusinessPrice() : 0.0);
            stmt.setDouble(5, flight.getEconomyPrice() != null ? flight.getEconomyPrice() : 0.0);
            stmt.setDouble(6, flight.getLuggagePrice() != null ? flight.getLuggagePrice() : 0.0);
            stmt.setDouble(7, flight.getWeightPrice() != null ? flight.getWeightPrice() : 0.0);
            stmt.setInt(8, flight.getAirlineId());
            stmt.setInt(9, flight.getDepAirportId());
            stmt.setInt(10, flight.getArrAirportId());
            stmt.setString(11, flight.getStatus());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    flight.setId(generatedKeys.getInt(1));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error saving flight: " + e.getMessage());
            return null;
        }
        
        return flight;
    }
    
    /**
     * Update flight
     */
    public Flight update(Flight flight) {
        ensureStatusColumnExists();
        String sql = "UPDATE flights SET dep_datetime = ?, arr_datetime = ?, first_price = ?, " +
                     "business_price = ?, economy_price = ?, luggage_price = ?, weight_price = ?, " +
                     "id_airline = ?, dep_airport = ?, arr_airport = ?, status = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, flight.getDepDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            stmt.setString(2, flight.getArrDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            stmt.setDouble(3, flight.getFirstPrice() != null ? flight.getFirstPrice() : 0.0);
            stmt.setDouble(4, flight.getBusinessPrice() != null ? flight.getBusinessPrice() : 0.0);
            stmt.setDouble(5, flight.getEconomyPrice() != null ? flight.getEconomyPrice() : 0.0);
            stmt.setDouble(6, flight.getLuggagePrice() != null ? flight.getLuggagePrice() : 0.0);
            stmt.setDouble(7, flight.getWeightPrice() != null ? flight.getWeightPrice() : 0.0);
            stmt.setInt(8, flight.getAirlineId());
            stmt.setInt(9, flight.getDepAirportId());
            stmt.setInt(10, flight.getArrAirportId());
            stmt.setString(11, flight.getStatus());
            stmt.setInt(12, flight.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                return flight;
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error updating flight: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Delete flight
     */
    public boolean delete(Integer id) {
        String sql = "DELETE FROM flights WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error deleting flight: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Search flights by date
     */
    public List<Flight> findByDate(LocalDateTime date) {
        ensureStatusColumnExists();
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE DATE(dep_datetime) = DATE(?) ORDER BY dep_datetime";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                flights.add(mapResultSetToFlight(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error searching flights by date: " + e.getMessage());
        }
        
        return flights;
    }
    
    /**
     * Search flights by route
     */
    public List<Flight> findByRoute(Integer depAirportId, Integer arrAirportId) {
        ensureStatusColumnExists();
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE dep_airport = ? AND arr_airport = ? ORDER BY dep_datetime";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, depAirportId);
            stmt.setInt(2, arrAirportId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                flights.add(mapResultSetToFlight(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error searching flights by route: " + e.getMessage());
        }
        
        return flights;
    }
    
    /**
     * Search flights by route and date
     */
    public List<Flight> findByRouteAndDate(Integer depAirportId, Integer arrAirportId, LocalDateTime date) {
        ensureStatusColumnExists();
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE dep_airport = ? AND arr_airport = ? AND DATE(dep_datetime) = DATE(?) ORDER BY dep_datetime";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, depAirportId);
            stmt.setInt(2, arrAirportId);
            stmt.setString(3, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                flights.add(mapResultSetToFlight(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error searching flights by route and date: " + e.getMessage());
        }
        
        return flights;
    }
    
    /**
     * Check for duplicate flight (same route and date)
     */
    public boolean isDuplicate(Flight flight, Integer excludeId) {
        ensureStatusColumnExists();
        String sql = "SELECT COUNT(*) FROM flights WHERE dep_airport = ? AND arr_airport = ? AND DATE(dep_datetime) = DATE(?)";
        if (excludeId != null) {
            sql += " AND id != ?";
        }
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, flight.getDepAirportId());
            stmt.setInt(2, flight.getArrAirportId());
            stmt.setString(3, flight.getDepDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            if (excludeId != null) {
                stmt.setInt(4, excludeId);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error checking duplicate: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get flights by status
     */
    public List<Flight> findByStatus(String status) {
        ensureStatusColumnExists();
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE status = ? ORDER BY dep_datetime";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                flights.add(mapResultSetToFlight(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error finding flights by status: " + e.getMessage());
        }
        
        return flights;
    }
    
    /**
     * Update flight status
     */
    public boolean updateStatus(Integer id, String status) {
        ensureStatusColumnExists();
        String sql = "UPDATE flights SET status = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("[FlightDao] Error updating flight status: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to Flight object
     */
    private Flight mapResultSetToFlight(ResultSet rs) throws SQLException {
        Flight flight = new Flight();
        flight.setId(rs.getInt("id"));
        flight.setDepDatetime(LocalDateTime.parse(rs.getString("dep_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        flight.setArrDatetime(LocalDateTime.parse(rs.getString("arr_datetime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        flight.setFirstPrice(rs.getDouble("first_price"));
        flight.setBusinessPrice(rs.getDouble("business_price"));
        flight.setEconomyPrice(rs.getDouble("economy_price"));
        flight.setLuggagePrice(rs.getDouble("luggage_price"));
        flight.setWeightPrice(rs.getDouble("weight_price"));
        flight.setAirlineId(rs.getInt("id_airline"));
        flight.setDepAirportId(rs.getInt("dep_airport"));
        flight.setArrAirportId(rs.getInt("arr_airport"));
        
        // Handle status column (might not exist in old databases)
        try {
            String status = rs.getString("status");
            flight.setStatus(status != null ? status : "ON_TIME");
        } catch (SQLException e) {
            flight.setStatus("ON_TIME");
        }
        
        return flight;
    }
}
