package com.skynet.admin.osgi.impl;

import com.skynet.admin.osgi.api.AdminService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private static final String DB_URL = "jdbc:sqlite:adminOsgi.db";

    public AdminServiceImpl() {
        initDb();
    }

    private void initDb() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS audit (id INTEGER PRIMARY KEY, action TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS feedback (id INTEGER PRIMARY KEY, user TEXT, comment TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String generateSalesReport() {
        logAction("Generated Sales Report");
        // In a real OSGi setup, we would consume PaymentService from the OSGi registry here.
        // For simplicity, we return a mock string.
        return "Sales Report: Total Revenue calculated from linked bundles.";
    }

    @Override
    public List<String> getPassengerList() {
        logAction("Viewed Passenger List");
        return List.of("John Doe", "Jane Smith", "Skynet Admin"); // Mock data or connect to DB
    }

    @Override
    public void submitFeedback(String user, String comment) {
        String sql = "INSERT INTO feedback (user, comment) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user);
            pstmt.setString(2, comment);
            pstmt.executeUpdate();
            logAction("Feedback submitted by " + user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getFeedback() {
        List<String> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM feedback")) {
            while (rs.next()) {
                list.add(rs.getString("user") + ": " + rs.getString("comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<String> getAuditLogs() {
        List<String> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM audit")) {
            while (rs.next()) {
                list.add(rs.getString("action"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void logAction(String action) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO audit (action) VALUES (?)")) {
            pstmt.setString(1, action + " at " + new java.util.Date());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}