package com.skynet.payment.osgi.dao;

import com.skynet.payment.osgi.model.Payment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Payments
 */
public class PaymentDAO {

    private static final String DB_URL = "jdbc:sqlite:paymentDatabase.db";

    public PaymentDAO() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS payments (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "amount REAL, " +
                     "currency TEXT, " +
                     "method TEXT, " +
                     "status TEXT, " +
                     "transactionDate TEXT, " +
                     "bookingId INTEGER)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Payment database initialized");
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public Payment save(Payment payment) {
        String sql = "INSERT INTO payments (amount, currency, method, status, transactionDate, bookingId) " +
                     "VALUES (?, ?, ?, ?, datetime('now'), ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDouble(1, payment.getAmount());
            stmt.setString(2, payment.getCurrency());
            stmt.setString(3, payment.getMethod());
            stmt.setString(4, payment.getStatus());
            stmt.setLong(5, payment.getBookingId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        payment.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Error saving payment: " + e.getMessage());
        }
        return payment;
    }

    public Payment findById(Long id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPayment(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding payment: " + e.getMessage());
        }
        return null;
    }

    public boolean updateStatus(Long id, String status) {
        String sql = "UPDATE payments SET status = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setLong(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating status: " + e.getMessage());
        }
        return false;
    }

    public List<Payment> findAll() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(mapResultSetToPayment(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error finding all: " + e.getMessage());
        }
        return list;
    }

    private Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setId(rs.getLong("id"));
        p.setAmount(rs.getDouble("amount"));
        p.setCurrency(rs.getString("currency"));
        p.setMethod(rs.getString("method"));
        p.setStatus(rs.getString("status"));
        p.setTransactionDate(rs.getString("transactionDate"));
        p.setBookingId(rs.getLong("bookingId"));
        return p;
    }
}
