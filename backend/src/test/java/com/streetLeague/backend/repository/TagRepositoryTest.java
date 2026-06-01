package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TagRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private TagRepository tagRepository;

    @Test
    void findByName_shouldReturnTag() {
        Tag tag = Tag.builder().name("sport").build();
        em.persist(tag);

        Tag result = tagRepository.findByName("sport");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("sport");
    }

    @Test
    void findByName_whenNotFound_shouldReturnNull() {
        Tag result = tagRepository.findByName("nonexistent");

        assertThat(result).isNull();
    }
}
