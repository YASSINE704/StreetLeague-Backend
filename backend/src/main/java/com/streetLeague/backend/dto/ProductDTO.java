package com.streetLeague.backend.dto;

import com.streetLeague.backend.entity.User;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String subcategory;
    private Integer pointure;
    private String taille;
    private String imageUrl;
    private User user;
}
