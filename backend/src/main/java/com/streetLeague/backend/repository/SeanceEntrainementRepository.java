package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.enums.StatutSeance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SeanceEntrainementRepository extends JpaRepository<SeanceEntrainement, Integer> {
    List<SeanceEntrainement> findByProgrammeIdProgramme(Integer programmeId);
    List<SeanceEntrainement> findByStatut(StatutSeance statut);

    /** Step 3 : trouver les séances d'un programme à une date donnée (pour détecter les conflits coach) */
    List<SeanceEntrainement> findByProgrammeIdProgrammeAndDateSeance(Integer programmeId, LocalDate dateSeance);

    /** Step 3 : trouver toutes les séances à une date donnée */
    List<SeanceEntrainement> findByDateSeance(LocalDate dateSeance);
}
