package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.SeanceExercice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeanceExerciceRepository extends JpaRepository<SeanceExercice, Integer> {
    List<SeanceExercice> findBySeanceIdSeanceOrderByOrdreAsc(Integer seanceId);
}
