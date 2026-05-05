# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/ProgrammeEntrainementService.java`

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
| 3 | `import com.streetLeague.backend.dto.ProgrammeEntrainementDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.entity.ProgrammeEntrainement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.enums.StatutProgramme;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.enums.StatutSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import com.streetLeague.backend.exception.BusinessRuleException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import com.streetLeague.backend.exception.ResourceNotFoundException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import com.streetLeague.backend.mapper.ProgrammeMapper;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import com.streetLeague.backend.repository.ProgrammeEntrainementRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `import org.springframework.transaction.annotation.Transactional;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 14 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 15 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 18 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 19 | `@Transactional` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 20 | `public class ProgrammeEntrainementService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 21 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 22 | `    private final ProgrammeEntrainementRepository programmeRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 23 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 24 | `    public ProgrammeEntrainementDTO.Response create(ProgrammeEntrainementDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 25 | `        validateDates(dto);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 26 | `        ProgrammeEntrainement programme = ProgrammeMapper.toEntity(dto);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 27 | `        return ProgrammeMapper.toResponse(programmeRepository.save(programme));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 28 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 29 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 30 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 31 | `    public List<ProgrammeEntrainementDTO.Response> getAll() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 32 | `        return programmeRepository.findAll().stream().map(ProgrammeMapper::toResponse).toList();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 33 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 34 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 35 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 36 | `    public ProgrammeEntrainementDTO.Response getById(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 37 | `        return ProgrammeMapper.toResponse(findOrThrow(id));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 38 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 39 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 40 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 41 | `    public List<ProgrammeEntrainementDTO.Response> getByStatut(StatutProgramme statut) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 42 | `        return programmeRepository.findByStatut(statut).stream().map(ProgrammeMapper::toResponse).toList();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 43 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 44 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 45 | `    public ProgrammeEntrainementDTO.Response update(Integer id, ProgrammeEntrainementDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 46 | `        ProgrammeEntrainement programme = findOrThrow(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 47 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 48 | `        // Règle métier : impossible de modifier un programme TERMINE` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 49 | `        if (programme.getStatut() == StatutProgramme.TERMINE) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 50 | `            throw new BusinessRuleException("Impossible de modifier un programme terminé");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 51 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 52 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 53 | `        validateDates(dto);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `        programme.setTitre(dto.getTitre());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `        programme.setDescription(dto.getDescription());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 56 | `        programme.setDateDebut(dto.getDateDebut());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 57 | `        programme.setDateFin(dto.getDateFin());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 58 | `        if (dto.getStatut() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 59 | `            // Règle métier : programme ne peut passer à TERMINE que si toutes les séances sont REALISEE ou ANNULEE` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 60 | `            if (dto.getStatut() == StatutProgramme.TERMINE) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 61 | `                boolean hasSeancesPrevues = programme.getSeances().stream()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 62 | `                        .anyMatch(s -> s.getStatut() == StatutSeance.PREVUE);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 63 | `                if (hasSeancesPrevues) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 64 | `                    throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 65 | `                            "Impossible de terminer le programme : certaines séances sont encore PREVUE");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 66 | `                }` | Fin d’un bloc de code ou d’un élément HTML. |
| 67 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 68 | `            programme.setStatut(dto.getStatut());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 69 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 70 | `        return ProgrammeMapper.toResponse(programmeRepository.save(programme));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 71 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 72 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 73 | `    public void delete(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 74 | `        findOrThrow(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 75 | `        programmeRepository.deleteById(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 76 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 77 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 78 | `    public ProgrammeEntrainement findOrThrow(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 79 | `        return programmeRepository.findById(id)` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 80 | `                .orElseThrow(() -> new ResourceNotFoundException("Programme non trouvé avec id: " + id));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 81 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 82 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 83 | `    private void validateDates(ProgrammeEntrainementDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 84 | `        if (dto.getDateDebut() != null && dto.getDateFin() != null` | Condition : exécute le bloc seulement si la règle est vraie. |
| 85 | `                && dto.getDateFin().isBefore(dto.getDateDebut())) {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 86 | `            throw new BusinessRuleException("La date de fin doit être après la date de début");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 87 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 88 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 89 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ProgrammeEntrainementService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.