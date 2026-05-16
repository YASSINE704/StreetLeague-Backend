package com.streetLeague.backend.mapper;

import org.springframework.stereotype.Component;

import com.streetLeague.backend.dto.ProductDTO;
import com.streetLeague.backend.entity.Product;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        if (product == null) return null;

        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .subcategory(product.getSubcategory())
                .pointure(product.getPointure())
                .taille(product.getTaille())
                .imageUrl(product.getImageUrl())
                .user(product.getUser())
                .build();
    }

    public Product toEntity(ProductDTO dto) {
        if (dto == null) return null;

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .category(dto.getCategory())
                .subcategory(dto.getSubcategory())
                .pointure(dto.getPointure())
                .taille(dto.getTaille())
                .imageUrl(dto.getImageUrl())
                .user(dto.getUser())
                .build();
    }
}
