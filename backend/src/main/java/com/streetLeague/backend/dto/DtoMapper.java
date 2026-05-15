package com.streetLeague.backend.dto;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.entity.Equipement;
import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.entity.SousEspace;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public EndroitDTO toEndroitDTO(Endroit e) {
        return new EndroitDTO(e.getId(), e.getNom(), e.getType(), e.getAdresse(),
                e.getVille(), e.getLatitude(), e.getLongitude(), e.getCapacite(),
                e.getStatut(), e.getDescription(), e.getImageUrl());
    }

    public Endroit toEndroit(EndroitDTO dto) {
        Endroit e = new Endroit();
        e.setId(dto.getId());
        e.setNom(dto.getNom());
        e.setType(dto.getType());
        e.setAdresse(dto.getAdresse());
        e.setVille(dto.getVille());
        e.setLatitude(dto.getLatitude());
        e.setLongitude(dto.getLongitude());
        e.setCapacite(dto.getCapacite());
        e.setStatut(dto.getStatut());
        e.setDescription(dto.getDescription());
        e.setImageUrl(dto.getImageUrl());
        return e;
    }

    public SousEspaceDTO toSousEspaceDTO(SousEspace se) {
        return new SousEspaceDTO(se.getId(), se.getNom(), se.getType(), se.getCapacite(),
                se.getStatut(),
                se.getEndroit() != null ? se.getEndroit().getId() : null,
                se.getEndroit() != null ? se.getEndroit().getNom() : null);
    }

    public SousEspace toSousEspace(SousEspaceDTO dto) {
        SousEspace se = new SousEspace();
        se.setId(dto.getId());
        se.setNom(dto.getNom());
        se.setType(dto.getType());
        se.setCapacite(dto.getCapacite());
        se.setStatut(dto.getStatut());
        return se;
    }

    public EquipementDTO toEquipementDTO(Equipement eq) {
        return new EquipementDTO(eq.getId(), eq.getNom(), eq.getQuantite(),
                eq.getSousEspace() != null ? eq.getSousEspace().getId() : null,
                eq.getSousEspace() != null ? eq.getSousEspace().getNom() : null);
    }

    public Equipement toEquipement(EquipementDTO dto) {
        Equipement eq = new Equipement();
        eq.setId(dto.getId());
        eq.setNom(dto.getNom());
        eq.setQuantite(dto.getQuantite());
        return eq;
    }

    public ReservationDTO toReservationDTO(Reservation r) {
        ReservationDTO dto = new ReservationDTO(r.getId(), r.getDateDebut(), r.getDateFin(),
                r.getStatut(), r.getDateCreation(), r.getMotifAnnulation(),
                r.getSousEspace() != null ? r.getSousEspace().getId() : null,
                r.getSousEspace() != null ? r.getSousEspace().getNom() : null,
                r.getSousEspace() != null && r.getSousEspace().getEndroit() != null ? r.getSousEspace().getEndroit().getId() : null,
                r.getSousEspace() != null && r.getSousEspace().getEndroit() != null ? r.getSousEspace().getEndroit().getNom() : null,
                r.getPrixTotal());
        return dto;
    }

    public Reservation toReservation(ReservationDTO dto) {
        Reservation r = new Reservation();
        r.setId(dto.getId());
        r.setDateDebut(dto.getDateDebut());
        r.setDateFin(dto.getDateFin());
        r.setStatut(dto.getStatut());
        r.setMotifAnnulation(dto.getMotifAnnulation());
        return r;
    }
}
