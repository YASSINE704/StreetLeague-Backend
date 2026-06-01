package com.streetLeague.backend.repository;

import com.streetLeague.backend.entity.Comment;
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
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void findByPostId_shouldReturnComments() {
        User user = em.persist(User.builder().nom("User").email("user@test.com").role(Role.SPORTIF).build());
        Post post = em.persist(Post.builder().title("Post").content("Content").user(user).build());
        em.persist(Comment.builder().content("Comment 1").post(post).user(user).build());
        em.persist(Comment.builder().content("Comment 2").post(post).user(user).build());

        List<Comment> result = commentRepository.findByPostId(post.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    void findByPostId_whenNoComments_shouldReturnEmpty() {
        User user = em.persist(User.builder().nom("User").email("user2@test.com").role(Role.SPORTIF).build());
        Post post = em.persist(Post.builder().title("Post").content("Content").user(user).build());

        List<Comment> result = commentRepository.findByPostId(post.getId());

        assertThat(result).isEmpty();
    }
}
