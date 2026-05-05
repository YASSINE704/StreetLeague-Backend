# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/ExerciceService.java`

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
| 3 | `import com.streetLeague.backend.dto.ExerciceDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.entity.Exercice;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.enums.TypeExercice;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.exception.ResourceNotFoundException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import com.streetLeague.backend.mapper.ExerciceMapper;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import com.streetLeague.backend.repository.ExerciceRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 12 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 14 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 15 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 16 | `public class ExerciceService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 17 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 18 | `    private final ExerciceRepository exerciceRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 19 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 20 | `    public ExerciceDTO.Response create(ExerciceDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 21 | `        Exercice exercice = ExerciceMapper.toEntity(dto);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 22 | `        return ExerciceMapper.toResponse(exerciceRepository.save(exercice));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 23 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 24 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 25 | `    public List<ExerciceDTO.Response> getAll() {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 26 | `        return exerciceRepository.findAll().stream().map(ExerciceMapper::toResponse).toList();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 27 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 28 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 29 | `    public ExerciceDTO.Response getById(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 30 | `        return ExerciceMapper.toResponse(findOrThrow(id));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 31 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 32 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 33 | `    public List<ExerciceDTO.Response> getByType(TypeExercice type) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 34 | `        return exerciceRepository.findByType(type).stream().map(ExerciceMapper::toResponse).toList();` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 35 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 36 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 37 | `    public ExerciceDTO.Response update(Integer id, ExerciceDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 38 | `        Exercice exercice = findOrThrow(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 39 | `        exercice.setNom(dto.getNom());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 40 | `        exercice.setDescription(dto.getDescription());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 41 | `        exercice.setType(dto.getType());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 42 | `        exercice.setVideoUrl(dto.getVideoUrl());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 43 | `        return ExerciceMapper.toResponse(exerciceRepository.save(exercice));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 44 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 45 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 46 | `    public void delete(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 47 | `        findOrThrow(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 48 | `        exerciceRepository.deleteById(id);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 49 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 50 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 51 | `    private Exercice findOrThrow(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 52 | `        return exerciceRepository.findById(id)` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 53 | `                .orElseThrow(() -> new ResourceNotFoundException("Exercice non trouvé avec id: " + id));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 55 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ExerciceService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.