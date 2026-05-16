package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeEndroit;
import com.streetLeague.backend.repository.EndroitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EndroitService {
    private final EndroitRepository endroitRepository;

    public List<Endroit> getAllEndroits() {
        return endroitRepository.findAll();
    }

    public Endroit getEndroitById(Long id) {
        return endroitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Endroit not found with id: " + id));
    }

    public Endroit createEndroit(Endroit endroit) {
        if (endroit.getCapacite() != null && endroit.getCapacite() < 0) {
            throw new IllegalArgumentException("La capacité ne peut pas être négative");
        }
        return endroitRepository.save(endroit);
    }

    public Endroit updateEndroit(Long id, Endroit endroitDetails) {
        if (endroitDetails.getCapacite() != null && endroitDetails.getCapacite() < 0) {
            throw new IllegalArgumentException("La capacité ne peut pas être négative");
        }
        Endroit endroit = getEndroitById(id);
        endroit.setNom(endroitDetails.getNom());
        endroit.setType(endroitDetails.getType());
        endroit.setAdresse(endroitDetails.getAdresse());
        endroit.setVille(endroitDetails.getVille());
        endroit.setLatitude(endroitDetails.getLatitude());
        endroit.setLongitude(endroitDetails.getLongitude());
        endroit.setCapacite(endroitDetails.getCapacite());
        endroit.setStatut(endroitDetails.getStatut());
        endroit.setDescription(endroitDetails.getDescription());
        endroit.setImageUrl(endroitDetails.getImageUrl());
        return endroitRepository.save(endroit);
    }

    public void deleteEndroit(Long id) {
        endroitRepository.deleteById(id);
    }

    public List<Endroit> getEndroitsByType(TypeEndroit type) {
        return endroitRepository.findByType(type);
    }

    public List<Endroit> getEndroitsByStatut(StatutEndroit statut) {
        return endroitRepository.findByStatut(statut);
    }

    public List<Endroit> getEndroitsByVille(String ville) {
        return endroitRepository.findByVille(ville);
    }
}
