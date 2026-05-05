# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/SeanceExerciceService.java`

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
| 3 | `import com.streetLeague.backend.dto.SeanceExerciceDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.entity.Exercice;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.entity.SeanceEntrainement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.entity.SeanceExercice;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import com.streetLeague.backend.exception.BusinessRuleException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import com.streetLeague.backend.exception.ResourceNotFoundException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import com.streetLeague.backend.mapper.ExerciceMapper;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import com.streetLeague.backend.repository.ExerciceRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import com.streetLeague.backend.repository.SeanceExerciceRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 14 | `import org.springframework.transaction.annotation.Transactional;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 15 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 16 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 17 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 18 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 19 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 20 | `@Transactional` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 21 | `public class SeanceExerciceService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 22 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 23 | `    private final SeanceExerciceRepository seanceExerciceRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 24 | `    private final SeanceEntrainementService seanceService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 25 | `    private final ExerciceRepository exerciceRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 26 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 27 | `    public SeanceExerciceDTO.Response create(SeanceExerciceDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 28 | `        SeanceEntrainement seance = seanceService.findOrThrow(dto.getSeanceId());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 29 | `        Exercice exercice = exerciceRepository.findById(dto.getExerciceId())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 30 | `                .orElseThrow(() -> new ResourceNotFoundException("Exercice non trouvé avec id: " + dto.getExerciceId()));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 31 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 32 | `        validateVolume(dto);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 33 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 34 | `        SeanceExercice se = SeanceExercice.builder()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 35 | `                .seance(seance)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 36 | `                .exercice(exercice)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 37 | `                .series(dto.getSeries())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 38 | `                .repetitions(dto.getRepetitions())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `                .charge(dto.getCharge())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `                .tempsSecondes(dto.getTempsSecondes())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `                .ordre(dto.getOrdre())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 42 | `                .build();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `        return ExerciceMapper.toSeanceExerciceResponse(seanceExerciceRepository.save(se));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 44 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 45 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 46 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 47 | `    public List<SeanceExerciceDTO.Response> getAll() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 48 | `        return seanceExerciceRepository.findAll().stream()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 49 | `                .map(ExerciceMapper::toSeanceExerciceResponse).toList();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 50 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 51 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 52 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 53 | `    public SeanceExerciceDTO.Response getById(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 54 | `        return ExerciceMapper.toSeanceExerciceResponse(findOrThrow(id));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 55 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 56 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 57 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 58 | `    public List<SeanceExerciceDTO.Response> getBySeance(Integer seanceId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 59 | `        return seanceExerciceRepository.findBySeanceIdSeanceOrderByOrdreAsc(seanceId).stream()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 60 | `                .map(ExerciceMapper::toSeanceExerciceResponse).toList();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 61 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 62 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 63 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 64 | `    public List<SeanceExerciceDTO.Response> getByExercice(Integer exerciceId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 65 | `        return seanceExerciceRepository.findByExerciceIdExercice(exerciceId).stream()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 66 | `                .map(ExerciceMapper::toSeanceExerciceResponse).toList();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 67 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 68 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 69 | `    public SeanceExerciceDTO.Response update(Integer id, SeanceExerciceDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 70 | `        SeanceExercice se = findOrThrow(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `        validateVolume(dto);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 73 | `        if (dto.getSeanceId() != null && !dto.getSeanceId().equals(se.getSeance().getIdSeance())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 74 | `            se.setSeance(seanceService.findOrThrow(dto.getSeanceId()));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 75 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 76 | `        if (dto.getExerciceId() != null && !dto.getExerciceId().equals(se.getExercice().getIdExercice())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 77 | `            Exercice exercice = exerciceRepository.findById(dto.getExerciceId())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 78 | `                    .orElseThrow(() -> new ResourceNotFoundException("Exercice non trouvé avec id: " + dto.getExerciceId()));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 79 | `            se.setExercice(exercice);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 80 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 81 | `        se.setSeries(dto.getSeries());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 82 | `        se.setRepetitions(dto.getRepetitions());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 83 | `        se.setCharge(dto.getCharge());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 84 | `        se.setTempsSecondes(dto.getTempsSecondes());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 85 | `        se.setOrdre(dto.getOrdre());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 86 | `        return ExerciceMapper.toSeanceExerciceResponse(seanceExerciceRepository.save(se));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 87 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 88 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 89 | `    public void delete(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 90 | `        seanceExerciceRepository.delete(findOrThrow(id));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 91 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 92 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 93 | `    private SeanceExercice findOrThrow(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 94 | `        return seanceExerciceRepository.findById(id)` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 95 | `                .orElseThrow(() -> new ResourceNotFoundException("SeanceExercice non trouvé avec id: " + id));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 96 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 97 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 98 | `    private void validateVolume(SeanceExerciceDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 99 | `        boolean hasRepetitions = dto.getRepetitions() != null && dto.getRepetitions() > 0;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 100 | `        boolean hasDuration = dto.getTempsSecondes() != null && dto.getTempsSecondes() > 0;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 101 | `        if (!hasRepetitions && !hasDuration) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 102 | `            throw new BusinessRuleException("Un exercice de séance doit avoir des répétitions ou un temps d'exécution");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 103 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 104 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 105 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `SeanceExerciceService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.