package com.streetLeague.backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.streetLeague.backend.dto.ProductDTO;
import com.streetLeague.backend.dto.ProductSearchRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarketplaceAIService {

    private final AIService aiService;
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public Map<String, Object> parseSearch(String query) {

        String json = aiService.smartSearch(query);

        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("raw", json);
            result.put("parseError", e.getMessage());
            return result;
        }
    }

    public Page<ProductDTO> searchWithAI(String query, int page, int size) {
        String json = aiService.smartSearch(query);
        Map<String, Object> filters;
        try {
            filters = objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            return Page.empty();
        }

        ProductSearchRequest req = new ProductSearchRequest();
        req.setQuery((String) filters.get("searchTerm"));
        req.setCategory((String) filters.get("category"));
        req.setMaxPrice(filters.get("maxPrice") instanceof Number
                ? ((Number) filters.get("maxPrice")).doubleValue() : null);
        req.setMinPrice(filters.get("minPrice") instanceof Number
                ? ((Number) filters.get("minPrice")).doubleValue() : null);
        req.setPage(page);
        req.setSize(size);

        return productService.search(req);
    }
}
