package com.skynet.flightcatalog.repository;

import com.skynet.flightcatalog.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {
    
    // Find flights by airline
    List<Flight> findByAirlineId(Integer airlineId);
    
    // Find flights by departure date (using DATE function for SQLite)
    @Query(value = "SELECT * FROM flights WHERE DATE(dep_datetime) = DATE(:date)", nativeQuery = true)
    List<Flight> findByDepDatetimeDate(@Param("date") String date);
    
    // Find flights by route
    List<Flight> findByDepAirportIdAndArrAirportId(Integer depAirportId, Integer arrAirportId);
    
    // Find flights by route and date
    @Query(value = "SELECT * FROM flights WHERE dep_airport = :depAirportId AND arr_airport = :arrAirportId AND DATE(dep_datetime) = DATE(:date)", nativeQuery = true)
    List<Flight> findByRouteAndDate(@Param("depAirportId") Integer depAirportId, 
                                     @Param("arrAirportId") Integer arrAirportId, 
                                     @Param("date") String date);
    
    // Find flights by status
    List<Flight> findByStatus(String status);
    
    // Check for duplicate flight (same route, date, and airline)
    @Query(value = "SELECT COUNT(*) > 0 FROM flights WHERE dep_airport = :depAirportId AND arr_airport = :arrAirportId AND DATE(dep_datetime) = DATE(:date) AND id_airline = :airlineId", nativeQuery = true)
    boolean existsDuplicate(@Param("depAirportId") Integer depAirportId,
                           @Param("arrAirportId") Integer arrAirportId,
                           @Param("date") String date,
                           @Param("airlineId") Integer airlineId);
    
    // Check for duplicate flight excluding current flight (for updates)
    @Query(value = "SELECT COUNT(*) > 0 FROM flights WHERE dep_airport = :depAirportId AND arr_airport = :arrAirportId AND DATE(dep_datetime) = DATE(:date) AND id_airline = :airlineId AND id != :excludeId", nativeQuery = true)
    boolean existsDuplicateExcluding(@Param("depAirportId") Integer depAirportId,
                                     @Param("arrAirportId") Integer arrAirportId,
                                     @Param("date") String date,
                                     @Param("airlineId") Integer airlineId,
                                     @Param("excludeId") Integer excludeId);
}
