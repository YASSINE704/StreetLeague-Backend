package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.entity.SuiviSeance;
import com.streetLeague.backend.enums.StatutReservationSeance;
import com.streetLeague.backend.enums.StatutSeance;
import com.streetLeague.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Step 7 : Service de statistiques pour le module coaching.
 * Fournit des métriques sur les programmes, séances, exercices et suivis.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoachingStatsService {

    private final ProgrammeEntrainementRepository programmeRepository;
    private final SeanceEntrainementRepository seanceRepository;
    private final ExerciceRepository exerciceRepository;
    private final SuiviSeanceRepository suiviRepository;
    private final ReservationSeanceRepository reservationRepository;

    /**
     * Statistiques globales du module coaching.
     */
    public Map<String, Object> getGlobalStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        long totalProgrammes = programmeRepository.count();
        long totalSeances = seanceRepository.count();
        long totalExercices = exerciceRepository.count();
        long seancesRealisees = seanceRepository.findByStatut(StatutSeance.REALISEE).size();
        long seancesPrevues = seanceRepository.findByStatut(StatutSeance.PREVUE).size();
        long seancesAnnulees = seanceRepository.findByStatut(StatutSeance.ANNULEE).size();

        stats.put("totalProgrammes", totalProgrammes);
        stats.put("totalSeances", totalSeances);
        stats.put("totalExercices", totalExercices);
        stats.put("seancesRealisees", seancesRealisees);
        stats.put("seancesPrevues", seancesPrevues);
        stats.put("seancesAnnulees", seancesAnnulees);
        stats.put("tauxCompletion", totalSeances > 0
                ? Math.round((double) seancesRealisees / totalSeances * 100) : 0);

        // Moyennes des suivis
        List<SuiviSeance> suivis = suiviRepository.findAll();
        if (!suivis.isEmpty()) {
            double avgRessenti = suivis.stream().mapToInt(SuiviSeance::getRessenti).average().orElse(0);
            double avgFatigue = suivis.stream().mapToInt(SuiviSeance::getFatigue).average().orElse(0);
            double avgNote = suivis.stream()
                    .filter(s -> s.getNote() != null)
                    .mapToInt(SuiviSeance::getNote).average().orElse(0);

            stats.put("moyenneRessenti", Math.round(avgRessenti * 10.0) / 10.0);
            stats.put("moyenneFatigue", Math.round(avgFatigue * 10.0) / 10.0);
            stats.put("moyenneNote", Math.round(avgNote * 10.0) / 10.0);
            stats.put("totalSuivis", suivis.size());
        } else {
            stats.put("moyenneRessenti", 0);
            stats.put("moyenneFatigue", 0);
            stats.put("moyenneNote", 0);
            stats.put("totalSuivis", 0);
        }

        return stats;
    }

    /**
     * Statistiques d'une séance spécifique.
     */
    public Map<String, Object> getSeanceStats(Integer seanceId) {
        Map<String, Object> stats = new LinkedHashMap<>();

        SeanceEntrainement seance = seanceRepository.findById(seanceId).orElse(null);
        if (seance == null) return stats;

        long nbReservations = reservationRepository
                .findBySeanceIdSeanceAndStatutNot(seanceId, StatutReservationSeance.ANNULEE).size();
        int maxPlaces = seance.getMaxParticipants() != null ? seance.getMaxParticipants() : 5;

        stats.put("titreSeance", seance.getTitreSeance());
        stats.put("statut", seance.getStatut());
        stats.put("nbParticipants", nbReservations);
        stats.put("maxParticipants", maxPlaces);
        stats.put("tauxRemplissage", maxPlaces > 0
                ? Math.round((double) nbReservations / maxPlaces * 100) : 0);
        stats.put("nbExercices", seance.getSeanceExercices() != null ? seance.getSeanceExercices().size() : 0);

        // Suivi
        if (seance.getSuiviSeances() != null && !seance.getSuiviSeances().isEmpty()) {
            SuiviSeance suivi = seance.getSuiviSeances().get(0);
            stats.put("ressenti", suivi.getRessenti());
            stats.put("fatigue", suivi.getFatigue());
            stats.put("note", suivi.getNote());
            stats.put("commentaire", suivi.getCommentaire());
        }

        return stats;
    }

    /**
     * Statistiques d'un programme spécifique.
     */
    public Map<String, Object> getProgrammeStats(Integer programmeId) {
        Map<String, Object> stats = new LinkedHashMap<>();

        var programme = programmeRepository.findById(programmeId).orElse(null);
        if (programme == null) return stats;

        List<SeanceEntrainement> seances = programme.getSeances();
        long realisees = seances.stream().filter(s -> s.getStatut() == StatutSeance.REALISEE).count();
        long prevues = seances.stream().filter(s -> s.getStatut() == StatutSeance.PREVUE).count();

        stats.put("titreProgramme", programme.getTitre());
        stats.put("statut", programme.getStatut());
        stats.put("totalSeances", seances.size());
        stats.put("seancesRealisees", realisees);
        stats.put("seancesPrevues", prevues);
        stats.put("tauxCompletion", seances.size() > 0
                ? Math.round((double) realisees / seances.size() * 100) : 0);

        // Moyennes des suivis du programme
        List<SuiviSeance> suivis = seances.stream()
                .filter(s -> s.getSuiviSeances() != null && !s.getSuiviSeances().isEmpty())
                .flatMap(s -> s.getSuiviSeances().stream())
                .toList();

        if (!suivis.isEmpty()) {
            stats.put("moyenneRessenti", Math.round(suivis.stream().mapToInt(SuiviSeance::getRessenti).average().orElse(0) * 10.0) / 10.0);
            stats.put("moyenneFatigue", Math.round(suivis.stream().mapToInt(SuiviSeance::getFatigue).average().orElse(0) * 10.0) / 10.0);
            stats.put("moyenneNote", Math.round(suivis.stream().filter(s -> s.getNote() != null).mapToInt(SuiviSeance::getNote).average().orElse(0) * 10.0) / 10.0);
        }

        return stats;
    }
}
