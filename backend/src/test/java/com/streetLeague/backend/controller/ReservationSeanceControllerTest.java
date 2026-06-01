package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.ReservationSeanceDTO;
import com.streetLeague.backend.enums.ModePaiement;
import com.streetLeague.backend.security.AuthenticatedUserResolver;
import com.streetLeague.backend.security.CustomUserDetailsService;
import com.streetLeague.backend.security.JwtService;
import com.streetLeague.backend.service.CoachingRoleService;
import com.streetLeague.backend.service.ReservationSeanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationSeanceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ReservationSeanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationSeanceService reservationSeanceService;

    @MockBean
    private CoachingRoleService roleService;

    @MockBean
    private AuthenticatedUserResolver userResolver;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void reserver_shouldReturn201() throws Exception {
        ReservationSeanceDTO.Request request = ReservationSeanceDTO.Request.builder().seanceId(10).modePaiement(ModePaiement.SUR_PLACE).build();
        ReservationSeanceDTO.Response response = ReservationSeanceDTO.Response.builder().idReservation(1).build();

        when(userResolver.resolveUserId(any())).thenReturn(1);
        when(reservationSeanceService.reserver(anyInt(), any())).thenReturn(response);

        mockMvc.perform(post("/api/reservations-seances")
                        .header("X-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReservation").value(1));
    }

    @Test
    void getBySeance_shouldReturnList() throws Exception {
        when(reservationSeanceService.getBySeance(10))
                .thenReturn(List.of(ReservationSeanceDTO.Response.builder().idReservation(1).build()));

        mockMvc.perform(get("/api/reservations-seances/seance/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getByUser_shouldReturnList() throws Exception {
        when(reservationSeanceService.getByUser(1))
                .thenReturn(List.of(ReservationSeanceDTO.Response.builder().idReservation(1).build()));

        mockMvc.perform(get("/api/reservations-seances/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getPlacesRestantes_shouldReturnCount() throws Exception {
        when(reservationSeanceService.getPlacesRestantes(10)).thenReturn(3);

        mockMvc.perform(get("/api/reservations-seances/seance/10/places"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placesRestantes").value(3));
    }

    @Test
    void confirmer_shouldSucceed() throws Exception {
        ReservationSeanceDTO.Response response = ReservationSeanceDTO.Response.builder()
                .idReservation(1).build();

        when(userResolver.resolveUserId(any())).thenReturn(2);
        when(roleService.requireCoachOrAdmin(2)).thenReturn(null);
        when(reservationSeanceService.confirmer(1)).thenReturn(response);

        mockMvc.perform(put("/api/reservations-seances/1/confirmer")
                        .header("X-User-Id", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void marquerPaye_shouldSucceed() throws Exception {
        when(userResolver.resolveUserId(any())).thenReturn(2);
        when(roleService.requireCoachOrAdmin(2)).thenReturn(null);
        when(reservationSeanceService.marquerPaye(1))
                .thenReturn(ReservationSeanceDTO.Response.builder().idReservation(1).build());

        mockMvc.perform(put("/api/reservations-seances/1/payer")
                        .header("X-User-Id", "2"))
                .andExpect(status().isOk());
    }
}
