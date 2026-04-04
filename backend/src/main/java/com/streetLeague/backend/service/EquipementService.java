package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.Equipement;
import com.streetLeague.backend.entity.SousEspace;
import com.streetLeague.backend.repository.EquipementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipementService {
    private final EquipementRepository equipementRepository;
    private final SousEspaceService sousEspaceService;

    public List<Equipement> getAllEquipements() {
        return equipementRepository.findAll();
    }

    public Equipement getEquipementById(Long id) {
        return equipementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipement not found with id: " + id));
    }

    public Equipement createEquipement(Long sousEspaceId, Equipement equipement) {
        SousEspace sousEspace = sousEspaceService.getSousEspaceById(sousEspaceId);
        equipement.setSousEspace(sousEspace);
        return equipementRepository.save(equipement);
    }

    public Equipement updateEquipement(Long id, Equipement equipementDetails) {
        Equipement equipement = getEquipementById(id);
        equipement.setNom(equipementDetails.getNom());
        equipement.setQuantite(equipementDetails.getQuantite());
        return equipementRepository.save(equipement);
    }

    public void deleteEquipement(Long id) {
        equipementRepository.deleteById(id);
    }

    public List<Equipement> getEquipementsBySousEspaceId(Long sousEspaceId) {
        return equipementRepository.findBySousEspaceId(sousEspaceId);
    }
}
