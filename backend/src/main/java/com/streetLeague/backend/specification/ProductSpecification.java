package com.streetLeague.backend.specification;

import org.springframework.data.jpa.domain.Specification;

import com.streetLeague.backend.entity.Product;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> build(String query, String category, String subcategory,
                                                Double minPrice, Double maxPrice,
                                                Integer pointure, String taille) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query != null && !query.isBlank()) {
                String pattern = "%" + query.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                ));
            }

            if (category != null && !category.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            if (subcategory != null && !subcategory.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("subcategory"), subcategory));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (pointure != null) {
                predicates.add(criteriaBuilder.equal(root.get("pointure"), pointure));
            }

            if (taille != null && !taille.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("taille"), taille));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
