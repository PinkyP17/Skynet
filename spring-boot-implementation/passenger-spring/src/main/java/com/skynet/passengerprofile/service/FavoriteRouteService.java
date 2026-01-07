package com.skynet.passengerprofile.service;

import com.skynet.passengerprofile.model.FavoriteRoute;
import com.skynet.passengerprofile.repository.FavoriteRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * FavoriteRouteService - Business logic for Favorite Routes management
 * 
 * Functionality 2: Manage Saved Favorites
 */
@Service
@Transactional
public class FavoriteRouteService {

    @Autowired
    private FavoriteRouteRepository favoriteRouteRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ========================================
    // FUNCTIONALITY 2: MANAGE SAVED FAVORITES
    // ========================================

    /**
     * Get favorite route by ID
     * @param id route ID
     * @return FavoriteRoute or null
     */
    public FavoriteRoute getRouteById(Long id) {
        return favoriteRouteRepository.findById(id).orElse(null);
    }

    /**
     * Get all favorite routes for a passenger
     * @param passengerId passenger ID
     * @return List of favorite routes
     */
    public List<FavoriteRoute> getFavoriteRoutes(Long passengerId) {
        return favoriteRouteRepository.findByPassengerId(passengerId);
    }

    /**
     * Add a new favorite route
     * @param route favorite route object
     * @return saved route
     */
    public FavoriteRoute addFavoriteRoute(FavoriteRoute route) {
        // Check for duplicates
        boolean exists = favoriteRouteRepository.existsByPassengerIdAndDepartureAirportAndArrivalAirport(
            route.getPassengerId(),
            route.getDepartureAirport(),
            route.getArrivalAirport()
        );
        
        if (exists) {
            throw new IllegalArgumentException("This route is already in your favorites");
        }
        
        // Validate route
        validateRoute(route);
        
        // Set created date
        if (route.getCreatedDate() == null) {
            route.setCreatedDate(LocalDate.now().format(DATE_FORMATTER));
        }
        
        return favoriteRouteRepository.save(route);
    }

    /**
     * Add favorite route with specific details
     * @param passengerId passenger ID
     * @param departureAirport departure airport code
     * @param arrivalAirport arrival airport code
     * @param nickname optional nickname for the route
     * @return saved route
     */
    public FavoriteRoute addFavoriteRoute(Long passengerId, String departureAirport, 
                                         String arrivalAirport, String nickname) {
        FavoriteRoute route = new FavoriteRoute();
        route.setPassengerId(passengerId);
        route.setDepartureAirport(departureAirport.toUpperCase());
        route.setArrivalAirport(arrivalAirport.toUpperCase());
        route.setNickname(nickname);
        route.setCreatedDate(LocalDate.now().format(DATE_FORMATTER));
        
        return addFavoriteRoute(route);
    }

    /**
     * Update favorite route (mainly for updating nickname)
     * @param id route ID
     * @param updatedRoute updated route data
     * @return updated route or null if not found
     */
    public FavoriteRoute updateFavoriteRoute(Long id, FavoriteRoute updatedRoute) {
        Optional<FavoriteRoute> existingOpt = favoriteRouteRepository.findById(id);
        
        if (existingOpt.isEmpty()) {
            return null;
        }
        
        FavoriteRoute existing = existingOpt.get();
        
        // Update nickname (main use case for update)
        if (updatedRoute.getNickname() != null) {
            existing.setNickname(updatedRoute.getNickname());
        }
        
        // Update airports if needed (but check for duplicates)
        if (updatedRoute.getDepartureAirport() != null && 
            updatedRoute.getArrivalAirport() != null) {
            
            boolean exists = favoriteRouteRepository.existsByPassengerIdAndDepartureAirportAndArrivalAirport(
                existing.getPassengerId(),
                updatedRoute.getDepartureAirport(),
                updatedRoute.getArrivalAirport()
            );
            
            if (exists && !existing.getDepartureAirport().equals(updatedRoute.getDepartureAirport()) 
                && !existing.getArrivalAirport().equals(updatedRoute.getArrivalAirport())) {
                throw new IllegalArgumentException("Updated route already exists in favorites");
            }
            
            existing.setDepartureAirport(updatedRoute.getDepartureAirport().toUpperCase());
            existing.setArrivalAirport(updatedRoute.getArrivalAirport().toUpperCase());
        }
        
        validateRoute(existing);
        
        return favoriteRouteRepository.save(existing);
    }

    /**
     * Delete favorite route
     * @param id route ID
     * @return true if deleted, false if not found
     */
    public boolean deleteFavoriteRoute(Long id) {
        if (favoriteRouteRepository.existsById(id)) {
            favoriteRouteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Delete favorite route by route details
     * @param passengerId passenger ID
     * @param departureAirport departure airport code
     * @param arrivalAirport arrival airport code
     * @return true if deleted
     */
    public boolean deleteFavoriteRoute(Long passengerId, String departureAirport, String arrivalAirport) {
        favoriteRouteRepository.deleteByPassengerIdAndDepartureAirportAndArrivalAirport(
            passengerId, 
            departureAirport.toUpperCase(), 
            arrivalAirport.toUpperCase()
        );
        return true;
    }

    /**
     * Delete all favorite routes for a passenger
     * @param passengerId passenger ID
     */
    public void deleteAllFavoriteRoutes(Long passengerId) {
        favoriteRouteRepository.deleteByPassengerId(passengerId);
    }

    /**
     * Check if route is already favorited
     * @param passengerId passenger ID
     * @param departureAirport departure airport code
     * @param arrivalAirport arrival airport code
     * @return true if route exists in favorites
     */
    public boolean isFavorited(Long passengerId, String departureAirport, String arrivalAirport) {
        return favoriteRouteRepository.existsByPassengerIdAndDepartureAirportAndArrivalAirport(
            passengerId, 
            departureAirport.toUpperCase(), 
            arrivalAirport.toUpperCase()
        );
    }

    /**
     * Get favorite routes by departure airport
     * @param passengerId passenger ID
     * @param departureAirport departure airport code
     * @return List of matching routes
     */
    public List<FavoriteRoute> getFavoritesByDeparture(Long passengerId, String departureAirport) {
        return favoriteRouteRepository.findByPassengerIdAndDepartureAirport(
            passengerId, 
            departureAirport.toUpperCase()
        );
    }

    /**
     * Get favorite routes by arrival airport
     * @param passengerId passenger ID
     * @param arrivalAirport arrival airport code
     * @return List of matching routes
     */
    public List<FavoriteRoute> getFavoritesByArrival(Long passengerId, String arrivalAirport) {
        return favoriteRouteRepository.findByPassengerIdAndArrivalAirport(
            passengerId, 
            arrivalAirport.toUpperCase()
        );
    }

    /**
     * Search favorite routes by nickname
     * @param passengerId passenger ID
     * @param keyword search keyword
     * @return List of matching routes
     */
    public List<FavoriteRoute> searchFavoritesByNickname(Long passengerId, String keyword) {
        return favoriteRouteRepository.searchByNickname(passengerId, keyword);
    }

    /**
     * Get favorite count for a passenger
     * @param passengerId passenger ID
     * @return count of favorite routes
     */
    public long getFavoriteCount(Long passengerId) {
        return favoriteRouteRepository.countByPassengerId(passengerId);
    }

    // ========================================
    // ANALYTICS & RECOMMENDATIONS
    // ========================================

    /**
     * Get most popular routes across all passengers
     * Can be used for recommendations
     * @param limit number of routes to return
     * @return List of popular route data
     */
    public List<java.util.Map<String, Object>> getMostPopularRoutes(int limit) {
        List<Object[]> results = favoriteRouteRepository.findMostPopularRoutes();
        
        return results.stream()
            .limit(limit)
            .map(row -> {
                java.util.Map<String, Object> routeData = new java.util.HashMap<>();
                routeData.put("departureAirport", row[0]);
                routeData.put("arrivalAirport", row[1]);
                routeData.put("popularity", row[2]);
                return routeData;
            })
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get most popular departure airports
     * @param limit number of airports to return
     * @return List of airport data
     */
    public List<java.util.Map<String, Object>> getMostPopularDepartures(int limit) {
        List<Object[]> results = favoriteRouteRepository.findMostPopularDepartureAirports();
        
        return results.stream()
            .limit(limit)
            .map(row -> {
                java.util.Map<String, Object> airportData = new java.util.HashMap<>();
                airportData.put("airport", row[0]);
                airportData.put("count", row[1]);
                return airportData;
            })
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get most popular arrival airports
     * @param limit number of airports to return
     * @return List of airport data
     */
    public List<java.util.Map<String, Object>> getMostPopularArrivals(int limit) {
        List<Object[]> results = favoriteRouteRepository.findMostPopularArrivalAirports();
        
        return results.stream()
            .limit(limit)
            .map(row -> {
                java.util.Map<String, Object> airportData = new java.util.HashMap<>();
                airportData.put("airport", row[0]);
                airportData.put("count", row[1]);
                return airportData;
            })
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Get route recommendations for a passenger
     * Based on popular routes they haven't favorited yet
     * @param passengerId passenger ID
     * @param limit number of recommendations
     * @return List of recommended routes
     */
    public List<java.util.Map<String, Object>> getRouteRecommendations(Long passengerId, int limit) {
        List<FavoriteRoute> userFavorites = getFavoriteRoutes(passengerId);
        List<java.util.Map<String, Object>> popularRoutes = getMostPopularRoutes(50);
        
        // Filter out routes user already has
        return popularRoutes.stream()
            .filter(route -> {
                String dep = (String) route.get("departureAirport");
                String arr = (String) route.get("arrivalAirport");
                
                return userFavorites.stream()
                    .noneMatch(fav -> fav.getDepartureAirport().equals(dep) 
                              && fav.getArrivalAirport().equals(arr));
            })
            .limit(limit)
            .collect(java.util.stream.Collectors.toList());
    }

    // ========================================
    // VALIDATION & HELPER METHODS
    // ========================================

    /**
     * Validate route data
     * @param route route to validate
     * @throws IllegalArgumentException if invalid
     */
    private void validateRoute(FavoriteRoute route) {
        if (route.getDepartureAirport() == null || route.getDepartureAirport().trim().isEmpty()) {
            throw new IllegalArgumentException("Departure airport is required");
        }
        
        if (route.getArrivalAirport() == null || route.getArrivalAirport().trim().isEmpty()) {
            throw new IllegalArgumentException("Arrival airport is required");
        }
        
        if (route.getDepartureAirport().equalsIgnoreCase(route.getArrivalAirport())) {
            throw new IllegalArgumentException("Departure and arrival airports cannot be the same");
        }
        
        // Validate airport code format (typically 3-4 letters)
        if (!route.getDepartureAirport().matches("[A-Z]{3,4}")) {
            throw new IllegalArgumentException("Invalid departure airport code format");
        }
        
        if (!route.getArrivalAirport().matches("[A-Z]{3,4}")) {
            throw new IllegalArgumentException("Invalid arrival airport code format");
        }
    }

    /**
     * Get total favorite routes count (all passengers)
     * @return count
     */
    public long getTotalFavoritesCount() {
        return favoriteRouteRepository.count();
    }
}
