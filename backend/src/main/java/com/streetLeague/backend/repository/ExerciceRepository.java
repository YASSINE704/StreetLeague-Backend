package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Exercice;
import com.streetLeague.backend.enums.TypeExercice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciceRepository extends JpaRepository<Exercice, Integer> {
    List<Exercice> findByType(TypeExercice type);
}
