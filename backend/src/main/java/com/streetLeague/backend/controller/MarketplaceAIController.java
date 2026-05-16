package com.streetLeague.backend.controller;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streetLeague.backend.dto.ProductDTO;
import com.streetLeague.backend.service.MarketplaceAIService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/market-ai")
@RequiredArgsConstructor
public class MarketplaceAIController {

    private final MarketplaceAIService marketplaceAIService;

    @GetMapping("/smart-search")
    public ResponseEntity<Map<String, Object>> smartSearch(@RequestParam String query) {
        return ResponseEntity.ok(marketplaceAIService.parseSearch(query));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> aiSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(marketplaceAIService.searchWithAI(query, page, size));
    }
}
