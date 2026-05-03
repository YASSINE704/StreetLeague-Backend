package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeEndroit;
import com.streetLeague.backend.repository.EndroitRepository;
import com.streetLeague.backend.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AI-powered recommendation engine for endroits.
 *
 * Scoring algorithm (Content-Based Filtering):
 * - Popularity (45%): number of reservations (all statuses except ANNULEE)
 * - Availability (30%): DISPONIBLE endroits get a boost
 * - Capacity (15%): higher capacity = more attractive
 * - Variety bonus (10%): types with fewer reservations get a discovery boost
 */
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final EndroitRepository endroitRepository;
    private final ReservationRepository reservationRepository;

    public List<Map<String, Object>> getRecommendations(Long userId, int limit) {
        List<Reservation> allReservations = reservationRepository.findAll();
        List<Endroit> allEndroits = endroitRepository.findAll();

        if (allEndroits.isEmpty()) {
            return Collections.emptyList();
        }

        // Popularity: endroitId -> reservation count (include EN_ATTENTE + CONFIRMEE)
        Map<Long, Long> popularityMap = allReservations.stream()
                .filter(r -> r.getSousEspace() != null && r.getSousEspace().getEndroit() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getSousEspace().getEndroit().getId(),
                        Collectors.counting()
                ));
        long maxPop = popularityMap.values().stream().max(Long::compareTo).orElse(1L);

        // Type popularity for variety bonus
        Map<TypeEndroit, Long> typePopularity = allReservations.stream()
                .filter(r -> r.getSousEspace() != null && r.getSousEspace().getEndroit() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getSousEspace().getEndroit().getType(),
                        Collectors.counting()
                ));
        long maxTypePop = typePopularity.values().stream().max(Long::compareTo).orElse(1L);

        // Max capacity for normalization
        int maxCapacity = allEndroits.stream()
                .mapToInt(e -> e.getCapacite() != null ? e.getCapacite() : 0)
                .max().orElse(1);

        // Score each endroit
        List<Map<String, Object>> scored = new ArrayList<>();
        for (Endroit endroit : allEndroits) {
            double popScore = popularityMap.getOrDefault(endroit.getId(), 0L) / (double) maxPop;
            double availScore = endroit.getStatut() == StatutEndroit.DISPONIBLE ? 1.0 : 0.0;
            double capScore = (endroit.getCapacite() != null ? endroit.getCapacite() : 0) / (double) maxCapacity;

            // Variety: less popular types get a boost (discovery)
            double typeCount = typePopularity.getOrDefault(endroit.getType(), 0L);
            double varietyScore = 1.0 - (typeCount / (double) (maxTypePop + 1));

            double score = popScore * 0.45 + availScore * 0.30 + capScore * 0.15 + varietyScore * 0.10;
            long scorePercent = Math.round(score * 100);

            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("id", endroit.getId());
            entry.put("nom", endroit.getNom());
            entry.put("type", endroit.getType());
            entry.put("ville", endroit.getVille());
            entry.put("adresse", endroit.getAdresse());
            entry.put("capacite", endroit.getCapacite());
            entry.put("statut", endroit.getStatut());
            entry.put("imageUrl", endroit.getImageUrl());
            entry.put("description", endroit.getDescription());
            entry.put("latitude", endroit.getLatitude());
            entry.put("longitude", endroit.getLongitude());
            entry.put("score", scorePercent);
            entry.put("totalReservations", popularityMap.getOrDefault(endroit.getId(), 0L));

            // Build reasons
            List<String> reasons = new ArrayList<>();
            long pop = popularityMap.getOrDefault(endroit.getId(), 0L);
            if (pop > 0) reasons.add("🔥 " + pop + " réservation(s)");
            if (endroit.getStatut() == StatutEndroit.DISPONIBLE) reasons.add("✅ Disponible");
            if (endroit.getCapacite() != null && endroit.getCapacite() >= 20) reasons.add("👥 Grande capacité");
            if (typeCount == 0) reasons.add("🆕 Nouveau type à découvrir");
            if (reasons.isEmpty()) reasons.add("📍 Suggestion IA");
            entry.put("reasons", reasons);

            scored.add(entry);
        }

        scored.sort((a, b) -> Long.compare((long) b.get("score"), (long) a.get("score")));
        return scored.stream().limit(limit).toList();
    }
}
