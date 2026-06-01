package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.entity.SousEspace;
import com.streetLeague.backend.enums.StatutReservation;
import com.streetLeague.backend.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SousEspaceService sousEspaceService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation createReservation(LocalDateTime debut, LocalDateTime fin, StatutReservation statut) {
        Reservation r = new Reservation();
        r.setId(1L);
        r.setDateDebut(debut);
        r.setDateFin(fin);
        r.setStatut(statut);
        r.setDateCreation(LocalDateTime.now());
        return r;
    }

    @Test
    void getAllReservations_shouldReturnList() {
        when(reservationRepository.findAll()).thenReturn(List.of(new Reservation(), new Reservation()));

        List<Reservation> result = reservationService.getAllReservations();

        assertThat(result).hasSize(2);
    }

    @Test
    void getReservationById_whenFound_shouldReturn() {
        Reservation r = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.EN_ATTENTE);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));

        Reservation result = reservationService.getReservationById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getReservationById_whenNotFound_shouldThrow() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.getReservationById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void createReservation_shouldSucceed() {
        SousEspace se = new SousEspace();
        se.setId(1L);
        se.setNom("Terrain A");
        Endroit endroit = new Endroit();
        endroit.setNom("Stade");
        se.setEndroit(endroit);

        Reservation r = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(2), null);

        when(sousEspaceService.getSousEspaceById(1L)).thenReturn(se);
        when(reservationRepository.findOverlapping(anyLong(), any(), any(), any())).thenReturn(List.of());
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.createReservation(1L, r);

        assertThat(result.getStatut()).isEqualTo(StatutReservation.EN_ATTENTE);
        assertThat(result.getDateCreation()).isNotNull();
        assertThat(result.getSousEspace()).isEqualTo(se);
        verify(notificationService).notifyReservationCreated(anyLong(), eq("Terrain A"), eq("Stade"));
    }

    @Test
    void createReservation_whenDateDebutAfterDateFin_shouldThrow() {
        Reservation r = createReservation(LocalDateTime.now().plusHours(2), LocalDateTime.now(), null);

        assertThatThrownBy(() -> reservationService.createReservation(1L, r))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("doit être avant");
    }

    @Test
    void createReservation_whenDateDebutEqualsDateFin_shouldThrow() {
        LocalDateTime same = LocalDateTime.now();
        Reservation r = createReservation(same, same, null);

        assertThatThrownBy(() -> reservationService.createReservation(1L, r))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ne peut pas être égale");
    }

    @Test
    void createReservation_whenOverlappingExists_shouldThrow() {
        SousEspace se = new SousEspace();
        se.setId(1L);
        Reservation r = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(2), null);

        Reservation overlapping = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.CONFIRMEE);
        when(reservationRepository.findOverlapping(anyLong(), any(), any(), any())).thenReturn(List.of(overlapping));

        assertThatThrownBy(() -> reservationService.createReservation(1L, r))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("déjà une réservation");
    }

    @Test
    void updateReservation_shouldSucceed() {
        Reservation existing = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.EN_ATTENTE);
        Reservation details = new Reservation();
        details.setDateDebut(LocalDateTime.now().plusHours(3));
        details.setDateFin(LocalDateTime.now().plusHours(4));
        details.setStatut(StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.updateReservation(1L, details);

        assertThat(result.getStatut()).isEqualTo(StatutReservation.CONFIRMEE);
    }

    @Test
    void deleteReservation_shouldSucceed() {
        reservationService.deleteReservation(1L);
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    void confirmerReservation_shouldSucceed() {
        SousEspace se = new SousEspace();
        se.setId(1L);
        Reservation r = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.EN_ATTENTE);
        r.setSousEspace(se);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservationRepository.findOverlapping(anyLong(), any(), any(), any())).thenReturn(List.of());
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.confirmerReservation(1L);

        assertThat(result.getStatut()).isEqualTo(StatutReservation.CONFIRMEE);
        verify(notificationService).notifyReservationConfirmed(anyLong(), any(), any());
    }

    @Test
    void confirmerReservation_whenOverlappingConfirmee_shouldThrow() {
        SousEspace se = new SousEspace();
        se.setId(1L);
        Reservation r = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.EN_ATTENTE);
        r.setSousEspace(se);

        Reservation overlapping = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservationRepository.findOverlapping(anyLong(), any(), any(), any())).thenReturn(List.of(overlapping));

        assertThatThrownBy(() -> reservationService.confirmerReservation(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Impossible de confirmer");
    }

    @Test
    void annulerReservation_shouldSucceed() {
        SousEspace se = new SousEspace();
        se.setNom("Terrain A");
        Endroit endroit = new Endroit();
        endroit.setNom("Stade");
        se.setEndroit(endroit);
        Reservation r = createReservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.EN_ATTENTE);
        r.setSousEspace(se);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(r));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Reservation result = reservationService.annulerReservation(1L, "Plus besoin");

        assertThat(result.getStatut()).isEqualTo(StatutReservation.ANNULEE);
        assertThat(result.getMotifAnnulation()).isEqualTo("Plus besoin");
        verify(notificationService).notifyReservationCancelled(anyLong(), any(), any());
    }

    @Test
    void getReservationsBySousEspaceId_shouldReturnList() {
        when(reservationRepository.findBySousEspaceId(1L)).thenReturn(List.of(new Reservation()));

        List<Reservation> result = reservationService.getReservationsBySousEspaceId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getReservationsByStatut_shouldFilter() {
        when(reservationRepository.findByStatut(StatutReservation.CONFIRMEE)).thenReturn(List.of(new Reservation()));

        List<Reservation> result = reservationService.getReservationsByStatut(StatutReservation.CONFIRMEE);

        assertThat(result).hasSize(1);
    }
}
