package com.skynet.booking.repository;

import com.skynet.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Integer> {
    Optional<BookingEntity> findByPnr(String pnr);
    List<BookingEntity> findByAccountId(int accountId);
    List<BookingEntity> findByFlightId(int flightId);
}
