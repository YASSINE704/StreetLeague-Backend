package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Product;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.specification.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProductRepository productRepository;

    private User createUser() {
        return em.persist(User.builder().nom("Seller").prenom("Test").email("seller@test.com").role(Role.SPORTIF).build());
    }

    @Test
    void saveAndFindAll_shouldWork() {
        User user = createUser();
        em.persist(Product.builder().name("Ball").price(29.99).category("Sports").user(user).build());
        em.persist(Product.builder().name("Shoe").price(59.99).category("Sports").user(user).build());

        List<Product> all = productRepository.findAll();

        assertThat(all).hasSize(2);
    }

    @Test
    void searchByCategory_shouldFilter() {
        User user = createUser();
        em.persist(Product.builder().name("Ball").price(29.99).category("Sports").user(user).build());
        em.persist(Product.builder().name("T-Shirt").price(19.99).category("Clothing").user(user).build());

        Specification<Product> spec = ProductSpecification.build(null, "Sports", null, null, null, null, null);
        var result = productRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Ball");
    }

    @Test
    void searchByKeyword_shouldMatchName() {
        User user = createUser();
        em.persist(Product.builder().name("Football Ball").price(29.99).category("Sports").user(user).build());
        em.persist(Product.builder().name("T-Shirt").price(19.99).category("Clothing").user(user).build());

        Specification<Product> spec = ProductSpecification.build("football", null, null, null, null, null, null);
        var result = productRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Football Ball");
    }

    @Test
    void searchByPriceRange_shouldFilter() {
        User user = createUser();
        em.persist(Product.builder().name("Cheap").price(5.0).category("Sports").user(user).build());
        em.persist(Product.builder().name("Medium").price(50.0).category("Sports").user(user).build());
        em.persist(Product.builder().name("Expensive").price(200.0).category("Sports").user(user).build());

        Specification<Product> spec = ProductSpecification.build(null, null, null, 10.0, 100.0, null, null);
        var result = productRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Medium");
    }

    @Test
    void searchWithMultipleFilters_shouldWork() {
        User user = createUser();
        em.persist(Product.builder().name("Nike Shoe").price(89.99).category("Sports").subcategory("Shoes").pointure(42).user(user).build());
        em.persist(Product.builder().name("Adidas Shoe").price(79.99).category("Sports").subcategory("Shoes").pointure(44).user(user).build());
        em.persist(Product.builder().name("T-Shirt").price(19.99).category("Clothing").user(user).build());

        Specification<Product> spec = ProductSpecification.build("shoe", null, "Shoes", null, null, 42, null);
        var result = productRepository.findAll(spec);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Nike Shoe");
    }

    @Test
    void searchWithPagination_shouldWork() {
        User user = createUser();
        for (int i = 1; i <= 5; i++) {
            em.persist(Product.builder().name("Product " + i).price(i * 10.0).category("Sports").user(user).build());
        }

        var page = productRepository.findAll(
                Specification.where(null),
                PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "id")));

        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }
}
