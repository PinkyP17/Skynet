package com.skynet.passengerprofile.repository;

import com.skynet.passengerprofile.model.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * FavoriteRouteRepository - Data access layer for FavoriteRoute entity
 * 
 * Supports Manage Saved Favorites functionality
 */
@Repository
public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Long> {

    /**
     * Find all favorite routes for a passenger
     * @param passengerId passenger ID
     * @return List of favorite routes
     */
    List<FavoriteRoute> findByPassengerId(Long passengerId);

    /**
     * Find favorite route by passenger and route details
     * @param passengerId passenger ID
     * @param departureAirport departure airport code
     * @param arrivalAirport arrival airport code
     * @return Optional<FavoriteRoute>
     */
    Optional<FavoriteRoute> findByPassengerIdAndDepartureAirportAndArrivalAirport(
        Long passengerId, 
        String departureAirport, 
        String arrivalAirport
    );

    /**
     * Find routes by departure airport for a passenger
     * @param passengerId passenger ID
     * @param departureAirport departure airport code
     * @return List of favorite routes
     */
    List<FavoriteRoute> findByPassengerIdAndDepartureAirport(Long passengerId, String departureAirport);

    /**
     * Find routes by arrival airport for a passenger
     * @param passengerId passenger ID
     * @param arrivalAirport arrival airport code
     * @return List of favorite routes
     */
    List<FavoriteRoute> findByPassengerIdAndArrivalAirport(Long passengerId, String arrivalAirport);

    /**
     * Find all routes with a specific departure airport
     * @param departureAirport departure airport code
     * @return List of favorite routes
     */
    List<FavoriteRoute> findByDepartureAirport(String departureAirport);

    /**
     * Find all routes with a specific arrival airport
     * @param arrivalAirport arrival airport code
     * @return List of favorite routes
     */
    List<FavoriteRoute> findByArrivalAirport(String arrivalAirport);

    /**
     * Search favorite routes by nickname
     * @param passengerId passenger ID
     * @param keyword search term
     * @return List of matching routes
     */
    @Query("SELECT fr FROM FavoriteRoute fr WHERE fr.passengerId = :passengerId " +
           "AND LOWER(fr.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<FavoriteRoute> searchByNickname(@Param("passengerId") Long passengerId, @Param("keyword") String keyword);

    /**
     * Check if passenger has already saved this route
     * @param passengerId passenger ID
     * @param departureAirport departure airport code
     * @param arrivalAirport arrival airport code
     * @return true if route exists
     */
    boolean existsByPassengerIdAndDepartureAirportAndArrivalAirport(
        Long passengerId, 
        String departureAirport, 
        String arrivalAirport
    );

    /**
     * Count favorite routes for a passenger
     * @param passengerId passenger ID
     * @return count
     */
    long countByPassengerId(Long passengerId);

    /**
     * Find most popular routes (across all passengers)
     * @return List of routes ordered by popularity
     */
    @Query("SELECT fr.departureAirport, fr.arrivalAirport, COUNT(fr) as popularity " +
           "FROM FavoriteRoute fr " +
           "GROUP BY fr.departureAirport, fr.arrivalAirport " +
           "ORDER BY popularity DESC")
    List<Object[]> findMostPopularRoutes();

    /**
     * Find most popular departure airports
     * @return List of departure airports ordered by usage count
     */
    @Query("SELECT fr.departureAirport, COUNT(fr) as count " +
           "FROM FavoriteRoute fr " +
           "GROUP BY fr.departureAirport " +
           "ORDER BY count DESC")
    List<Object[]> findMostPopularDepartureAirports();

    /**
     * Find most popular arrival airports
     * @return List of arrival airports ordered by usage count
     */
    @Query("SELECT fr.arrivalAirport, COUNT(fr) as count " +
           "FROM FavoriteRoute fr " +
           "GROUP BY fr.arrivalAirport " +
           "ORDER BY count DESC")
    List<Object[]> findMostPopularArrivalAirports();

    /**
     * Delete all favorite routes for a passenger
     * @param passengerId passenger ID
     */
    void deleteByPassengerId(Long passengerId);

    /**
     * Delete a specific route for a passenger
     * @param passengerId passenger ID
     * @param departureAirport departure airport code
     * @param arrivalAirport arrival airport code
     */
    void deleteByPassengerIdAndDepartureAirportAndArrivalAirport(
        Long passengerId, 
        String departureAirport, 
        String arrivalAirport
    );
}
