# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/SeanceEntrainementService.java`

## 1. Rôle du fichier

Service métier : contient les règles métier et la logique principale.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Appelé par un controller et utilise souvent un repository pour appliquer les règles métier.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.service;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.dto.SeanceEntrainementDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.entity.AffectationProgramme;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.entity.ProgrammeEntrainement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.entity.SeanceEntrainement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import com.streetLeague.backend.entity.SousEspace;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import com.streetLeague.backend.enums.StatutProgramme;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import com.streetLeague.backend.enums.StatutSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import com.streetLeague.backend.enums.TypeAffectationProgramme;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import com.streetLeague.backend.exception.BusinessRuleException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `import com.streetLeague.backend.exception.ResourceNotFoundException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `import com.streetLeague.backend.mapper.SeanceMapper;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 14 | `import com.streetLeague.backend.repository.AffectationProgrammeRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 15 | `import com.streetLeague.backend.repository.SeanceEntrainementRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 16 | `import com.streetLeague.backend.repository.SousEspaceRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 17 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 18 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 19 | `import org.springframework.transaction.annotation.Transactional;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 20 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 21 | `import java.time.LocalDate;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 22 | `import java.time.LocalTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 23 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 26 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `@Transactional` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 28 | `public class SeanceEntrainementService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 29 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 30 | `    private final SeanceEntrainementRepository seanceRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 31 | `    private final ProgrammeEntrainementService programmeService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 32 | `    private final AffectationProgrammeRepository affectationRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 33 | `    private final SousEspaceRepository sousEspaceRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 34 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 35 | `    public SeanceEntrainementDTO.Response create(SeanceEntrainementDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 36 | `        ProgrammeEntrainement programme = programmeService.findOrThrow(dto.getProgrammeId());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 37 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 38 | `        // Règle métier : impossible de créer une séance si programme TERMINE` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 39 | `        if (programme.getStatut() == StatutProgramme.TERMINE) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 40 | `            throw new BusinessRuleException("Impossible de créer une séance pour un programme terminé");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 42 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 43 | `        // Règle métier : la date de la séance doit être dans l'intervalle du programme` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 44 | `        validateDateInProgrammeRange(dto.getDateSeance(), programme);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 45 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 46 | `        /* Step 8 : valider que heureDebut < heureFin */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 47 | `        validateTimeRange(dto.getHeureDebut(), dto.getHeureFin());        /* Step 3 : vérifier les conflits d'emploi du temps du coach */` | Ligne liée aux rôles et aux permissions utilisateur. |
| 48 | `        validateCoachScheduleConflict(programme, dto.getDateSeance(), dto.getHeureDebut(), dto.getHeureFin(), null);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 49 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 50 | `        SeanceEntrainement seance = SeanceMapper.toEntity(dto);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `        seance.setProgramme(programme);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 52 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 53 | `        /* Step 4 : lier le lieu (SousEspace) si fourni */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 54 | `        if (dto.getSousEspaceId() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 55 | `            SousEspace lieu = sousEspaceRepository.findById(dto.getSousEspaceId())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 56 | `                    .orElseThrow(() -> new ResourceNotFoundException("Lieu non trouvé avec id: " + dto.getSousEspaceId()));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 57 | `            seance.setLieu(lieu);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 58 | `            /* Step 4 : vérifier que le lieu n'est pas déjà réservé au même créneau */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 59 | `            validateLocationConflict(dto.getSousEspaceId(), dto.getDateSeance(), dto.getHeureDebut(), dto.getHeureFin(), null);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 60 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 61 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 62 | `        return SeanceMapper.toResponse(seanceRepository.save(seance));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 63 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 64 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 65 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 66 | `    public List<SeanceEntrainementDTO.Response> getAll() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 67 | `        return seanceRepository.findAll().stream().map(SeanceMapper::toResponse).toList();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 68 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 69 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 70 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 71 | `    public SeanceEntrainementDTO.Response getById(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 72 | `        return SeanceMapper.toResponse(findOrThrow(id));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 73 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 74 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 75 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 76 | `    public List<SeanceEntrainementDTO.Response> getByProgramme(Integer programmeId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 77 | `        return seanceRepository.findByProgrammeIdProgramme(programmeId).stream()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 78 | `                .map(SeanceMapper::toResponse).toList();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 79 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 80 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 81 | `    public SeanceEntrainementDTO.Response update(Integer id, SeanceEntrainementDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 82 | `        SeanceEntrainement seance = findOrThrow(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 83 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 84 | `        // Règle métier : impossible de modifier une séance REALISEE` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 85 | `        if (seance.getStatut() == StatutSeance.REALISEE) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 86 | `            throw new BusinessRuleException("Impossible de modifier une séance déjà réalisée");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 87 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 88 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 89 | `        seance.setTitreSeance(dto.getTitreSeance());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 90 | `        seance.setDateSeance(dto.getDateSeance());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 91 | `        seance.setDureeMinutes(dto.getDureeMinutes());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 92 | `        seance.setIntensite(dto.getIntensite());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 93 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 94 | `        /* Step 2 : mise à jour des champs horaire et capacité */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 95 | `        if (dto.getHeureDebut() != null) seance.setHeureDebut(dto.getHeureDebut());` | Condition : exécute le bloc seulement si la règle est vraie. |
| 96 | `        if (dto.getHeureFin() != null) seance.setHeureFin(dto.getHeureFin());` | Condition : exécute le bloc seulement si la règle est vraie. |
| 97 | `        if (dto.getMaxParticipants() != null) seance.setMaxParticipants(dto.getMaxParticipants());` | Condition : exécute le bloc seulement si la règle est vraie. |
| 98 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 99 | `        /* Step 4 : mise à jour du lieu */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 100 | `        if (dto.getSousEspaceId() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 101 | `            SousEspace lieu = sousEspaceRepository.findById(dto.getSousEspaceId())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 102 | `                    .orElseThrow(() -> new ResourceNotFoundException("Lieu non trouvé avec id: " + dto.getSousEspaceId()));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 103 | `            seance.setLieu(lieu);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 104 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 105 | `        if (dto.getEnPleinAir() != null) seance.setEnPleinAir(dto.getEnPleinAir());` | Condition : exécute le bloc seulement si la règle est vraie. |
| 106 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 107 | `        // Règle métier : la date de la séance doit être dans l'intervalle du programme` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 108 | `        ProgrammeEntrainement currentProgramme = seance.getProgramme();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 109 | `        if (dto.getProgrammeId() != null && !dto.getProgrammeId().equals(currentProgramme.getIdProgramme())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 110 | `            currentProgramme = programmeService.findOrThrow(dto.getProgrammeId());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 111 | `            seance.setProgramme(currentProgramme);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 112 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 113 | `        validateDateInProgrammeRange(dto.getDateSeance(), currentProgramme);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 114 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 115 | `        /* Step 3 : vérifier les conflits d'emploi du temps du coach */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 116 | `        LocalTime newDebut = dto.getHeureDebut() != null ? dto.getHeureDebut() : seance.getHeureDebut();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 117 | `        LocalTime newFin = dto.getHeureFin() != null ? dto.getHeureFin() : seance.getHeureFin();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 118 | `        validateCoachScheduleConflict(currentProgramme, dto.getDateSeance(), newDebut, newFin, seance.getIdSeance());` | Ligne liée aux rôles et aux permissions utilisateur. |
| 119 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 120 | `        /* Step 4 : vérifier conflit de lieu */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 121 | `        Long lieuId = dto.getSousEspaceId() != null ? dto.getSousEspaceId()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 122 | `                : (seance.getLieu() != null ? seance.getLieu().getId() : null);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 123 | `        if (lieuId != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 124 | `            validateLocationConflict(lieuId, dto.getDateSeance(), newDebut, newFin, seance.getIdSeance());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 125 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 126 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 127 | `        if (dto.getStatut() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 128 | `            // Règle métier : séance ne peut passer à REALISEE que si elle a au moins 1 exercice` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 129 | `            if (dto.getStatut() == StatutSeance.REALISEE` | Condition : exécute le bloc seulement si la règle est vraie. |
| 130 | `                    && (seance.getSeanceExercices() == null \|\| seance.getSeanceExercices().isEmpty())) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 131 | `                throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 132 | `                        "Impossible de marquer la séance comme réalisée : aucun exercice associé");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 133 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 134 | `            seance.setStatut(dto.getStatut());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 135 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 136 | `        return SeanceMapper.toResponse(seanceRepository.save(seance));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 137 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 138 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 139 | `    public void delete(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 140 | `        findOrThrow(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 141 | `        seanceRepository.deleteById(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 142 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 143 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 144 | `    public SeanceEntrainement findOrThrow(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 145 | `        return seanceRepository.findById(id)` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 146 | `                .orElseThrow(() -> new ResourceNotFoundException("Séance non trouvée avec id: " + id));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 147 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 148 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 149 | `    private void validateDateInProgrammeRange(LocalDate dateSeance, ProgrammeEntrainement programme) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 150 | `        if (dateSeance == null \|\| programme.getDateDebut() == null \|\| programme.getDateFin() == null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 151 | `            return;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 152 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 153 | `        if (dateSeance.isBefore(programme.getDateDebut()) \|\| dateSeance.isAfter(programme.getDateFin())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 154 | `            throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 155 | `                    "La date de la séance doit être comprise entre le "` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 156 | `                    + programme.getDateDebut() + " et le " + programme.getDateFin()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 157 | `                    + " (dates du programme « " + programme.getTitre() + " »)");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 158 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 159 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 160 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 161 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 162 | `     * Step 3 : Vérifie qu'un coach n'a pas déjà une séance au même créneau horaire.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 163 | `     * On cherche le coach affecté au programme, puis on vérifie toutes ses séances` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 164 | `     * dans tous ses programmes pour détecter un chevauchement.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 165 | `     *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 166 | `     * @param programme     le programme de la séance` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 167 | `     * @param dateSeance    la date de la séance` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 168 | `     * @param heureDebut    l'heure de début` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 169 | `     * @param heureFin      l'heure de fin` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 170 | `     * @param excludeSeanceId  ID de la séance à exclure (pour l'update, on exclut la séance elle-même)` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 171 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 172 | `    private void validateCoachScheduleConflict(ProgrammeEntrainement programme,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 173 | `                                                LocalDate dateSeance,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 174 | `                                                LocalTime heureDebut,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 175 | `                                                LocalTime heureFin,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 176 | `                                                Integer excludeSeanceId) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 177 | `        // Si pas d'heure définie, on ne peut pas vérifier le chevauchement` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 178 | `        if (heureDebut == null \|\| heureFin == null \|\| dateSeance == null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 179 | `            return;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 180 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 181 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 182 | `        // Trouver le coach affecté à ce programme` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 183 | `        var coachAffectation = affectationRepository` | Ligne liée aux rôles et aux permissions utilisateur. |
| 184 | `                .findByProgrammeIdProgrammeAndType(programme.getIdProgramme(), TypeAffectationProgramme.COACH);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 185 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 186 | `        if (coachAffectation.isEmpty()) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 187 | `            return; // Pas de coach affecté, pas de conflit possible` | Ligne liée aux rôles et aux permissions utilisateur. |
| 188 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 189 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 190 | `        Integer coachId = coachAffectation.get().getUser().getIdUser();` | Ligne liée aux rôles et aux permissions utilisateur. |
| 191 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 192 | `        // Trouver tous les programmes où ce coach est affecté` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 193 | `        List<AffectationProgramme> coachProgrammes = affectationRepository` | Ligne liée aux rôles et aux permissions utilisateur. |
| 194 | `                .findByUserIdUserAndType(coachId, TypeAffectationProgramme.COACH);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 195 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 196 | `        // Pour chaque programme du coach, vérifier les séances à la même date` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 197 | `        for (AffectationProgramme aff : coachProgrammes) {` | Boucle : répète une logique sur plusieurs éléments. |
| 198 | `            List<SeanceEntrainement> seancesMemeJour = seanceRepository` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 199 | `                    .findByProgrammeIdProgrammeAndDateSeance(aff.getProgramme().getIdProgramme(), dateSeance);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 200 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 201 | `            for (SeanceEntrainement existante : seancesMemeJour) {` | Boucle : répète une logique sur plusieurs éléments. |
| 202 | `                // Exclure la séance en cours de modification` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 203 | `                if (excludeSeanceId != null && excludeSeanceId.equals(existante.getIdSeance())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 204 | `                    continue;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 205 | `                }` | Fin d’un bloc de code ou d’un élément HTML. |
| 206 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 207 | `                // Vérifier le chevauchement horaire` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 208 | `                if (existante.getHeureDebut() != null && existante.getHeureFin() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 209 | `                    if (heureDebut.isBefore(existante.getHeureFin()) && heureFin.isAfter(existante.getHeureDebut())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 210 | `                        throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 211 | `                                "Conflit d'emploi du temps du coach : la séance « " + existante.getTitreSeance()` | Ligne liée aux rôles et aux permissions utilisateur. |
| 212 | `                                + " » est déjà planifiée le " + dateSeance` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 213 | `                                + " de " + existante.getHeureDebut() + " à " + existante.getHeureFin()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 214 | `                                + " (programme « " + existante.getProgramme().getTitre() + " »)");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 215 | `                    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 216 | `                }` | Fin d’un bloc de code ou d’un élément HTML. |
| 217 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 218 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 219 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 220 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 221 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 222 | `     * Step 4 : Vérifie qu'un lieu (SousEspace) n'est pas déjà utilisé par une autre séance` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 223 | `     * au même créneau horaire le même jour.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 224 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 225 | `    private void validateLocationConflict(Long sousEspaceId, LocalDate dateSeance,` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 226 | `                                           LocalTime heureDebut, LocalTime heureFin,` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 227 | `                                           Integer excludeSeanceId) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 228 | `        if (sousEspaceId == null \|\| dateSeance == null \|\| heureDebut == null \|\| heureFin == null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 229 | `            return;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 230 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 231 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 232 | `        // Chercher toutes les séances à cette date` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 233 | `        List<SeanceEntrainement> seancesMemeJour = seanceRepository.findByDateSeance(dateSeance);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 234 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 235 | `        for (SeanceEntrainement existante : seancesMemeJour) {` | Boucle : répète une logique sur plusieurs éléments. |
| 236 | `            if (excludeSeanceId != null && excludeSeanceId.equals(existante.getIdSeance())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 237 | `                continue;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 238 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 239 | `            // Même lieu ?` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 240 | `            if (existante.getLieu() == null \|\| !existante.getLieu().getId().equals(sousEspaceId)) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 241 | `                continue;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 242 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 243 | `            // Chevauchement horaire ?` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 244 | `            if (existante.getHeureDebut() != null && existante.getHeureFin() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 245 | `                if (heureDebut.isBefore(existante.getHeureFin()) && heureFin.isAfter(existante.getHeureDebut())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 246 | `                    throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 247 | `                            "Conflit de lieu : le lieu « " + existante.getLieu().getNom()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 248 | `                            + " » est déjà réservé le " + dateSeance` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 249 | `                            + " de " + existante.getHeureDebut() + " à " + existante.getHeureFin()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 250 | `                            + " pour la séance « " + existante.getTitreSeance() + " »");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 251 | `                }` | Fin d’un bloc de code ou d’un élément HTML. |
| 252 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 253 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 254 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 255 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 256 | `    /** Step 8 : Valide que heureDebut est avant heureFin */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 257 | `    private void validateTimeRange(LocalTime heureDebut, LocalTime heureFin) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 258 | `        if (heureDebut != null && heureFin != null && !heureFin.isAfter(heureDebut)) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 259 | `            throw new BusinessRuleException("L'heure de fin doit être après l'heure de début");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 260 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 261 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 262 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `SeanceEntrainementService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.