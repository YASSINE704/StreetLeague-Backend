package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.AffectationProgramme;
import com.streetLeague.backend.enums.TypeAffectationProgramme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AffectationProgrammeRepository extends JpaRepository<AffectationProgramme, Integer> {
    List<AffectationProgramme> findByProgrammeIdProgramme(Integer programmeId);
    List<AffectationProgramme> findByUserIdUser(Integer userId);
    Optional<AffectationProgramme> findByProgrammeIdProgrammeAndType(Integer programmeId, TypeAffectationProgramme type);

    /** Step 3 : trouver tous les programmes où un utilisateur est affecté avec un type donné */
    List<AffectationProgramme> findByUserIdUserAndType(Integer userId, TypeAffectationProgramme type);
}
