package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.DtoMapper;
import com.streetLeague.backend.dto.ReservationDTO;
import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.enums.StatutReservation;
import com.streetLeague.backend.security.CustomUserDetailsService;
import com.streetLeague.backend.security.JwtService;
import com.streetLeague.backend.service.ReservationPdfService;
import com.streetLeague.backend.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private ReservationPdfService reservationPdfService;

    @MockBean
    private DtoMapper dtoMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getAll_shouldReturnList() throws Exception {
        when(reservationService.getAllReservations()).thenReturn(List.of(new Reservation()));
        when(dtoMapper.toReservationDTO(any())).thenReturn(new ReservationDTO());

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getById_shouldReturnReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        ReservationDTO dto = new ReservationDTO();
        dto.setId(1L);

        when(reservationService.getReservationById(1L)).thenReturn(reservation);
        when(dtoMapper.toReservationDTO(reservation)).thenReturn(dto);

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_shouldReturn201() throws Exception {
        ReservationDTO request = new ReservationDTO();
        request.setDateDebut(LocalDateTime.now());
        request.setDateFin(LocalDateTime.now().plusHours(1));

        Reservation entity = new Reservation();
        ReservationDTO response = new ReservationDTO();
        response.setId(1L);

        when(dtoMapper.toReservation(any())).thenReturn(entity);
        when(reservationService.createReservation(anyLong(), any())).thenReturn(entity);
        when(dtoMapper.toReservationDTO(entity)).thenReturn(response);

        mockMvc.perform(post("/api/reservations/sous-espace/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getByStatut_shouldFilter() throws Exception {
        when(reservationService.getReservationsByStatut(StatutReservation.CONFIRMEE))
                .thenReturn(List.of(new Reservation()));
        when(dtoMapper.toReservationDTO(any())).thenReturn(new ReservationDTO());

        mockMvc.perform(get("/api/reservations/statut/CONFIRMEE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void confirmer_shouldSucceed() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatut(StatutReservation.CONFIRMEE);
        ReservationDTO dto = new ReservationDTO();
        dto.setId(1L);
        dto.setStatut(StatutReservation.CONFIRMEE);

        when(reservationService.confirmerReservation(1L)).thenReturn(reservation);
        when(dtoMapper.toReservationDTO(reservation)).thenReturn(dto);

        mockMvc.perform(patch("/api/reservations/1/confirmer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("CONFIRMEE"));
    }

    @Test
    void annuler_shouldSucceed() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setStatut(StatutReservation.ANNULEE);
        ReservationDTO dto = new ReservationDTO();
        dto.setId(1L);
        dto.setStatut(StatutReservation.ANNULEE);

        when(reservationService.annulerReservation(eq(1L), anyString())).thenReturn(reservation);
        when(dtoMapper.toReservationDTO(reservation)).thenReturn(dto);

        mockMvc.perform(patch("/api/reservations/1/annuler?motif=Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("ANNULEE"));
    }
}
