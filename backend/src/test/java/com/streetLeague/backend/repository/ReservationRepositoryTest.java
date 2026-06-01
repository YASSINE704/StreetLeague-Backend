package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.entity.SousEspace;
import com.streetLeague.backend.enums.StatutReservation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReservationRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ReservationRepository reservationRepository;

    private Endroit createEndroit() {
        Endroit e = new Endroit();
        e.setNom("Stade");
        e.setVille("Tunis");
        return em.persist(e);
    }

    private SousEspace createSousEspace(Endroit endroit) {
        SousEspace se = new SousEspace();
        se.setNom("Terrain A");
        se.setEndroit(endroit);
        return em.persist(se);
    }

    private Reservation createReservation(SousEspace se, LocalDateTime debut, LocalDateTime fin, StatutReservation statut) {
        Reservation r = new Reservation();
        r.setDateDebut(debut);
        r.setDateFin(fin);
        r.setStatut(statut);
        r.setSousEspace(se);
        r.setDateCreation(LocalDateTime.now());
        return em.persist(r);
    }

    @Test
    void findBySousEspaceId_shouldReturnReservations() {
        Endroit e = createEndroit();
        SousEspace se = createSousEspace(e);
        createReservation(se, LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.EN_ATTENTE);
        createReservation(se, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), StatutReservation.CONFIRMEE);

        List<Reservation> result = reservationRepository.findBySousEspaceId(se.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    void findByStatut_shouldFilter() {
        Endroit e = createEndroit();
        SousEspace se = createSousEspace(e);
        createReservation(se, LocalDateTime.now(), LocalDateTime.now().plusHours(1), StatutReservation.EN_ATTENTE);
        createReservation(se, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(1), StatutReservation.CONFIRMEE);

        List<Reservation> result = reservationRepository.findByStatut(StatutReservation.CONFIRMEE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutReservation.CONFIRMEE);
    }

    @Test
    void findOverlapping_shouldDetectConflicts() {
        Endroit e = createEndroit();
        SousEspace se = createSousEspace(e);
        createReservation(se,
                LocalDateTime.of(2026, 6, 15, 10, 0),
                LocalDateTime.of(2026, 6, 15, 12, 0),
                StatutReservation.CONFIRMEE);

        List<Reservation> overlapping = reservationRepository.findOverlapping(
                se.getId(),
                LocalDateTime.of(2026, 6, 15, 11, 0),
                LocalDateTime.of(2026, 6, 15, 13, 0),
                List.of(StatutReservation.CONFIRMEE, StatutReservation.EN_ATTENTE));

        assertThat(overlapping).hasSize(1);
    }

    @Test
    void findOverlapping_whenNoConflict_shouldReturnEmpty() {
        Endroit e = createEndroit();
        SousEspace se = createSousEspace(e);
        createReservation(se,
                LocalDateTime.of(2026, 6, 15, 10, 0),
                LocalDateTime.of(2026, 6, 15, 12, 0),
                StatutReservation.CONFIRMEE);

        List<Reservation> overlapping = reservationRepository.findOverlapping(
                se.getId(),
                LocalDateTime.of(2026, 6, 15, 13, 0),
                LocalDateTime.of(2026, 6, 15, 14, 0),
                List.of(StatutReservation.CONFIRMEE, StatutReservation.EN_ATTENTE));

        assertThat(overlapping).isEmpty();
    }

    @Test
    void findOverlapping_withCancelledStatus_shouldIgnore() {
        Endroit e = createEndroit();
        SousEspace se = createSousEspace(e);
        createReservation(se,
                LocalDateTime.of(2026, 6, 15, 10, 0),
                LocalDateTime.of(2026, 6, 15, 12, 0),
                StatutReservation.ANNULEE);

        List<Reservation> overlapping = reservationRepository.findOverlapping(
                se.getId(),
                LocalDateTime.of(2026, 6, 15, 11, 0),
                LocalDateTime.of(2026, 6, 15, 13, 0),
                List.of(StatutReservation.CONFIRMEE, StatutReservation.EN_ATTENTE));

        assertThat(overlapping).isEmpty();
    }
}
