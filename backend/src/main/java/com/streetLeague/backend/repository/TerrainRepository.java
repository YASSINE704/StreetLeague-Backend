package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Terrain (Field/Court) entity.
 * 
 * Provides database access and CRUD operations for terrain records.
 * Extends JpaRepository to inherit standard Spring Data JPA functionality.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Repository
public interface TerrainRepository extends JpaRepository<Terrain, Long> {
}
