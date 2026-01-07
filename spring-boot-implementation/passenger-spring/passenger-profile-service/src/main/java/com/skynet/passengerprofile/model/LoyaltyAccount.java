package com.skynet.passengerprofile.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * LoyaltyAccount Entity - represents passenger loyalty/rewards program
 * 
 * Maps to the 'loyalty_accounts' table in the database
 * 
 * Enhancement: Loyalty Tier Visualizer
 * - Silver: 0 - 4,999 points
 * - Gold: 5,000 - 9,999 points
 * - Diamond: 10,000+ points
 */
@Entity
@Table(name = "loyalty_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_passenger", insertable = false, updatable = false)
    private Long passengerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passenger", nullable = false, unique = true)
    @JsonIgnore
    private Passenger passenger;

    @Column(name = "points_balance")
    private Integer pointsBalance = 0;

    @Column(name = "tier")
    private String tier = "Silver"; // Silver, Gold, Diamond

    @Column(name = "tier_expiry_date")
    private String tierExpiryDate; // Format: "YYYY-MM-DD"

    @Column(name = "total_points_earned")
    private Integer totalPointsEarned = 0;

    @Column(name = "total_points_redeemed")
    private Integer totalPointsRedeemed = 0;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "last_updated")
    private String lastUpdated;

    // Tier thresholds
    public static final int SILVER_THRESHOLD = 0;
    public static final int GOLD_THRESHOLD = 5000;
    public static final int DIAMOND_THRESHOLD = 10000;

    // Helper methods for Loyalty Tier Visualizer Enhancement

    /**
     * Calculate and update tier based on current points balance
     */
    public void updateTier() {
        if (pointsBalance >= DIAMOND_THRESHOLD) {
            this.tier = "Diamond";
        } else if (pointsBalance >= GOLD_THRESHOLD) {
            this.tier = "Gold";
        } else {
            this.tier = "Silver";
        }
    }

    /**
     * Get the next tier
     * @return name of next tier, or "Maximum" if already Diamond
     */
    public String getNextTier() {
        switch (tier) {
            case "Silver":
                return "Gold";
            case "Gold":
                return "Diamond";
            case "Diamond":
                return "Maximum";
            default:
                return "Unknown";
        }
    }

    /**
     * Get points needed to reach next tier
     * @return points needed, or 0 if already at max tier
     */
    public int getPointsToNextTier() {
        switch (tier) {
            case "Silver":
                return GOLD_THRESHOLD - pointsBalance;
            case "Gold":
                return DIAMOND_THRESHOLD - pointsBalance;
            case "Diamond":
                return 0; // Already at max
            default:
                return 0;
        }
    }

    /**
     * Get progress percentage to next tier
     * Enhancement: Visual progress bar data
     * @return percentage (0-100)
     */
    public int getProgressToNextTier() {
        switch (tier) {
            case "Silver":
                // Progress from 0 to 5000
                return Math.min(100, (int)((pointsBalance / (double)GOLD_THRESHOLD) * 100));
            case "Gold":
                // Progress from 5000 to 10000
                int goldProgress = pointsBalance - GOLD_THRESHOLD;
                int goldRange = DIAMOND_THRESHOLD - GOLD_THRESHOLD;
                return Math.min(100, (int)((goldProgress / (double)goldRange) * 100));
            case "Diamond":
                return 100; // Already at max
            default:
                return 0;
        }
    }

    /**
     * Add points and update tier
     * @param points points to add
     */
    public void addPoints(int points) {
        this.pointsBalance += points;
        this.totalPointsEarned += points;
        updateTier();
    }

    /**
     * Redeem points and update tier
     * @param points points to redeem
     * @return true if successful, false if insufficient balance
     */
    public boolean redeemPoints(int points) {
        if (pointsBalance >= points) {
            this.pointsBalance -= points;
            this.totalPointsRedeemed += points;
            updateTier();
            return true;
        }
        return false;
    }

    /**
     * Get tier color for UI visualization
     * @return hex color code
     */
    public String getTierColor() {
        switch (tier) {
            case "Silver":
                return "#C0C0C0"; // Silver color
            case "Gold":
                return "#FFD700"; // Gold color
            case "Diamond":
                return "#B9F2FF"; // Diamond/cyan color
            default:
                return "#808080"; // Gray
        }
    }

    /**
     * Get tier benefits description
     * @return benefits as a string
     */
    public String getTierBenefits() {
        switch (tier) {
            case "Silver":
                return "5% discount on flights, Priority check-in";
            case "Gold":
                return "10% discount on flights, Lounge access, Extra baggage";
            case "Diamond":
                return "15% discount on flights, Premium lounge, Free upgrades, 24/7 support";
            default:
                return "No benefits";
        }
    }
}
