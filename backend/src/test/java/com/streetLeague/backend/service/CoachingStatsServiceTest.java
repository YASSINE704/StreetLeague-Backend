package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.ProgrammeEntrainement;
import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.SuiviSeance;
import com.streetLeague.backend.enums.StatutProgramme;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoachingStatsServiceTest {

    @Mock
    private ProgrammeEntrainementRepository programmeRepository;
    @Mock
    private SeanceEntrainementRepository seanceRepository;
    @Mock
    private ExerciceRepository exerciceRepository;
    @Mock
    private SuiviSeanceRepository suiviRepository;
    @Mock
    private ReservationSeanceRepository reservationRepository;

    @InjectMocks
    private CoachingStatsService statsService;

    @Test
    void getGlobalStats_shouldReturnCorrectCounts() {
        when(programmeRepository.count()).thenReturn(3L);
        when(seanceRepository.count()).thenReturn(10L);
        when(exerciceRepository.count()).thenReturn(25L);
        when(seanceRepository.findByStatut(StatutSeance.REALISEE)).thenReturn(Arrays.asList(new SeanceEntrainement(), new SeanceEntrainement()));
        when(seanceRepository.findByStatut(StatutSeance.PREVUE)).thenReturn(Arrays.asList(new SeanceEntrainement()));
        when(seanceRepository.findByStatut(StatutSeance.ANNULEE)).thenReturn(Collections.emptyList());
        when(suiviRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, Object> stats = statsService.getGlobalStats();

        assertEquals(3L, stats.get("totalProgrammes"));
        assertEquals(10L, stats.get("totalSeances"));
        assertEquals(25L, stats.get("totalExercices"));
        assertEquals(2, ((List<?>) seanceRepository.findByStatut(StatutSeance.REALISEE)).size());
    }

    @Test
    void getGlobalStats_shouldCalculateTauxCompletion() {
        when(programmeRepository.count()).thenReturn(1L);
        when(seanceRepository.count()).thenReturn(4L);
        when(exerciceRepository.count()).thenReturn(10L);

        List<SeanceEntrainement> realisees = Arrays.asList(new SeanceEntrainement(), new SeanceEntrainement());
        when(seanceRepository.findByStatut(StatutSeance.REALISEE)).thenReturn(realisees);
        when(seanceRepository.findByStatut(StatutSeance.PREVUE)).thenReturn(Arrays.asList(new SeanceEntrainement()));
        when(seanceRepository.findByStatut(StatutSeance.ANNULEE)).thenReturn(Arrays.asList(new SeanceEntrainement()));
        when(suiviRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, Object> stats = statsService.getGlobalStats();

        assertEquals(50L, stats.get("tauxCompletion")); // 2/4 = 50%
    }

    @Test
    void getGlobalStats_shouldCalculateAverages_whenSuivisExist() {
        when(programmeRepository.count()).thenReturn(1L);
        when(seanceRepository.count()).thenReturn(2L);
        when(exerciceRepository.count()).thenReturn(5L);
        when(seanceRepository.findByStatut(StatutSeance.REALISEE)).thenReturn(Collections.emptyList());
        when(seanceRepository.findByStatut(StatutSeance.PREVUE)).thenReturn(Collections.emptyList());
        when(seanceRepository.findByStatut(StatutSeance.ANNULEE)).thenReturn(Collections.emptyList());

        SuiviSeance s1 = SuiviSeance.builder().ressenti(8).fatigue(6).note(4).build();
        SuiviSeance s2 = SuiviSeance.builder().ressenti(6).fatigue(4).note(5).build();
        when(suiviRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        Map<String, Object> stats = statsService.getGlobalStats();

        assertEquals(7.0, stats.get("moyenneRessenti")); // (8+6)/2
        assertEquals(5.0, stats.get("moyenneFatigue")); // (6+4)/2
        assertEquals(4.5, stats.get("moyenneNote")); // (4+5)/2
    }

    @Test
    void getProgrammeStats_shouldReturnEmpty_whenNotFound() {
        when(programmeRepository.findById(99)).thenReturn(Optional.empty());

        Map<String, Object> stats = statsService.getProgrammeStats(99);

        assertTrue(stats.isEmpty());
    }

    @Test
    void getProgrammeStats_shouldReturnStats_whenFound() {
        ProgrammeEntrainement programme = ProgrammeEntrainement.builder()
                .idProgramme(1)
                .titre("Programme Force")
                .statut(StatutProgramme.ACTIF)
                .build();

        SeanceEntrainement s1 = SeanceEntrainement.builder().statut(StatutSeance.REALISEE).suiviSeances(new ArrayList<>()).build();
        SeanceEntrainement s2 = SeanceEntrainement.builder().statut(StatutSeance.PREVUE).suiviSeances(new ArrayList<>()).build();
        programme.setSeances(Arrays.asList(s1, s2));

        when(programmeRepository.findById(1)).thenReturn(Optional.of(programme));

        Map<String, Object> stats = statsService.getProgrammeStats(1);

        assertEquals("Programme Force", stats.get("titreProgramme"));
        assertEquals(2, stats.get("totalSeances"));
        assertEquals(1L, stats.get("seancesRealisees"));
        assertEquals(50L, stats.get("tauxCompletion"));
    }
}
