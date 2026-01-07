package com.skynet.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skynet.booking.dto.CreateBookingRequest;
import com.skynet.common.model.Reservation;
import com.skynet.common.service.IBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBooking_ShouldReturnReservation_WhenValidRequest() throws Exception {
        // Arrange
        CreateBookingRequest request = new CreateBookingRequest(101, 202, "1");
        Reservation mockReservation = new Reservation();
        mockReservation.setId(1);
        mockReservation.setStatus("BOOKED");

        when(bookingService.createBooking(anyInt(), anyInt(), anyString())).thenReturn(mockReservation);

        // Act & Assert
        mockMvc.perform(post("/api/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("BOOKED"));
    }

    @Test
    void getPNR_ShouldReturnPNR_WhenBookingExists() throws Exception {
        // Arrange
        when(bookingService.retrieveBookingPNR(1)).thenReturn("PNR123");

        // Act & Assert
        mockMvc.perform(get("/api/booking/1/pnr"))
                .andExpect(status().isOk())
                .andExpect(content().string("PNR123"));
    }
}
