package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.SuiviSeance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuiviSeanceRepository extends JpaRepository<SuiviSeance, Integer> {
    Optional<SuiviSeance> findBySeanceIdSeance(Integer seanceId);
}
