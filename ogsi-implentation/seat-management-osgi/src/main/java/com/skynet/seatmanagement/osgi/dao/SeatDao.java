package com.skynet.seatmanagement.osgi.dao;

import com.skynet.seatmanagement.osgi.model.Seat;
import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Seat Management
 * Handles database operations for seats
 */
public class SeatDao {
    
    private static final String DB_PATH = System.getProperty("user.home") + "/.Skynet/applicationDataBase.db";
    
    // Static initializer to load SQLite JDBC driver
    static {
        try {
            // Explicitly load the SQLite JDBC driver for OSGi
            Class.forName("org.sqlite.JDBC");
            // Register the driver
            DriverManager.registerDriver(new JDBC());
            System.out.println("[SeatDao] SQLite JDBC driver loaded successfully");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("[SeatDao] ERROR: Failed to load SQLite JDBC driver: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }
    
    public Seat findById(Integer id) {
        String sql = "SELECT * FROM seats WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSeat(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding seat by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Seat> findAll() {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats ORDER BY row, \"column\"";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                seats.add(mapResultSetToSeat(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all seats: " + e.getMessage());
            e.printStackTrace();
        }
        return seats;
    }
    
    public List<Seat> findByType(String type) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE type = ? ORDER BY row, \"column\"";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                seats.add(mapResultSetToSeat(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding seats by type: " + e.getMessage());
            e.printStackTrace();
        }
        return seats;
    }
    
    public Seat findByLabel(String column, Integer row) {
        String sql = "SELECT * FROM seats WHERE \"column\" = ? AND row = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, column);
            stmt.setInt(2, row);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSeat(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error finding seat by label: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean isSeatReserved(Integer seatId, Integer flightId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE id_seat = ? AND id_flight = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, seatId);
            stmt.setInt(2, flightId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking seat reservation: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    public int countReservationsForFlight(Integer flightId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE id_flight = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, flightId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting reservations: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public long count() {
        String sql = "SELECT COUNT(*) FROM seats";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting seats: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean exists(Integer id) {
        return findById(id) != null;
    }
    
    private Seat mapResultSetToSeat(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setId(rs.getInt("id"));
        seat.setColumn(rs.getString("column"));
        seat.setRow(rs.getInt("row"));
        seat.setType(rs.getString("type"));
        return seat;
    }
}
