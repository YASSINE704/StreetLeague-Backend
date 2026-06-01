package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Post;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PostRepository postRepository;

    @Test
    void saveAndFind_shouldWork() {
        User user = em.persist(User.builder().nom("Author").prenom("Test").email("author@test.com").role(Role.SPORTIF).build());
        Post post = Post.builder().title("Test Post").content("Content").user(user).build();
        em.persist(post);

        List<Post> all = postRepository.findAll();

        assertThat(all).hasSize(1);
        assertThat(all.get(0).getTitle()).isEqualTo("Test Post");
    }

    @Test
    void findById_shouldReturnCorrectPost() {
        User user = em.persist(User.builder().nom("Author").prenom("Test").email("author2@test.com").role(Role.SPORTIF).build());
        Post post = Post.builder().title("Specific Post").content("Content").user(user).build();
        Long id = em.persist(post).getId();

        var result = postRepository.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Specific Post");
    }
}
