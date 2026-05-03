package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.entity.SousEspace;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeSousEspace;
import com.streetLeague.backend.repository.SousEspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SousEspaceService {
    private final SousEspaceRepository sousEspaceRepository;
    private final EndroitService endroitService;

    public List<SousEspace> getAllSousEspaces() {
        return sousEspaceRepository.findAll();
    }

    public SousEspace getSousEspaceById(Long id) {
        return sousEspaceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SousEspace not found with id: " + id));
    }

    public SousEspace createSousEspace(Long endroitId, SousEspace sousEspace) {
        if (sousEspace.getCapacite() != null && sousEspace.getCapacite() < 0) {
            throw new IllegalArgumentException("La capacité ne peut pas être négative");
        }
        Endroit endroit = endroitService.getEndroitById(endroitId);
        if (sousEspace.getCapacite() != null && endroit.getCapacite() != null) {
            int totalExistant = sousEspaceRepository.findByEndroitId(endroitId).stream()
                    .mapToInt(se -> se.getCapacite() != null ? se.getCapacite() : 0)
                    .sum();
            int restant = endroit.getCapacite() - totalExistant;
            if (sousEspace.getCapacite() > restant) {
                throw new IllegalArgumentException(
                    "Capacité insuffisante. L'endroit a une capacité de " + endroit.getCapacite() +
                    ", déjà " + totalExistant + " utilisée. Reste disponible: " + restant
                );
            }
        }
        sousEspace.setEndroit(endroit);
        return sousEspaceRepository.save(sousEspace);
    }

    public SousEspace updateSousEspace(Long id, SousEspace sousEspaceDetails) {
        if (sousEspaceDetails.getCapacite() != null && sousEspaceDetails.getCapacite() < 0) {
            throw new IllegalArgumentException("La capacité ne peut pas être négative");
        }
        SousEspace sousEspace = getSousEspaceById(id);
        if (sousEspaceDetails.getCapacite() != null && sousEspace.getEndroit() != null
                && sousEspace.getEndroit().getCapacite() != null) {
            int totalAutres = sousEspaceRepository.findByEndroitId(sousEspace.getEndroit().getId()).stream()
                    .filter(se -> !se.getId().equals(id))
                    .mapToInt(se -> se.getCapacite() != null ? se.getCapacite() : 0)
                    .sum();
            int restant = sousEspace.getEndroit().getCapacite() - totalAutres;
            if (sousEspaceDetails.getCapacite() > restant) {
                throw new IllegalArgumentException(
                    "Capacité insuffisante. L'endroit a une capacité de " + sousEspace.getEndroit().getCapacite() +
                    ", déjà " + totalAutres + " utilisée par les autres sous-espaces. Reste disponible: " + restant
                );
            }
        }
        sousEspace.setNom(sousEspaceDetails.getNom());
        sousEspace.setType(sousEspaceDetails.getType());
        sousEspace.setCapacite(sousEspaceDetails.getCapacite());
        sousEspace.setStatut(sousEspaceDetails.getStatut());
        return sousEspaceRepository.save(sousEspace);
    }

    public void deleteSousEspace(Long id) {
        sousEspaceRepository.deleteById(id);
    }

    public List<SousEspace> getSousEspacesByEndroitId(Long endroitId) {
        return sousEspaceRepository.findByEndroitId(endroitId);
    }

    public List<SousEspace> getSousEspacesByType(TypeSousEspace type) {
        return sousEspaceRepository.findByType(type);
    }

    public List<SousEspace> getSousEspacesByStatut(StatutEndroit statut) {
        return sousEspaceRepository.findByStatut(statut);
    }
}
