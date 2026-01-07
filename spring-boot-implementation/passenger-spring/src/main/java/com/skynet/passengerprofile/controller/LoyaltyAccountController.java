package com.skynet.passengerprofile.controller;

import com.skynet.passengerprofile.model.LoyaltyAccount;
import com.skynet.passengerprofile.service.LoyaltyAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * LoyaltyAccountController - REST API endpoints for Loyalty Points management
 * 
 * Base URL: /api/passenger-profile/loyalty
 * 
 * Functionality 4: Manage Loyalty Points
 * Enhancement 2: Loyalty Tier Visualizer
 */
@RestController
@RequestMapping("/loyalty")
@CrossOrigin(origins = "*")
public class LoyaltyAccountController {

    @Autowired
    private LoyaltyAccountService loyaltyAccountService;

    // ========================================
    // FUNCTIONALITY 4: MANAGE LOYALTY POINTS
    // ========================================

    /**
     * Get loyalty account by ID
     * GET /loyalty/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id) {
        LoyaltyAccount account = loyaltyAccountService.getAccountById(id);
        
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Loyalty account not found with ID: " + id));
        }
        
        return ResponseEntity.ok(account);
    }

    /**
     * Get loyalty account by passenger ID
     * GET /loyalty/passenger/{passengerId}
     */
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<?> getAccountByPassengerId(@PathVariable Long passengerId) {
        LoyaltyAccount account = loyaltyAccountService.getAccountByPassengerId(passengerId);
        
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No loyalty account found for passenger ID: " + passengerId));
        }
        
        return ResponseEntity.ok(account);
    }

    /**
     * Create new loyalty account for a passenger
     * POST /loyalty/passenger/{passengerId}
     */
    @PostMapping("/passenger/{passengerId}")
    public ResponseEntity<?> createLoyaltyAccount(@PathVariable Long passengerId) {
        try {
            LoyaltyAccount account = loyaltyAccountService.createLoyaltyAccount(passengerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Add points to loyalty account
     * POST /loyalty/passenger/{passengerId}/add-points
     * Body: { "points": 500 }
     */
    @PostMapping("/passenger/{passengerId}/add-points")
    public ResponseEntity<?> addPoints(@PathVariable Long passengerId, 
                                      @RequestBody Map<String, Integer> request) {
        try {
            Integer points = request.get("points");
            if (points == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Points field is required"));
            }
            
            LoyaltyAccount account = loyaltyAccountService.addPoints(passengerId, points);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Redeem points from loyalty account
     * POST /loyalty/passenger/{passengerId}/redeem-points
     * Body: { "points": 200 }
     */
    @PostMapping("/passenger/{passengerId}/redeem-points")
    public ResponseEntity<?> redeemPoints(@PathVariable Long passengerId, 
                                         @RequestBody Map<String, Integer> request) {
        try {
            Integer points = request.get("points");
            if (points == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Points field is required"));
            }
            
            LoyaltyAccount account = loyaltyAccountService.redeemPoints(passengerId, points);
            return ResponseEntity.ok(account);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Get points balance
     * GET /loyalty/passenger/{passengerId}/balance
     */
    @GetMapping("/passenger/{passengerId}/balance")
    public ResponseEntity<?> getPointsBalance(@PathVariable Long passengerId) {
        int balance = loyaltyAccountService.getPointsBalance(passengerId);
        
        if (balance == -1) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No loyalty account found for passenger ID: " + passengerId));
        }
        
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "pointsBalance", balance
        ));
    }

    /**
     * Get current tier
     * GET /loyalty/passenger/{passengerId}/tier
     */
    @GetMapping("/passenger/{passengerId}/tier")
    public ResponseEntity<?> getCurrentTier(@PathVariable Long passengerId) {
        String tier = loyaltyAccountService.getCurrentTier(passengerId);
        
        if (tier == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No loyalty account found for passenger ID: " + passengerId));
        }
        
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "currentTier", tier
        ));
    }

    // ========================================
    // ENHANCEMENT 2: LOYALTY TIER VISUALIZER
    // ========================================

    /**
     * Get tier visualization data
     * GET /loyalty/passenger/{passengerId}/tier-visualization
     * Enhancement: Complete data for tier visualizer UI
     */
    @GetMapping("/passenger/{passengerId}/tier-visualization")
    public ResponseEntity<?> getTierVisualizationData(@PathVariable Long passengerId) {
        Map<String, Object> tierData = loyaltyAccountService.getTierVisualizationData(passengerId);
        
        if (tierData == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No loyalty account found for passenger ID: " + passengerId));
        }
        
        return ResponseEntity.ok(tierData);
    }

    /**
     * Get tier progress percentage
     * GET /loyalty/passenger/{passengerId}/tier-progress
     * Enhancement: Progress bar data (0-100)
     */
    @GetMapping("/passenger/{passengerId}/tier-progress")
    public ResponseEntity<?> getTierProgress(@PathVariable Long passengerId) {
        int progress = loyaltyAccountService.getTierProgress(passengerId);
        
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "progressToNextTier", progress
        ));
    }

    /**
     * Get all tier thresholds
     * GET /loyalty/tier-thresholds
     * For UI reference
     */
    @GetMapping("/tier-thresholds")
    public ResponseEntity<Map<String, Integer>> getTierThresholds() {
        Map<String, Integer> thresholds = loyaltyAccountService.getTierThresholds();
        return ResponseEntity.ok(thresholds);
    }

    /**
     * Get tier distribution statistics
     * GET /loyalty/stats/tier-distribution
     * Enhancement: Analytics for reporting
     */
    @GetMapping("/stats/tier-distribution")
    public ResponseEntity<Map<String, Long>> getTierDistribution() {
        Map<String, Long> distribution = loyaltyAccountService.getTierDistribution();
        return ResponseEntity.ok(distribution);
    }

    /**
     * Get leaderboard (top accounts by points)
     * GET /loyalty/leaderboard?limit=10
     * Enhancement: Gamification
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<LoyaltyAccount>> getLeaderboard(
            @RequestParam(defaultValue = "10") int limit) {
        List<LoyaltyAccount> leaderboard = loyaltyAccountService.getLeaderboard(limit);
        return ResponseEntity.ok(leaderboard);
    }

    /**
     * Get accounts by tier
     * GET /loyalty/tier/{tierName}
     * Example: /loyalty/tier/Gold
     */
    @GetMapping("/tier/{tierName}")
    public ResponseEntity<List<LoyaltyAccount>> getAccountsByTier(@PathVariable String tierName) {
        List<LoyaltyAccount> accounts = loyaltyAccountService.getAccountsByTier(tierName);
        return ResponseEntity.ok(accounts);
    }

    // ========================================
    // ADMIN ENDPOINTS
    // ========================================

    /**
     * Update all tiers (maintenance function)
     * POST /loyalty/admin/update-all-tiers
     * Admin only
     */
    @PostMapping("/admin/update-all-tiers")
    public ResponseEntity<?> updateAllTiers() {
        int updatedCount = loyaltyAccountService.updateAllTiers();
        return ResponseEntity.ok(Map.of(
            "message", "Tier update completed",
            "accountsUpdated", updatedCount
        ));
    }

    /**
     * Get total loyalty accounts count
     * GET /loyalty/stats/count
     */
    @GetMapping("/stats/count")
    public ResponseEntity<?> getTotalAccountsCount() {
        long count = loyaltyAccountService.getTotalAccountsCount();
        return ResponseEntity.ok(Map.of("totalLoyaltyAccounts", count));
    }

    /**
     * Get total points across all accounts
     * GET /loyalty/stats/total-points
     */
    @GetMapping("/stats/total-points")
    public ResponseEntity<?> getTotalPoints() {
        long totalPoints = loyaltyAccountService.getTotalPointsAcrossAllAccounts();
        return ResponseEntity.ok(Map.of("totalPointsAcrossAllAccounts", totalPoints));
    }

    /**
     * Get average points balance
     * GET /loyalty/stats/average-points
     */
    @GetMapping("/stats/average-points")
    public ResponseEntity<?> getAveragePoints() {
        double avgPoints = loyaltyAccountService.getAveragePointsBalance();
        return ResponseEntity.ok(Map.of("averagePointsBalance", avgPoints));
    }

    // ========================================
    // UTILITIES
    // ========================================

    /**
     * Check if passenger has loyalty account
     * GET /loyalty/passenger/{passengerId}/exists
     */
    @GetMapping("/passenger/{passengerId}/exists")
    public ResponseEntity<?> hasLoyaltyAccount(@PathVariable Long passengerId) {
        boolean hasAccount = loyaltyAccountService.hasLoyaltyAccount(passengerId);
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "hasLoyaltyAccount", hasAccount
        ));
    }

    /**
     * Delete loyalty account
     * DELETE /loyalty/passenger/{passengerId}
     */
    @DeleteMapping("/passenger/{passengerId}")
    public ResponseEntity<?> deleteLoyaltyAccount(@PathVariable Long passengerId) {
        boolean deleted = loyaltyAccountService.deleteLoyaltyAccount(passengerId);
        
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "No loyalty account found for passenger ID: " + passengerId));
        }
        
        return ResponseEntity.ok(Map.of(
            "message", "Loyalty account deleted successfully"
        ));
    }
}
