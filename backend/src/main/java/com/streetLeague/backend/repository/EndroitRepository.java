package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeEndroit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EndroitRepository extends JpaRepository<Endroit, Long> {
    List<Endroit> findByType(TypeEndroit type);
    List<Endroit> findByStatut(StatutEndroit statut);
    List<Endroit> findByVille(String ville);
}
