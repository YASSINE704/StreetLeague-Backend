package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.ReservationSeance;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.enums.StatutReservationSeance;
import com.streetLeague.backend.enums.StatutSeance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReservationSeanceRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ReservationSeanceRepository reservationSeanceRepository;

    private User createUser() {
        return em.persist(User.builder().nom("Test").prenom("User").email("test@test.com")
                .role(Role.SPORTIF).build());
    }

    private SeanceEntrainement createSeance() {
        return em.persist(SeanceEntrainement.builder()
                .titreSeance("Seance Test")
                .statut(StatutSeance.PREVUE)
                .maxParticipants(5)
                .build());
    }

    private ReservationSeance createReservation(User user, SeanceEntrainement seance, StatutReservationSeance statut) {
        return em.persist(ReservationSeance.builder()
                .user(user).seance(seance).statut(statut)
                .build());
    }

    @Test
    void findBySeanceIdSeanceAndStatutNot_shouldFilter() {
        User user = createUser();
        SeanceEntrainement seance = createSeance();
        createReservation(user, seance, StatutReservationSeance.RESERVEE);
        createReservation(user, seance, StatutReservationSeance.ANNULEE);

        var result = reservationSeanceRepository
                .findBySeanceIdSeanceAndStatutNot(seance.getIdSeance(), StatutReservationSeance.ANNULEE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatut()).isEqualTo(StatutReservationSeance.RESERVEE);
    }

    @Test
    void findByUserIdUserAndSeanceIdSeanceAndStatutNot_shouldFind() {
        User user = createUser();
        SeanceEntrainement seance = createSeance();
        createReservation(user, seance, StatutReservationSeance.RESERVEE);

        Optional<ReservationSeance> result = reservationSeanceRepository
                .findByUserIdUserAndSeanceIdSeanceAndStatutNot(user.getIdUser(), seance.getIdSeance(), StatutReservationSeance.ANNULEE);

        assertThat(result).isPresent();
    }

    @Test
    void findByUserIdUserAndSeanceIdSeanceAndStatutNot_whenCancelled_shouldNotFind() {
        User user = createUser();
        SeanceEntrainement seance = createSeance();
        createReservation(user, seance, StatutReservationSeance.ANNULEE);

        Optional<ReservationSeance> result = reservationSeanceRepository
                .findByUserIdUserAndSeanceIdSeanceAndStatutNot(user.getIdUser(), seance.getIdSeance(), StatutReservationSeance.ANNULEE);

        assertThat(result).isNotPresent();
    }

    @Test
    void findByUserIdUserAndStatutNot_shouldReturnActive() {
        User user = createUser();
        SeanceEntrainement s1 = createSeance();
        SeanceEntrainement s2 = createSeance();
        createReservation(user, s1, StatutReservationSeance.RESERVEE);
        createReservation(user, s2, StatutReservationSeance.ANNULEE);

        var result = reservationSeanceRepository
                .findByUserIdUserAndStatutNot(user.getIdUser(), StatutReservationSeance.ANNULEE);

        assertThat(result).hasSize(1);
    }
}
