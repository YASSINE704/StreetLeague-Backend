package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipementRepository extends JpaRepository<Equipement, Long> {
    List<Equipement> findBySousEspaceId(Long sousEspaceId);
}
