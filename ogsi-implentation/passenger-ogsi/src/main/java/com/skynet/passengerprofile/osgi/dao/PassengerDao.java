package com.skynet.passengerprofile.osgi.dao;

import com.skynet.passengerprofile.osgi.model.Passenger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Passenger Data Access Object (DAO)
 * 
 * Handles database operations for Passenger Profile OSGi Bundle
 */
public class PassengerDao {
    
    private static final String DB_URL = "jdbc:sqlite:applicationDataBase.db";
    
    /**
     * Get database connection
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    /**
     * Get passenger by ID
     */
    public Passenger findById(Long id) {
        String sql = "SELECT * FROM passengers WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPassenger(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("[PassengerDao] Error finding passenger: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get all passengers
     */
    public List<Passenger> findAll() {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM passengers";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                passengers.add(mapResultSetToPassenger(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[PassengerDao] Error finding all passengers: " + e.getMessage());
        }
        
        return passengers;
    }
    
    /**
     * Create new passenger
     */
    public Passenger save(Passenger passenger) {
        String sql = "INSERT INTO passengers (firstName, lastName, birthDate, gender, country, " +
                     "profilePicture, phone_number, address, nationality, meal_preference, profile_completeness) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, passenger.getFirstName());
            stmt.setString(2, passenger.getLastName());
            stmt.setString(3, passenger.getBirthDate());
            stmt.setString(4, passenger.getGender());
            stmt.setString(5, passenger.getCountry());
            stmt.setString(6, passenger.getProfilePicture());
            stmt.setString(7, passenger.getPhoneNumber());
            stmt.setString(8, passenger.getAddress());
            stmt.setString(9, passenger.getNationality());
            stmt.setString(10, passenger.getMealPreference());
            stmt.setInt(11, passenger.getProfileCompleteness() != null ? passenger.getProfileCompleteness() : 0);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    passenger.setId(generatedKeys.getLong(1));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("[PassengerDao] Error saving passenger: " + e.getMessage());
        }
        
        return passenger;
    }
    
    /**
     * Update passenger
     */
    public Passenger update(Long id, Passenger passenger) {
        String sql = "UPDATE passengers SET firstName = ?, lastName = ?, birthDate = ?, gender = ?, " +
                     "country = ?, profilePicture = ?, phone_number = ?, address = ?, nationality = ?, " +
                     "meal_preference = ?, profile_completeness = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, passenger.getFirstName());
            stmt.setString(2, passenger.getLastName());
            stmt.setString(3, passenger.getBirthDate());
            stmt.setString(4, passenger.getGender());
            stmt.setString(5, passenger.getCountry());
            stmt.setString(6, passenger.getProfilePicture());
            stmt.setString(7, passenger.getPhoneNumber());
            stmt.setString(8, passenger.getAddress());
            stmt.setString(9, passenger.getNationality());
            stmt.setString(10, passenger.getMealPreference());
            stmt.setInt(11, passenger.getProfileCompleteness() != null ? passenger.getProfileCompleteness() : 0);
            stmt.setLong(12, id);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                passenger.setId(id);
                return passenger;
            }
            
        } catch (SQLException e) {
            System.err.println("[PassengerDao] Error updating passenger: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Delete passenger
     */
    public boolean delete(Long id) {
        String sql = "DELETE FROM passengers WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("[PassengerDao] Error deleting passenger: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Search passengers by name
     */
    public List<Passenger> searchByName(String keyword) {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM passengers WHERE firstName LIKE ? OR lastName LIKE ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                passengers.add(mapResultSetToPassenger(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("[PassengerDao] Error searching passengers: " + e.getMessage());
        }
        
        return passengers;
    }
    
    /**
     * Get total count
     */
    public long count() {
        String sql = "SELECT COUNT(*) FROM passengers";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            System.err.println("[PassengerDao] Error counting passengers: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Check if passenger exists
     */
    public boolean exists(Long id) {
        String sql = "SELECT COUNT(*) FROM passengers WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("[PassengerDao] Error checking passenger existence: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to Passenger object
     */
    private Passenger mapResultSetToPassenger(ResultSet rs) throws SQLException {
        Passenger passenger = new Passenger();
        passenger.setId(rs.getLong("id"));
        passenger.setFirstName(rs.getString("firstName"));
        passenger.setLastName(rs.getString("lastName"));
        passenger.setBirthDate(rs.getString("birthDate"));
        passenger.setGender(rs.getString("gender"));
        passenger.setCountry(rs.getString("country"));
        passenger.setProfilePicture(rs.getString("profilePicture"));
        passenger.setPhoneNumber(rs.getString("phone_number"));
        passenger.setAddress(rs.getString("address"));
        passenger.setNationality(rs.getString("nationality"));
        passenger.setMealPreference(rs.getString("meal_preference"));
        passenger.setProfileCompleteness(rs.getInt("profile_completeness"));
        return passenger;
    }
}
