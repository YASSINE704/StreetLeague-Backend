package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Equipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Equipe (Team) entity.
 * 
 * Provides database access and CRUD operations for team records.
 * Extends JpaRepository to inherit standard Spring Data JPA functionality.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
}
