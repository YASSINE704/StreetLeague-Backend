package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.ProgrammeEntrainement;
import com.streetLeague.backend.enums.StatutProgramme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgrammeEntrainementRepository extends JpaRepository<ProgrammeEntrainement, Integer> {
    List<ProgrammeEntrainement> findByStatut(StatutProgramme statut);
}
