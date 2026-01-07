package com.skynet.passengerprofile.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * LoyaltyAccount Entity - represents passenger loyalty/rewards program
 *
 * Maps to the 'loyalty_accounts' table in the database
 */
@Entity
@Table(name = "loyalty_accounts")
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
    private String tier = "Silver";

    @Column(name = "tier_expiry_date")
    private String tierExpiryDate;

    @Column(name = "total_points_earned")
    private Integer totalPointsEarned = 0;

    @Column(name = "total_points_redeemed")
    private Integer totalPointsRedeemed = 0;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "last_updated")
    private String lastUpdated;

    public static final int SILVER_THRESHOLD = 0;
    public static final int GOLD_THRESHOLD = 5000;
    public static final int DIAMOND_THRESHOLD = 10000;

    // Constructors
    public LoyaltyAccount() {}

    public LoyaltyAccount(Long id, Long passengerId, Integer pointsBalance, String tier,
                          String tierExpiryDate, Integer totalPointsEarned, Integer totalPointsRedeemed,
                          String createdDate, String lastUpdated) {
        this.id = id;
        this.passengerId = passengerId;
        this.pointsBalance = pointsBalance;
        this.tier = tier;
        this.tierExpiryDate = tierExpiryDate;
        this.totalPointsEarned = totalPointsEarned;
        this.totalPointsRedeemed = totalPointsRedeemed;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Passenger getPassenger() { return passenger; }
    public void setPassenger(Passenger passenger) { this.passenger = passenger; }

    public Integer getPointsBalance() { return pointsBalance; }
    public void setPointsBalance(Integer pointsBalance) { this.pointsBalance = pointsBalance; }

    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }

    public String getTierExpiryDate() { return tierExpiryDate; }
    public void setTierExpiryDate(String tierExpiryDate) { this.tierExpiryDate = tierExpiryDate; }

    public Integer getTotalPointsEarned() { return totalPointsEarned; }
    public void setTotalPointsEarned(Integer totalPointsEarned) { this.totalPointsEarned = totalPointsEarned; }

    public Integer getTotalPointsRedeemed() { return totalPointsRedeemed; }
    public void setTotalPointsRedeemed(Integer totalPointsRedeemed) { this.totalPointsRedeemed = totalPointsRedeemed; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    // Helper methods
    public void updateTier() {
        if (pointsBalance >= DIAMOND_THRESHOLD) {
            this.tier = "Diamond";
        } else if (pointsBalance >= GOLD_THRESHOLD) {
            this.tier = "Gold";
        } else {
            this.tier = "Silver";
        }
    }

    public String getNextTier() {
        switch (tier) {
            case "Silver": return "Gold";
            case "Gold": return "Diamond";
            case "Diamond": return "Maximum";
            default: return "Unknown";
        }
    }

    public int getPointsToNextTier() {
        switch (tier) {
            case "Silver": return GOLD_THRESHOLD - pointsBalance;
            case "Gold": return DIAMOND_THRESHOLD - pointsBalance;
            case "Diamond": return 0;
            default: return 0;
        }
    }

    public int getProgressToNextTier() {
        switch (tier) {
            case "Silver":
                return Math.min(100, (int)((pointsBalance / (double)GOLD_THRESHOLD) * 100));
            case "Gold":
                int goldProgress = pointsBalance - GOLD_THRESHOLD;
                int goldRange = DIAMOND_THRESHOLD - GOLD_THRESHOLD;
                return Math.min(100, (int)((goldProgress / (double)goldRange) * 100));
            case "Diamond":
                return 100;
            default:
                return 0;
        }
    }

    public void addPoints(int points) {
        this.pointsBalance += points;
        this.totalPointsEarned += points;
        updateTier();
    }

    public boolean redeemPoints(int points) {
        if (pointsBalance >= points) {
            this.pointsBalance -= points;
            this.totalPointsRedeemed += points;
            updateTier();
            return true;
        }
        return false;
    }

    public String getTierColor() {
        switch (tier) {
            case "Silver": return "#C0C0C0";
            case "Gold": return "#FFD700";
            case "Diamond": return "#B9F2FF";
            default: return "#808080";
        }
    }

    public String getTierBenefits() {
        switch (tier) {
            case "Silver": return "5% discount on flights, Priority check-in";
            case "Gold": return "10% discount on flights, Lounge access, Extra baggage";
            case "Diamond": return "15% discount on flights, Premium lounge, Free upgrades, 24/7 support";
            default: return "No benefits";
        }
    }
}
