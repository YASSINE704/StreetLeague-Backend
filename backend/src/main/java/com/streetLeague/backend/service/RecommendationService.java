package com.streetLeague.backend.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.streetLeague.backend.entity.Endroit;
import com.streetLeague.backend.entity.Product;
import com.streetLeague.backend.entity.Reservation;
import com.streetLeague.backend.enums.StatutEndroit;
import com.streetLeague.backend.enums.TypeEndroit;
import com.streetLeague.backend.repository.EndroitRepository;
import com.streetLeague.backend.repository.ProductRepository;
import com.streetLeague.backend.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final EndroitRepository endroitRepository;
    private final ReservationRepository reservationRepository;
    private final ProductRepository productRepository;

    public List<Map<String, Object>> getRecommendations(Long userId, int limit) {
        List<Reservation> allReservations = reservationRepository.findAll();
        List<Endroit> allEndroits = endroitRepository.findAll();

        if (allEndroits.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Long, Long> popularityMap = allReservations.stream()
                .filter(r -> r.getSousEspace() != null && r.getSousEspace().getEndroit() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getSousEspace().getEndroit().getId(),
                        Collectors.counting()
                ));
        long maxPop = popularityMap.values().stream().max(Long::compareTo).orElse(1L);

        Map<TypeEndroit, Long> typePopularity = allReservations.stream()
                .filter(r -> r.getSousEspace() != null && r.getSousEspace().getEndroit() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getSousEspace().getEndroit().getType(),
                        Collectors.counting()
                ));
        long maxTypePop = typePopularity.values().stream().max(Long::compareTo).orElse(1L);

        int maxCapacity = allEndroits.stream()
                .mapToInt(e -> e.getCapacite() != null ? e.getCapacite() : 0)
                .max().orElse(1);

        List<Map<String, Object>> scored = new ArrayList<>();
        for (Endroit endroit : allEndroits) {
            double popScore = popularityMap.getOrDefault(endroit.getId(), 0L) / (double) maxPop;
            double availScore = endroit.getStatut() == StatutEndroit.DISPONIBLE ? 1.0 : 0.0;
            double capScore = (endroit.getCapacite() != null ? endroit.getCapacite() : 0) / (double) maxCapacity;

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

    public List<Product> recommend(String category, double maxPrice) {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getPrice() <= maxPrice)
                .toList();
    }
}
