package com.streetLeague.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductSearchRequest {
    private String query;
    private String category;
    private String subcategory;
    private Double minPrice;
    private Double maxPrice;
    private Integer pointure;
    private String taille;
    @Builder.Default private String sortBy = "id";
    @Builder.Default private String sortDir = "desc";
    @Builder.Default private int page = 0;
    @Builder.Default private int size = 12;
}
