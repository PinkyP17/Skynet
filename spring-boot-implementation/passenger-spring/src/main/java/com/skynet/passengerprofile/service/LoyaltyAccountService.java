package com.skynet.passengerprofile.service;

import com.skynet.passengerprofile.model.LoyaltyAccount;
import com.skynet.passengerprofile.repository.LoyaltyAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * LoyaltyAccountService - Business logic for Loyalty Points management
 * 
 * Functionality 4: Manage Loyalty Points
 * Enhancement 2: Loyalty Tier Visualizer
 */
@Service
@Transactional
public class LoyaltyAccountService {

    @Autowired
    private LoyaltyAccountRepository loyaltyAccountRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ========================================
    // FUNCTIONALITY 4: MANAGE LOYALTY POINTS
    // ========================================

    /**
     * Get loyalty account by ID
     * @param id account ID
     * @return LoyaltyAccount or null
     */
    public LoyaltyAccount getAccountById(Long id) {
        return loyaltyAccountRepository.findById(id).orElse(null);
    }

    /**
     * Get loyalty account by passenger ID
     * @param passengerId passenger ID
     * @return LoyaltyAccount or null
     */
    public LoyaltyAccount getAccountByPassengerId(Long passengerId) {
        return loyaltyAccountRepository.findByPassengerId(passengerId).orElse(null);
    }

    /**
     * Create new loyalty account for a passenger
     * @param passengerId passenger ID
     * @return created loyalty account
     */
    public LoyaltyAccount createLoyaltyAccount(Long passengerId) {
        // Check if account already exists
        if (loyaltyAccountRepository.existsByPassengerId(passengerId)) {
            throw new IllegalArgumentException("Passenger already has a loyalty account");
        }
        
        LoyaltyAccount account = new LoyaltyAccount();
        account.setPassengerId(passengerId);
        account.setPointsBalance(0);
        account.setTier("Silver"); // Default tier
        account.setTotalPointsEarned(0);
        account.setTotalPointsRedeemed(0);
        account.setCreatedDate(LocalDate.now().format(DATE_FORMATTER));
        account.setLastUpdated(LocalDate.now().format(DATE_FORMATTER));
        
        // Set tier expiry (1 year from now)
        account.setTierExpiryDate(LocalDate.now().plusYears(1).format(DATE_FORMATTER));
        
        return loyaltyAccountRepository.save(account);
    }

    /**
     * Add points to loyalty account
     * Automatically updates tier
     * @param passengerId passenger ID
     * @param points points to add
     * @return updated loyalty account
     */
    public LoyaltyAccount addPoints(Long passengerId, int points) {
        if (points <= 0) {
            throw new IllegalArgumentException("Points must be positive");
        }
        
        LoyaltyAccount account = getAccountByPassengerId(passengerId);
        
        if (account == null) {
            throw new IllegalArgumentException("Loyalty account not found for passenger");
        }
        
        // Add points
        account.addPoints(points);
        account.setLastUpdated(LocalDate.now().format(DATE_FORMATTER));
        
        return loyaltyAccountRepository.save(account);
    }

    /**
     * Redeem points from loyalty account
     * Automatically updates tier if points decrease
     * @param passengerId passenger ID
     * @param points points to redeem
     * @return updated loyalty account or null if insufficient balance
     */
    public LoyaltyAccount redeemPoints(Long passengerId, int points) {
        if (points <= 0) {
            throw new IllegalArgumentException("Points must be positive");
        }
        
        LoyaltyAccount account = getAccountByPassengerId(passengerId);
        
        if (account == null) {
            throw new IllegalArgumentException("Loyalty account not found for passenger");
        }
        
        // Try to redeem
        boolean success = account.redeemPoints(points);
        
        if (!success) {
            throw new IllegalArgumentException("Insufficient points balance");
        }
        
        account.setLastUpdated(LocalDate.now().format(DATE_FORMATTER));
        
        return loyaltyAccountRepository.save(account);
    }

    /**
     * Get points balance
     * @param passengerId passenger ID
     * @return points balance or -1 if account not found
     */
    public int getPointsBalance(Long passengerId) {
        LoyaltyAccount account = getAccountByPassengerId(passengerId);
        return account != null ? account.getPointsBalance() : -1;
    }

    /**
     * Get current tier
     * @param passengerId passenger ID
     * @return tier name or null if account not found
     */
    public String getCurrentTier(Long passengerId) {
        LoyaltyAccount account = getAccountByPassengerId(passengerId);
        return account != null ? account.getTier() : null;
    }

    // ========================================
    // ENHANCEMENT 2: LOYALTY TIER VISUALIZER
    // ========================================

    /**
     * Get tier visualization data
     * Enhancement: Provides all data needed for tier visualizer UI
     * @param passengerId passenger ID
     * @return Map with tier details
     */
    public java.util.Map<String, Object> getTierVisualizationData(Long passengerId) {
        LoyaltyAccount account = getAccountByPassengerId(passengerId);
        
        if (account == null) {
            return null;
        }
        
        java.util.Map<String, Object> tierData = new java.util.HashMap<>();
        
        tierData.put("currentTier", account.getTier());
        tierData.put("tierColor", account.getTierColor());
        tierData.put("pointsBalance", account.getPointsBalance());
        tierData.put("nextTier", account.getNextTier());
        tierData.put("pointsToNextTier", account.getPointsToNextTier());
        tierData.put("progressToNextTier", account.getProgressToNextTier());
        tierData.put("tierBenefits", account.getTierBenefits());
        tierData.put("tierExpiryDate", account.getTierExpiryDate());
        tierData.put("totalPointsEarned", account.getTotalPointsEarned());
        tierData.put("totalPointsRedeemed", account.getTotalPointsRedeemed());
        
        return tierData;
    }

    /**
     * Get all tier thresholds
     * For UI reference
     * @return Map of tier → points threshold
     */
    public java.util.Map<String, Integer> getTierThresholds() {
        java.util.Map<String, Integer> thresholds = new java.util.HashMap<>();
        thresholds.put("Silver", LoyaltyAccount.SILVER_THRESHOLD);
        thresholds.put("Gold", LoyaltyAccount.GOLD_THRESHOLD);
        thresholds.put("Diamond", LoyaltyAccount.DIAMOND_THRESHOLD);
        return thresholds;
    }

    /**
     * Update tier for all accounts that need it
     * Maintenance function - can be run periodically
     * @return count of accounts updated
     */
    public int updateAllTiers() {
        List<LoyaltyAccount> accountsNeedingUpdate = 
            loyaltyAccountRepository.findAccountsNeedingTierUpdate();
        
        for (LoyaltyAccount account : accountsNeedingUpdate) {
            account.updateTier();
            account.setLastUpdated(LocalDate.now().format(DATE_FORMATTER));
            loyaltyAccountRepository.save(account);
        }
        
        return accountsNeedingUpdate.size();
    }

    /**
     * Get accounts by tier
     * @param tier tier name ("Silver", "Gold", "Diamond")
     * @return List of loyalty accounts
     */
    public List<LoyaltyAccount> getAccountsByTier(String tier) {
        return loyaltyAccountRepository.findByTier(tier);
    }

    /**
     * Get tier distribution statistics
     * Enhancement: For analytics/reporting
     * @return Map of tier → count
     */
    public java.util.Map<String, Long> getTierDistribution() {
        java.util.Map<String, Long> distribution = new java.util.HashMap<>();
        distribution.put("Silver", loyaltyAccountRepository.countByTier("Silver"));
        distribution.put("Gold", loyaltyAccountRepository.countByTier("Gold"));
        distribution.put("Diamond", loyaltyAccountRepository.countByTier("Diamond"));
        return distribution;
    }

    /**
     * Get leaderboard (top accounts by points)
     * Enhancement: Gamification feature
     * @param limit number of top accounts
     * @return List of top loyalty accounts
     */
    public List<LoyaltyAccount> getLeaderboard(int limit) {
        return loyaltyAccountRepository.findTopAccountsByPoints(limit);
    }

    /**
     * Calculate tier progress percentage
     * @param passengerId passenger ID
     * @return progress percentage (0-100)
     */
    public int getTierProgress(Long passengerId) {
        LoyaltyAccount account = getAccountByPassengerId(passengerId);
        return account != null ? account.getProgressToNextTier() : 0;
    }

    /**
     * Get estimated tier upgrade date
     * Based on average monthly points earned
     * @param passengerId passenger ID
     * @return estimated months to next tier, or -1 if already at max
     */
    public int getEstimatedMonthsToNextTier(Long passengerId) {
        LoyaltyAccount account = getAccountByPassengerId(passengerId);
        
        if (account == null || account.getTier().equals("Diamond")) {
            return -1;
        }
        
        int pointsNeeded = account.getPointsToNextTier();
        
        // Simple estimation: assume they earn at current rate
        // (This is a simplified calculation - could be enhanced with historical data)
        if (account.getTotalPointsEarned() > 0) {
            // Rough estimate based on total earned so far
            return pointsNeeded / 100; // Simplified calculation
        }
        
        return -1; // Cannot estimate
    }

    // ========================================
    // VALIDATION & HELPER METHODS
    // ========================================

    /**
     * Check if passenger has loyalty account
     * @param passengerId passenger ID
     * @return true if account exists
     */
    public boolean hasLoyaltyAccount(Long passengerId) {
        return loyaltyAccountRepository.existsByPassengerId(passengerId);
    }

    /**
     * Delete loyalty account
     * @param passengerId passenger ID
     * @return true if deleted, false if not found
     */
    public boolean deleteLoyaltyAccount(Long passengerId) {
        if (loyaltyAccountRepository.existsByPassengerId(passengerId)) {
            loyaltyAccountRepository.deleteByPassengerId(passengerId);
            return true;
        }
        return false;
    }

    /**
     * Get total loyalty accounts count
     * @return count
     */
    public long getTotalAccountsCount() {
        return loyaltyAccountRepository.count();
    }

    /**
     * Get total points across all accounts
     * @return total points
     */
    public long getTotalPointsAcrossAllAccounts() {
        Long total = loyaltyAccountRepository.getTotalPointsAcrossAllAccounts();
        return total != null ? total : 0L;
    }

    /**
     * Get average points balance
     * @return average points
     */
    public double getAveragePointsBalance() {
        Double avg = loyaltyAccountRepository.getAveragePointsBalance();
        return avg != null ? avg : 0.0;
    }
}
