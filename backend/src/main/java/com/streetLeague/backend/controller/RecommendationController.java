package com.streetLeague.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streetLeague.backend.entity.Product;
import com.streetLeague.backend.service.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/api/recommendations")
    public ResponseEntity<List<Map<String, Object>>> getRecommendations(
            @RequestParam(defaultValue = "0") Long userId,
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(recommendationService.getRecommendations(userId, limit));
    }

    @GetMapping("/api/products/recommend")
    public List<Product> recommend(
            @RequestParam String category,
            @RequestParam double maxPrice
    ) {
        return recommendationService.recommend(category, maxPrice);
    }
}
