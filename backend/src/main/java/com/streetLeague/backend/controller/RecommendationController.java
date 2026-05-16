package com.streetLeague.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streetLeague.backend.entity.Product;
import com.streetLeague.backend.service.RecommendationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/recommend")
    public List<Product> recommend(
            @RequestParam String category,
            @RequestParam double maxPrice
    ) {
        return recommendationService.recommend(category, maxPrice);
    }
}