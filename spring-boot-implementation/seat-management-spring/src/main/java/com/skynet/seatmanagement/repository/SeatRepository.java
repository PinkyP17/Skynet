package com.skynet.seatmanagement.repository;

import com.skynet.seatmanagement.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SeatRepository - Data access layer for Seat entity
 * 
 * Spring Data JPA automatically provides:
 * - save(Seat) - Create or update
 * - findById(Integer) - Read by ID
 * - findAll() - Read all
 * - deleteById(Integer) - Delete by ID
 * - count() - Count total records
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    /**
     * Find seats by type
     * @param type seat type (First, Business, Economy)
     * @return List of seats
     */
    List<Seat> findByType(String type);

    /**
     * Find seat by column and row
     * @param column seat column (A, B, C, etc.)
     * @param row seat row number
     * @return Optional<Seat>
     */
    Optional<Seat> findByColumnAndRow(String column, Integer row);

    /**
     * Find seats by column
     * @param column seat column
     * @return List of seats
     */
    List<Seat> findByColumn(String column);

    /**
     * Find seats by row
     * @param row seat row number
     * @return List of seats
     */
    List<Seat> findByRow(Integer row);

    /**
     * Search seats by label pattern
     * Enhancement: Seat search
     * @param pattern search pattern
     * @return List of matching seats
     */
    @Query("SELECT s FROM Seat s WHERE " +
           "LOWER(CONCAT(s.column, s.row)) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " +
           "LOWER(s.column) LIKE LOWER(CONCAT('%', :pattern, '%')) OR " +
           "CAST(s.row AS string) LIKE CONCAT('%', :pattern, '%') OR " +
           "LOWER(s.type) LIKE LOWER(CONCAT('%', :pattern, '%'))")
    List<Seat> searchSeats(@Param("pattern") String pattern);

    /**
     * Count seats by type
     * @param type seat type
     * @return count
     */
    long countByType(String type);
}
