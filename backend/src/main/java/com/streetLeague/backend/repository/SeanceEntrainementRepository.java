package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.SeanceEntrainement;
import com.streetLeague.backend.enums.StatutSeance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeanceEntrainementRepository extends JpaRepository<SeanceEntrainement, Integer> {
    List<SeanceEntrainement> findByProgrammeIdProgramme(Integer programmeId);
    List<SeanceEntrainement> findByStatut(StatutSeance statut);
}
