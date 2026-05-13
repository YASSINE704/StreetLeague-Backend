# Code hyper commenté — `backend/src/main/java/com/streetLeague/backend/service/ReservationSeanceService.java`

## 1. Rôle du fichier

Service métier : contient les règles métier et la logique principale.

## 2. Avec quoi ce fichier est implémenté

- Technologie principale : **Java / Spring Boot**.
- Utilise Spring Boot, JPA/Hibernate, Jakarta Validation ou Spring Security selon les annotations présentes.

## 3. Comment il communique avec les autres fichiers

- Participe à la chaîne IA : Angular → Spring Boot → Python Flask → retour vers Angular.
- Participe au workflow de réservation et aux contrôles de capacité/doublons/chevauchements.
- Appelé par un controller et utilise souvent un repository pour appliquer les règles métier.

## 4. Explication ligne par ligne

> Objectif : pouvoir expliquer le code même avec zéro prérequis. La colonne 'Explication' dit ce que fait la ligne sans modifier le code source.

| Ligne | Code | Explication débutant |
|---:|---|---|
| 1 | `package com.streetLeague.backend.service;` | Déclare le package Java : cela indique où la classe est rangée dans l’architecture du backend. |
| 2 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 3 | `import com.streetLeague.backend.dto.ReservationSeanceDTO;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 4 | `import com.streetLeague.backend.entity.ReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 5 | `import com.streetLeague.backend.entity.SeanceEntrainement;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 6 | `import com.streetLeague.backend.entity.User;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 7 | `import com.streetLeague.backend.enums.StatutReservationSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 8 | `import com.streetLeague.backend.enums.StatutSeance;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 9 | `import com.streetLeague.backend.exception.BusinessRuleException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 10 | `import com.streetLeague.backend.exception.ResourceNotFoundException;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 11 | `import com.streetLeague.backend.mapper.ReservationSeanceMapper;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 12 | `import com.streetLeague.backend.repository.ReservationSeanceRepository;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 13 | `import lombok.RequiredArgsConstructor;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 14 | `import org.springframework.stereotype.Service;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 15 | `import org.springframework.transaction.annotation.Transactional;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 16 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 17 | `import java.time.LocalDateTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 18 | `import java.time.LocalTime;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 19 | `import java.util.List;` | Importe une classe/bibliothèque nécessaire pour utiliser une fonctionnalité dans ce fichier. |
| 20 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 21 | `/**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 22 | ` * Service de réservation de séances de coaching.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 23 | ` *` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 24 | ` * Règles métier :` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 25 | ` * 1. Seuls les SPORTIF/ADMIN peuvent réserver (le COACH gère, il ne réserve pas)` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 26 | ` * 2. Max 5 participants par séance (configurable via maxParticipants)` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 27 | ` * 3. Pas de doublon : un sportif ne peut pas réserver 2 fois la même séance` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 28 | ` * 4. Pas de chevauchement : un sportif ne peut pas réserver 2 séances au même créneau` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 29 | ` * 5. La séance doit être PREVUE (pas REALISEE, ANNULEE)` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 30 | ` * 6. Le mode de paiement est obligatoire` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 31 | ` */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 32 | `@Service` | Déclare un service Spring : cette classe contient la logique métier. |
| 33 | `@RequiredArgsConstructor` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 34 | `@Transactional` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 35 | `public class ReservationSeanceService {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 36 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 37 | `    private final ReservationSeanceRepository reservationRepository;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 38 | `    private final SeanceEntrainementService seanceService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 39 | `    private final CoachingRoleService roleService;` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 40 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 41 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 42 | `     * Réserver une place dans une séance.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 43 | `     * Le userId vient du header X-User-Id.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 44 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 45 | `    public ReservationSeanceDTO.Response reserver(Integer userId, ReservationSeanceDTO.Request dto) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 46 | `        // Vérifier que l'utilisateur est SPORTIF ou ADMIN` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 47 | `        User user = roleService.requireSportifOrCoachOrAdmin(userId);` | Ligne liée aux rôles et aux permissions utilisateur. |
| 48 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 49 | `        SeanceEntrainement seance = seanceService.findOrThrow(dto.getSeanceId());` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 50 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 51 | `        // Règle 1 : la séance doit être PREVUE` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 52 | `        if (seance.getStatut() != StatutSeance.PREVUE) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 53 | `            throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 54 | `                    "Impossible de réserver : la séance n'est pas disponible (statut actuel : " + seance.getStatut() + ")");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 55 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 56 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 57 | `        // Règle 2 : vérifier la capacité (max participants)` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 58 | `        List<ReservationSeance> reservationsActives = reservationRepository` | Ligne liée à la réservation d’une séance par un sportif. |
| 59 | `                .findBySeanceIdSeanceAndStatutNot(dto.getSeanceId(), StatutReservationSeance.ANNULEE);` | Ligne liée à la réservation d’une séance par un sportif. |
| 60 | `        int maxPlaces = seance.getMaxParticipants() != null ? seance.getMaxParticipants() : 5;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 61 | `        if (reservationsActives.size() >= maxPlaces) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 62 | `            throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 63 | `                    "Séance complète : capacité maximale atteinte (" + maxPlaces + "/" + maxPlaces + " places)");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 64 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 65 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 66 | `        // Règle 3 : pas de doublon (même sportif, même séance)` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 67 | `        reservationRepository.findByUserIdUserAndSeanceIdSeanceAndStatutNot(` | Ligne liée à la réservation d’une séance par un sportif. |
| 68 | `                userId, dto.getSeanceId(), StatutReservationSeance.ANNULEE` | Ligne liée à la réservation d’une séance par un sportif. |
| 69 | `        ).ifPresent(existing -> {` | Début d’un bloc de code : les lignes suivantes appartiennent à cette structure. |
| 70 | `            throw new BusinessRuleException("Vous avez déjà réservé cette séance");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 71 | `        });` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 72 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 73 | `        // Règle 4 : pas de chevauchement horaire avec une autre séance réservée` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 74 | `        if (seance.getHeureDebut() != null && seance.getHeureFin() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 75 | `            validateNoTimeOverlap(userId, seance);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 76 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 77 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 78 | `        // Créer la réservation` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 79 | `        ReservationSeance reservation = ReservationSeance.builder()` | Ligne liée à la réservation d’une séance par un sportif. |
| 80 | `                .user(user)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 81 | `                .seance(seance)` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 82 | `                .dateReservation(LocalDateTime.now())` | Ligne liée à la réservation d’une séance par un sportif. |
| 83 | `                .statut(StatutReservationSeance.RESERVEE)` | Ligne liée à la réservation d’une séance par un sportif. |
| 84 | `                .modePaiement(dto.getModePaiement())` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 85 | `                .build();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 86 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 87 | `        return ReservationSeanceMapper.toResponse(reservationRepository.save(reservation));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 88 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 89 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 90 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 91 | `     * Annuler une réservation (par le sportif ou le coach).` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 92 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 93 | `    public ReservationSeanceDTO.Response annuler(Integer reservationId, String motif) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 94 | `        ReservationSeance reservation = findOrThrow(reservationId);` | Ligne liée à la réservation d’une séance par un sportif. |
| 95 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 96 | `        if (reservation.getStatut() == StatutReservationSeance.ANNULEE) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 97 | `            throw new BusinessRuleException("Cette réservation est déjà annulée");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 98 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 99 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 100 | `        reservation.setStatut(StatutReservationSeance.ANNULEE);` | Ligne liée à la réservation d’une séance par un sportif. |
| 101 | `        reservation.setMotifAnnulation(motif != null ? motif : "Annulée par l'utilisateur");` | Ligne liée à la réservation d’une séance par un sportif. |
| 102 | `        return ReservationSeanceMapper.toResponse(reservationRepository.save(reservation));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 103 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 104 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 105 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 106 | `     * Confirmer une réservation (par le coach).` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 107 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 108 | `    public ReservationSeanceDTO.Response confirmer(Integer reservationId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 109 | `        ReservationSeance reservation = findOrThrow(reservationId);` | Ligne liée à la réservation d’une séance par un sportif. |
| 110 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 111 | `        if (reservation.getStatut() != StatutReservationSeance.RESERVEE) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 112 | `            throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 113 | `                    "Seules les réservations en attente peuvent être confirmées (statut actuel : " + reservation.getStatut() + ")");` | Ligne liée à la réservation d’une séance par un sportif. |
| 114 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 115 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 116 | `        reservation.setStatut(StatutReservationSeance.CONFIRMEE);` | Ligne liée à la réservation d’une séance par un sportif. |
| 117 | `        return ReservationSeanceMapper.toResponse(reservationRepository.save(reservation));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 118 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 119 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 120 | `    /** Réservations d'une séance */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 121 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 122 | `    public List<ReservationSeanceDTO.Response> getBySeance(Integer seanceId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 123 | `        return reservationRepository.findBySeanceIdSeance(seanceId).stream()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 124 | `                .map(ReservationSeanceMapper::toResponse).toList();` | Ligne liée à la réservation d’une séance par un sportif. |
| 125 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 126 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 127 | `    /** Réservations d'un sportif */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 128 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 129 | `    public List<ReservationSeanceDTO.Response> getByUser(Integer userId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 130 | `        return reservationRepository.findByUserIdUser(userId).stream()` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 131 | `                .map(ReservationSeanceMapper::toResponse).toList();` | Ligne liée à la réservation d’une séance par un sportif. |
| 132 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 133 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 134 | `    /** Nombre de places restantes pour une séance */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 135 | `    @Transactional(readOnly = true)` | Déclare une transaction : si une erreur arrive, les changements en base peuvent être annulés. |
| 136 | `    public int getPlacesRestantes(Integer seanceId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 137 | `        SeanceEntrainement seance = seanceService.findOrThrow(seanceId);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 138 | `        int maxPlaces = seance.getMaxParticipants() != null ? seance.getMaxParticipants() : 5;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 139 | `        long reservees = reservationRepository` | Ligne liée à la réservation d’une séance par un sportif. |
| 140 | `                .findBySeanceIdSeanceAndStatutNot(seanceId, StatutReservationSeance.ANNULEE).size();` | Ligne liée à la réservation d’une séance par un sportif. |
| 141 | `        return Math.max(0, maxPlaces - (int) reservees);` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 142 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 143 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 144 | `    /** Step 6 : Marquer une réservation comme payée (par le coach) */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 145 | `    public ReservationSeanceDTO.Response marquerPaye(Integer reservationId) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 146 | `        ReservationSeance reservation = findOrThrow(reservationId);` | Ligne liée à la réservation d’une séance par un sportif. |
| 147 | `        if (reservation.getStatut() == StatutReservationSeance.ANNULEE) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 148 | `            throw new BusinessRuleException("Impossible de marquer comme payée une réservation annulée");` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 149 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 150 | `        reservation.setStatutPaiement(com.streetLeague.backend.enums.StatutPaiement.PAYE);` | Ligne liée à la réservation d’une séance par un sportif. |
| 151 | `        return ReservationSeanceMapper.toResponse(reservationRepository.save(reservation));` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 152 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 153 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 154 | `    private ReservationSeance findOrThrow(Integer id) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 155 | `        return reservationRepository.findById(id)` | Retourne un résultat à l’appelant de la méthode/fonction. |
| 156 | `                .orElseThrow(() -> new ResourceNotFoundException("Réservation non trouvée avec id: " + id));` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 157 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 158 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 159 | `    /**` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 160 | `     * Vérifie qu'un sportif n'a pas déjà une réservation active` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 161 | `     * pour une autre séance qui chevauche le même créneau horaire.` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 162 | `     */` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 163 | `    private void validateNoTimeOverlap(Integer userId, SeanceEntrainement newSeance) {` | Déclare une propriété ou méthode avec un niveau d’accès dans la classe. |
| 164 | `        List<ReservationSeance> mesReservations = reservationRepository` | Ligne liée à la réservation d’une séance par un sportif. |
| 165 | `                .findByUserIdUserAndStatutNot(userId, StatutReservationSeance.ANNULEE);` | Ligne liée à la réservation d’une séance par un sportif. |
| 166 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 167 | `        for (ReservationSeance r : mesReservations) {` | Boucle : répète une logique sur plusieurs éléments. |
| 168 | `            SeanceEntrainement existante = r.getSeance();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 169 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 170 | `            // Même date ?` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 171 | `            if (existante.getDateSeance() == null \|\| !existante.getDateSeance().equals(newSeance.getDateSeance())) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 172 | `                continue;` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 173 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 174 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 175 | `            // Vérifier le chevauchement horaire` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 176 | `            if (existante.getHeureDebut() != null && existante.getHeureFin() != null) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 177 | `                LocalTime newDebut = newSeance.getHeureDebut();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 178 | `                LocalTime newFin = newSeance.getHeureFin();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 179 | `                LocalTime existDebut = existante.getHeureDebut();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 180 | `                LocalTime existFin = existante.getHeureFin();` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 181 | `` | Ligne vide utilisée pour séparer visuellement les blocs de code. |
| 182 | `                // Chevauchement : newDebut < existFin AND newFin > existDebut` | Commentaire existant : il explique une partie du code sans être exécuté. |
| 183 | `                if (newDebut.isBefore(existFin) && newFin.isAfter(existDebut)) {` | Condition : exécute le bloc seulement si la règle est vraie. |
| 184 | `                    throw new BusinessRuleException(` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 185 | `                            "Conflit horaire : vous avez déjà réservé la séance « " + existante.getTitreSeance()` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 186 | `                            + " » le même jour de " + existDebut + " à " + existFin);` | Ligne de code participant au fonctionnement du fichier ; elle conserve la logique existante du projet. |
| 187 | `                }` | Fin d’un bloc de code ou d’un élément HTML. |
| 188 | `            }` | Fin d’un bloc de code ou d’un élément HTML. |
| 189 | `        }` | Fin d’un bloc de code ou d’un élément HTML. |
| 190 | `    }` | Fin d’un bloc de code ou d’un élément HTML. |
| 191 | `}` | Fin d’un bloc de code ou d’un élément HTML. |

## 5. Réponse orale courte si le prof demande ce fichier

Tu peux répondre :

> Ce fichier `ReservationSeanceService.java` fait partie du backend Spring Boot. Il est séparé selon l’architecture du projet pour garder le code propre : controller pour les endpoints, service pour le métier, repository pour la base, entity pour la table ou DTO pour les échanges.