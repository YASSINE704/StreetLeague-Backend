package com.streetLeague.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.streetLeague.backend.entity.Product;
import com.streetLeague.backend.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ProductRepository productRepository;

    public List<Product> recommend(String category, double maxPrice) {

        return productRepository.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .filter(p -> p.getPrice() <= maxPrice)
                .toList();
    }
}
