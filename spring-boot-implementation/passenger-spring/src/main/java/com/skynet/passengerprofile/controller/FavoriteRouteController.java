package com.skynet.passengerprofile.controller;

import com.skynet.passengerprofile.model.FavoriteRoute;
import com.skynet.passengerprofile.service.FavoriteRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * FavoriteRouteController - REST API endpoints for Favorite Routes management
 * 
 * Base URL: /api/passenger-profile/favorites
 * 
 * Functionality 2: Manage Saved Favorites
 */
@RestController
@RequestMapping("/favorites")
@CrossOrigin(origins = "*")
public class FavoriteRouteController {

    @Autowired
    private FavoriteRouteService favoriteRouteService;

    // ========================================
    // FUNCTIONALITY 2: MANAGE SAVED FAVORITES
    // ========================================

    /**
     * Get favorite route by ID
     * GET /favorites/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRouteById(@PathVariable Long id) {
        FavoriteRoute route = favoriteRouteService.getRouteById(id);
        
        if (route == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Favorite route not found with ID: " + id));
        }
        
        return ResponseEntity.ok(route);
    }

    /**
     * Get all favorite routes for a passenger
     * GET /favorites/passenger/{passengerId}
     */
    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<FavoriteRoute>> getFavoriteRoutes(@PathVariable Long passengerId) {
        List<FavoriteRoute> routes = favoriteRouteService.getFavoriteRoutes(passengerId);
        return ResponseEntity.ok(routes);
    }

    /**
     * Add a new favorite route
     * POST /favorites
     * Body: FavoriteRoute JSON
     */
    @PostMapping
    public ResponseEntity<?> addFavoriteRoute(@RequestBody FavoriteRoute route) {
        try {
            FavoriteRoute created = favoriteRouteService.addFavoriteRoute(route);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Add favorite route with specific details
     * POST /favorites/passenger/{passengerId}
     * Body: { "departureAirport": "KLIA", "arrivalAirport": "SIN", "nickname": "Weekend Trip" }
     */
    @PostMapping("/passenger/{passengerId}")
    public ResponseEntity<?> addFavoriteRouteByPassenger(
            @PathVariable Long passengerId,
            @RequestBody Map<String, String> routeData) {
        try {
            String departure = routeData.get("departureAirport");
            String arrival = routeData.get("arrivalAirport");
            String nickname = routeData.get("nickname");
            
            if (departure == null || arrival == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "departureAirport and arrivalAirport are required"));
            }
            
            FavoriteRoute route = favoriteRouteService.addFavoriteRoute(
                passengerId, departure, arrival, nickname
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(route);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Update favorite route (mainly nickname)
     * PUT /favorites/{id}
     * Body: FavoriteRoute JSON with fields to update
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFavoriteRoute(@PathVariable Long id, 
                                                 @RequestBody FavoriteRoute route) {
        try {
            FavoriteRoute updated = favoriteRouteService.updateFavoriteRoute(id, route);
            
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Favorite route not found with ID: " + id));
            }
            
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Delete favorite route
     * DELETE /favorites/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavoriteRoute(@PathVariable Long id) {
        boolean deleted = favoriteRouteService.deleteFavoriteRoute(id);
        
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Favorite route not found with ID: " + id));
        }
        
        return ResponseEntity.ok(Map.of("message", "Favorite route deleted successfully"));
    }

    /**
     * Delete favorite route by route details
     * DELETE /favorites/passenger/{passengerId}/route
     * Query params: ?departure=KLIA&arrival=SIN
     */
    @DeleteMapping("/passenger/{passengerId}/route")
    public ResponseEntity<?> deleteFavoriteRouteByDetails(
            @PathVariable Long passengerId,
            @RequestParam String departure,
            @RequestParam String arrival) {
        favoriteRouteService.deleteFavoriteRoute(passengerId, departure, arrival);
        return ResponseEntity.ok(Map.of(
            "message", "Favorite route deleted successfully"
        ));
    }

    /**
     * Delete all favorite routes for a passenger
     * DELETE /favorites/passenger/{passengerId}/all
     */
    @DeleteMapping("/passenger/{passengerId}/all")
    public ResponseEntity<?> deleteAllFavoriteRoutes(@PathVariable Long passengerId) {
        favoriteRouteService.deleteAllFavoriteRoutes(passengerId);
        return ResponseEntity.ok(Map.of(
            "message", "All favorite routes deleted for passenger " + passengerId
        ));
    }

    /**
     * Check if route is already favorited
     * GET /favorites/passenger/{passengerId}/check
     * Query params: ?departure=KLIA&arrival=SIN
     */
    @GetMapping("/passenger/{passengerId}/check")
    public ResponseEntity<?> isFavorited(
            @PathVariable Long passengerId,
            @RequestParam String departure,
            @RequestParam String arrival) {
        boolean isFavorited = favoriteRouteService.isFavorited(passengerId, departure, arrival);
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "departureAirport", departure,
            "arrivalAirport", arrival,
            "isFavorited", isFavorited
        ));
    }

    /**
     * Get favorite routes by departure airport
     * GET /favorites/passenger/{passengerId}/departure/{airport}
     */
    @GetMapping("/passenger/{passengerId}/departure/{airport}")
    public ResponseEntity<List<FavoriteRoute>> getFavoritesByDeparture(
            @PathVariable Long passengerId,
            @PathVariable String airport) {
        List<FavoriteRoute> routes = favoriteRouteService.getFavoritesByDeparture(passengerId, airport);
        return ResponseEntity.ok(routes);
    }

    /**
     * Get favorite routes by arrival airport
     * GET /favorites/passenger/{passengerId}/arrival/{airport}
     */
    @GetMapping("/passenger/{passengerId}/arrival/{airport}")
    public ResponseEntity<List<FavoriteRoute>> getFavoritesByArrival(
            @PathVariable Long passengerId,
            @PathVariable String airport) {
        List<FavoriteRoute> routes = favoriteRouteService.getFavoritesByArrival(passengerId, airport);
        return ResponseEntity.ok(routes);
    }

    /**
     * Search favorite routes by nickname
     * GET /favorites/passenger/{passengerId}/search?keyword=trip
     */
    @GetMapping("/passenger/{passengerId}/search")
    public ResponseEntity<List<FavoriteRoute>> searchByNickname(
            @PathVariable Long passengerId,
            @RequestParam String keyword) {
        List<FavoriteRoute> routes = favoriteRouteService.searchFavoritesByNickname(passengerId, keyword);
        return ResponseEntity.ok(routes);
    }

    /**
     * Get favorite count for a passenger
     * GET /favorites/passenger/{passengerId}/count
     */
    @GetMapping("/passenger/{passengerId}/count")
    public ResponseEntity<?> getFavoriteCount(@PathVariable Long passengerId) {
        long count = favoriteRouteService.getFavoriteCount(passengerId);
        return ResponseEntity.ok(Map.of(
            "passengerId", passengerId,
            "favoriteCount", count
        ));
    }

    // ========================================
    // ANALYTICS & RECOMMENDATIONS
    // ========================================

    /**
     * Get most popular routes across all passengers
     * GET /favorites/analytics/popular-routes?limit=10
     */
    @GetMapping("/analytics/popular-routes")
    public ResponseEntity<List<Map<String, Object>>> getMostPopularRoutes(
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> popularRoutes = favoriteRouteService.getMostPopularRoutes(limit);
        return ResponseEntity.ok(popularRoutes);
    }

    /**
     * Get most popular departure airports
     * GET /favorites/analytics/popular-departures?limit=10
     */
    @GetMapping("/analytics/popular-departures")
    public ResponseEntity<List<Map<String, Object>>> getMostPopularDepartures(
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> popularDepartures = favoriteRouteService.getMostPopularDepartures(limit);
        return ResponseEntity.ok(popularDepartures);
    }

    /**
     * Get most popular arrival airports
     * GET /favorites/analytics/popular-arrivals?limit=10
     */
    @GetMapping("/analytics/popular-arrivals")
    public ResponseEntity<List<Map<String, Object>>> getMostPopularArrivals(
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> popularArrivals = favoriteRouteService.getMostPopularArrivals(limit);
        return ResponseEntity.ok(popularArrivals);
    }

    /**
     * Get route recommendations for a passenger
     * GET /favorites/passenger/{passengerId}/recommendations?limit=5
     * Based on popular routes they haven't favorited yet
     */
    @GetMapping("/passenger/{passengerId}/recommendations")
    public ResponseEntity<List<Map<String, Object>>> getRouteRecommendations(
            @PathVariable Long passengerId,
            @RequestParam(defaultValue = "5") int limit) {
        List<Map<String, Object>> recommendations = 
            favoriteRouteService.getRouteRecommendations(passengerId, limit);
        return ResponseEntity.ok(recommendations);
    }

    // ========================================
    // STATISTICS & UTILITIES
    // ========================================

    /**
     * Get total favorites count (all passengers)
     * GET /favorites/stats/count
     */
    @GetMapping("/stats/count")
    public ResponseEntity<?> getTotalFavoritesCount() {
        long count = favoriteRouteService.getTotalFavoritesCount();
        return ResponseEntity.ok(Map.of("totalFavorites", count));
    }
}
