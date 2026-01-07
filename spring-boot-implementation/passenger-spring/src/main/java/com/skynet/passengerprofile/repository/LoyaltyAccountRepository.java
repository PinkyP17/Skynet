package com.skynet.passengerprofile.repository;

import com.skynet.passengerprofile.model.LoyaltyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * LoyaltyAccountRepository - Data access layer for LoyaltyAccount entity
 * 
 * Supports Loyalty Tier Visualizer enhancement
 */
@Repository
public interface LoyaltyAccountRepository extends JpaRepository<LoyaltyAccount, Long> {

    /**
     * Find loyalty account by passenger ID
     * One-to-one relationship: each passenger has max 1 loyalty account
     * @param passengerId passenger ID
     * @return Optional<LoyaltyAccount>
     */
    Optional<LoyaltyAccount> findByPassengerId(Long passengerId);

    /**
     * Find all accounts by tier
     * Enhancement: Loyalty Tier Visualizer
     * @param tier tier name ("Silver", "Gold", "Diamond")
     * @return List of loyalty accounts
     */
    List<LoyaltyAccount> findByTier(String tier);

    /**
     * Find accounts with points balance greater than or equal to amount
     * @param points minimum points
     * @return List of loyalty accounts
     */
    List<LoyaltyAccount> findByPointsBalanceGreaterThanEqual(Integer points);

    /**
     * Find accounts with points balance between min and max
     * @param minPoints minimum points
     * @param maxPoints maximum points
     * @return List of loyalty accounts
     */
    List<LoyaltyAccount> findByPointsBalanceBetween(Integer minPoints, Integer maxPoints);

    /**
     * Find Silver tier accounts (0 - 4,999 points)
     * Enhancement: Loyalty Tier Visualizer
     * @return List of Silver tier accounts
     */
    @Query("SELECT la FROM LoyaltyAccount la WHERE la.tier = 'Silver'")
    List<LoyaltyAccount> findSilverTierAccounts();

    /**
     * Find Gold tier accounts (5,000 - 9,999 points)
     * Enhancement: Loyalty Tier Visualizer
     * @return List of Gold tier accounts
     */
    @Query("SELECT la FROM LoyaltyAccount la WHERE la.tier = 'Gold'")
    List<LoyaltyAccount> findGoldTierAccounts();

    /**
     * Find Diamond tier accounts (10,000+ points)
     * Enhancement: Loyalty Tier Visualizer
     * @return List of Diamond tier accounts
     */
    @Query("SELECT la FROM LoyaltyAccount la WHERE la.tier = 'Diamond'")
    List<LoyaltyAccount> findDiamondTierAccounts();

    /**
     * Find accounts eligible for tier upgrade
     * (e.g., Silver with 5000+ points, Gold with 10000+ points)
     * @return List of accounts needing tier update
     */
    @Query("SELECT la FROM LoyaltyAccount la WHERE " +
           "(la.tier = 'Silver' AND la.pointsBalance >= 5000) OR " +
           "(la.tier = 'Gold' AND la.pointsBalance >= 10000)")
    List<LoyaltyAccount> findAccountsNeedingTierUpdate();

    /**
     * Find top N accounts by points balance
     * @param limit number of accounts to return
     * @return List of top loyalty accounts
     */
    @Query("SELECT la FROM LoyaltyAccount la ORDER BY la.pointsBalance DESC")
    List<LoyaltyAccount> findTopAccountsByPoints(@Param("limit") int limit);

    /**
     * Check if passenger has a loyalty account
     * @param passengerId passenger ID
     * @return true if account exists
     */
    boolean existsByPassengerId(Long passengerId);

    /**
     * Count accounts by tier
     * @param tier tier name
     * @return count
     */
    long countByTier(String tier);

    /**
     * Get total points across all accounts
     * @return sum of all points balances
     */
    @Query("SELECT SUM(la.pointsBalance) FROM LoyaltyAccount la")
    Long getTotalPointsAcrossAllAccounts();

    /**
     * Get average points balance
     * @return average points
     */
    @Query("SELECT AVG(la.pointsBalance) FROM LoyaltyAccount la")
    Double getAveragePointsBalance();

    /**
     * Find accounts with tier expiring before date
     * @param date expiry date threshold
     * @return List of accounts with expiring tiers
     */
    @Query("SELECT la FROM LoyaltyAccount la WHERE la.tierExpiryDate <= :date")
    List<LoyaltyAccount> findTierExpiringBefore(@Param("date") String date);

    /**
     * Delete loyalty account by passenger ID
     * @param passengerId passenger ID
     */
    void deleteByPassengerId(Long passengerId);
}
