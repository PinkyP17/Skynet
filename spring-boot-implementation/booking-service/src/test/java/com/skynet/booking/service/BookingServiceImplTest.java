package com.skynet.booking.service;

import com.skynet.booking.entity.BookingEntity;
import com.skynet.booking.exception.ResourceNotFoundException;
import com.skynet.booking.repository.BookingRepository;
import com.skynet.common.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBooking_ShouldReturnReservation_WhenInputsAreValid() {
        // Arrange
        BookingEntity savedEntity = new BookingEntity();
        savedEntity.setId(1);
        savedEntity.setFlightId(101);
        savedEntity.setAccountId(202);
        savedEntity.setStatus("BOOKED");
        savedEntity.setPnr("ABC123");

        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(savedEntity);

        // Mock Passenger Service Response
        java.util.Map<String, Object> passengerResponse = new java.util.HashMap<>();
        passengerResponse.put("exists", true);
        when(restTemplate.getForObject(anyString(), eq(java.util.Map.class)))
            .thenReturn(passengerResponse);

        // Act
        Reservation result = bookingService.createBooking(101, 202, "1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("BOOKED", result.getStatus());
        assertEquals(101, result.getFlight().getId());
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    void cancelBooking_ShouldReturnTrue_WhenBookingExists() {
        // Arrange
        BookingEntity existingBooking = new BookingEntity();
        existingBooking.setId(1);
        existingBooking.setStatus("BOOKED");

        when(bookingRepository.findById(1)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(existingBooking);

        // Act
        boolean result = bookingService.cancelBooking(1);

        // Assert
        assertTrue(result);
        assertEquals("CANCELLED", existingBooking.getStatus());
        verify(bookingRepository, times(1)).save(existingBooking);
    }

    @Test
    void retrieveBookingPNR_ShouldThrowException_WhenBookingNotFound() {
        // Arrange
        when(bookingRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            bookingService.retrieveBookingPNR(999);
        });
    }
}
