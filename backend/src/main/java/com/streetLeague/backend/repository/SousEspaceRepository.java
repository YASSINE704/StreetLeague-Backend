package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.SousEspace;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeSousEspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SousEspaceRepository extends JpaRepository<SousEspace, Long> {
    List<SousEspace> findByEndroitId(Long endroitId);
    List<SousEspace> findByType(TypeSousEspace type);
    List<SousEspace> findByStatut(StatutEndroit statut);
}
