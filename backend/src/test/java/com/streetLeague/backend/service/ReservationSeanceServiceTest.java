package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.ReservationSeanceDTO;
import com.streetLeague.backend.entity.ReservationSeance;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.ModePaiement;
import com.streetLeague.backend.enums.StatutPaiement;
import com.streetLeague.backend.enums.StatutReservationSeance;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.ReservationSeanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationSeanceServiceTest {

    @Mock
    private ReservationSeanceRepository reservationRepository;

    @Mock
    private SeanceEntrainementService seanceService;

    @Mock
    private CoachingRoleService roleService;

    @InjectMocks
    private ReservationSeanceService reservationSeanceService;

    private User createUser(Integer id) {
        return User.builder().idUser(id).nom("Test").prenom("User").build();
    }

    private SeanceEntrainement createSeance(Integer id, StatutSeance statut, LocalDate date,
                                             LocalTime debut, LocalTime fin, int maxParticipants) {
        return SeanceEntrainement.builder()
                .idSeance(id)
                .titreSeance("Seance " + id)
                .dateSeance(date)
                .heureDebut(debut)
                .heureFin(fin)
                .statut(statut)
                .maxParticipants(maxParticipants)
                .build();
    }

    @Test
    void reserver_shouldSucceed() {
        User user = createUser(1);
        SeanceEntrainement seance = createSeance(10, StatutSeance.PREVUE,
                LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 5);

        ReservationSeanceDTO.Request dto = ReservationSeanceDTO.Request.builder()
                .seanceId(10).modePaiement(ModePaiement.SUR_PLACE).build();

        when(roleService.requireSportifOrCoachOrAdmin(1)).thenReturn(user);
        when(seanceService.findOrThrow(10)).thenReturn(seance);
        when(reservationRepository.findBySeanceIdSeanceAndStatutNot(eq(10), any())).thenReturn(List.of());
        when(reservationRepository.findByUserIdUserAndSeanceIdSeanceAndStatutNot(eq(1), eq(10), any())).thenReturn(Optional.empty());
        when(reservationRepository.findByUserIdUserAndStatutNot(eq(1), any())).thenReturn(List.of());
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ReservationSeanceDTO.Response result = reservationSeanceService.reserver(1, dto);

        assertThat(result.getStatut()).isEqualTo(StatutReservationSeance.RESERVEE);
        assertThat(result.getModePaiement()).isEqualTo(ModePaiement.SUR_PLACE);
        assertThat(result.getStatutPaiement()).isEqualTo(StatutPaiement.EN_ATTENTE);
    }

    @Test
    void reserver_whenSeanceNotPrevue_shouldThrow() {
        User user = createUser(1);
        SeanceEntrainement seance = createSeance(10, StatutSeance.REALISEE,
                LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 5);

        ReservationSeanceDTO.Request dto = ReservationSeanceDTO.Request.builder()
                .seanceId(10).modePaiement(ModePaiement.SUR_PLACE).build();

        when(roleService.requireSportifOrCoachOrAdmin(1)).thenReturn(user);
        when(seanceService.findOrThrow(10)).thenReturn(seance);

        assertThatThrownBy(() -> reservationSeanceService.reserver(1, dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("pas disponible");
    }

    @Test
    void reserver_whenCapacityFull_shouldThrow() {
        User user = createUser(1);
        SeanceEntrainement seance = createSeance(10, StatutSeance.PREVUE,
                LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 2);

        ReservationSeanceDTO.Request dto = ReservationSeanceDTO.Request.builder()
                .seanceId(10).modePaiement(ModePaiement.SUR_PLACE).build();

        when(roleService.requireSportifOrCoachOrAdmin(1)).thenReturn(user);
        when(seanceService.findOrThrow(10)).thenReturn(seance);
        when(reservationRepository.findBySeanceIdSeanceAndStatutNot(eq(10), any()))
                .thenReturn(List.of(new ReservationSeance(), new ReservationSeance()));

        assertThatThrownBy(() -> reservationSeanceService.reserver(1, dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("complète");
    }

    @Test
    void reserver_whenDuplicate_shouldThrow() {
        User user = createUser(1);
        SeanceEntrainement seance = createSeance(10, StatutSeance.PREVUE,
                LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0), 5);

        ReservationSeanceDTO.Request dto = ReservationSeanceDTO.Request.builder()
                .seanceId(10).modePaiement(ModePaiement.SUR_PLACE).build();

        when(roleService.requireSportifOrCoachOrAdmin(1)).thenReturn(user);
        when(seanceService.findOrThrow(10)).thenReturn(seance);
        when(reservationRepository.findBySeanceIdSeanceAndStatutNot(eq(10), any())).thenReturn(List.of());
        when(reservationRepository.findByUserIdUserAndSeanceIdSeanceAndStatutNot(eq(1), eq(10), any()))
                .thenReturn(Optional.of(new ReservationSeance()));

        assertThatThrownBy(() -> reservationSeanceService.reserver(1, dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("déjà réservé");
    }

    @Test
    void reserver_whenTimeOverlap_shouldThrow() {
        User user = createUser(1);
        SeanceEntrainement seance = createSeance(10, StatutSeance.PREVUE,
                LocalDate.of(2026, 6, 15), LocalTime.of(10, 0), LocalTime.of(12, 0), 5);

        SeanceEntrainement existingSeance = createSeance(20, StatutSeance.PREVUE,
                LocalDate.of(2026, 6, 15), LocalTime.of(11, 0), LocalTime.of(13, 0), 5);
        ReservationSeance existingReservation = new ReservationSeance();
        existingReservation.setSeance(existingSeance);

        ReservationSeanceDTO.Request dto = ReservationSeanceDTO.Request.builder()
                .seanceId(10).modePaiement(ModePaiement.SUR_PLACE).build();

        when(roleService.requireSportifOrCoachOrAdmin(1)).thenReturn(user);
        when(seanceService.findOrThrow(10)).thenReturn(seance);
        when(reservationRepository.findBySeanceIdSeanceAndStatutNot(eq(10), any())).thenReturn(List.of());
        when(reservationRepository.findByUserIdUserAndSeanceIdSeanceAndStatutNot(eq(1), eq(10), any()))
                .thenReturn(Optional.empty());
        when(reservationRepository.findByUserIdUserAndStatutNot(eq(1), any()))
                .thenReturn(List.of(existingReservation));

        assertThatThrownBy(() -> reservationSeanceService.reserver(1, dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Conflit horaire");
    }

    @Test
    void annuler_shouldSucceed() {
        ReservationSeance r = ReservationSeance.builder()
                .idReservation(1).statut(StatutReservationSeance.RESERVEE)
                .user(createUser(1))
                .seance(createSeance(10, StatutSeance.PREVUE, LocalDate.now(), null, null, 5))
                .build();

        when(reservationRepository.findById(1)).thenReturn(Optional.of(r));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ReservationSeanceDTO.Response result = reservationSeanceService.annuler(1, "Test annulation");

        assertThat(result.getStatut()).isEqualTo(StatutReservationSeance.ANNULEE);
        assertThat(result.getMotifAnnulation()).isEqualTo("Test annulation");
    }

    @Test
    void annuler_whenAlreadyCancelled_shouldThrow() {
        ReservationSeance r = ReservationSeance.builder()
                .idReservation(1).statut(StatutReservationSeance.ANNULEE).build();

        when(reservationRepository.findById(1)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> reservationSeanceService.annuler(1, "motif"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("déjà annulée");
    }

    @Test
    void confirmer_shouldSucceed() {
        ReservationSeance r = ReservationSeance.builder()
                .idReservation(1).statut(StatutReservationSeance.RESERVEE)
                .user(createUser(1))
                .seance(createSeance(10, StatutSeance.PREVUE, LocalDate.now(), null, null, 5))
                .build();

        when(reservationRepository.findById(1)).thenReturn(Optional.of(r));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ReservationSeanceDTO.Response result = reservationSeanceService.confirmer(1);

        assertThat(result.getStatut()).isEqualTo(StatutReservationSeance.CONFIRMEE);
    }

    @Test
    void confirmer_whenNotReservee_shouldThrow() {
        ReservationSeance r = ReservationSeance.builder()
                .idReservation(1).statut(StatutReservationSeance.ANNULEE).build();

        when(reservationRepository.findById(1)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> reservationSeanceService.confirmer(1))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("en attente");
    }

    @Test
    void marquerPaye_shouldSucceed() {
        ReservationSeance r = ReservationSeance.builder()
                .idReservation(1).statut(StatutReservationSeance.RESERVEE)
                .user(createUser(1))
                .seance(createSeance(10, StatutSeance.PREVUE, LocalDate.now(), null, null, 5))
                .build();

        when(reservationRepository.findById(1)).thenReturn(Optional.of(r));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ReservationSeanceDTO.Response result = reservationSeanceService.marquerPaye(1);

        assertThat(result.getStatutPaiement()).isEqualTo(StatutPaiement.PAYE);
    }

    @Test
    void marquerPaye_whenAnnulee_shouldThrow() {
        ReservationSeance r = ReservationSeance.builder()
                .idReservation(1).statut(StatutReservationSeance.ANNULEE).build();

        when(reservationRepository.findById(1)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> reservationSeanceService.marquerPaye(1))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("annulée");
    }

    @Test
    void getPlacesRestantes_shouldReturnCorrectCount() {
        SeanceEntrainement seance = SeanceEntrainement.builder()
                .idSeance(10).maxParticipants(5).build();
        when(seanceService.findOrThrow(10)).thenReturn(seance);
        when(reservationRepository.findBySeanceIdSeanceAndStatutNot(eq(10), any()))
                .thenReturn(List.of(new ReservationSeance(), new ReservationSeance()));

        int places = reservationSeanceService.getPlacesRestantes(10);

        assertThat(places).isEqualTo(3);
    }

    @Test
    void getBySeance_shouldReturnList() {
        when(reservationRepository.findBySeanceIdSeance(10)).thenReturn(List.of(
                createReservationSeance(1), createReservationSeance(2)
        ));

        var result = reservationSeanceService.getBySeance(10);

        assertThat(result).hasSize(2);
    }

    @Test
    void getByUser_shouldReturnList() {
        when(reservationRepository.findByUserIdUser(1)).thenReturn(List.of(
                createReservationSeance(1)
        ));

        var result = reservationSeanceService.getByUser(1);

        assertThat(result).hasSize(1);
    }

    @Test
    void annuler_whenNotFound_shouldThrow() {
        when(reservationRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationSeanceService.annuler(99, "motif"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private ReservationSeance createReservationSeance(int id) {
        return ReservationSeance.builder()
                .idReservation(id)
                .statut(StatutReservationSeance.RESERVEE)
                .user(createUser(1))
                .seance(createSeance(10, StatutSeance.PREVUE, LocalDate.now(), null, null, 5))
                .build();
    }
}
