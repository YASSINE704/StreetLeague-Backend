package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.PostDTO;
import com.streetLeague.backend.entity.Comment;
import com.streetLeague.backend.entity.Post;
import com.streetLeague.backend.entity.Tag;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.mapper.PostMapper;
import com.streetLeague.backend.repository.PostRepository;
import com.streetLeague.backend.repository.TagRepository;
import com.streetLeague.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    private final PostMapper postMapper = new PostMapper();

    private PostService postService;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, userRepository, postMapper, tagRepository);
    }

    private User createUser() {
        return User.builder().idUser(1).nom("Test").prenom("User").build();
    }

    @Test
    void create_shouldSucceedWithNewTags() {
        User user = createUser();
        PostDTO dto = PostDTO.builder().title("Title").content("Content").user(user).tags(List.of("newTag")).build();
        Post savedPost = Post.builder().id(1L).title("Title").content("Content").user(user).createdAt(LocalDateTime.now()).tags(new java.util.ArrayList<>()).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(tagRepository.findByName("newTag")).thenReturn(null);
        when(tagRepository.save(any(Tag.class))).thenAnswer(inv -> inv.getArgument(0));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostDTO result = postService.create(dto);

        assertThat(result.getTitle()).isEqualTo("Title");
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void create_shouldUseExistingTags() {
        User user = createUser();
        Tag existingTag = Tag.builder().id(1L).name("existing").build();
        PostDTO dto = PostDTO.builder().title("Title").content("Content").user(user).tags(List.of("existing")).build();
        Post savedPost = Post.builder().id(1L).title("Title").content("Content").user(user).createdAt(LocalDateTime.now()).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(tagRepository.findByName("existing")).thenReturn(existingTag);
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostDTO result = postService.create(dto);

        assertThat(result.getTitle()).isEqualTo("Title");
        verify(tagRepository, never()).save(any(Tag.class));
    }

    @Test
    void create_whenUserNotFound_shouldThrow() {
        PostDTO dto = PostDTO.builder().user(User.builder().idUser(99).build()).build();

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getAll_shouldReturnList() {
        when(postRepository.findAll()).thenReturn(List.of(
                Post.builder().id(1L).build(), Post.builder().id(2L).build()
        ));

        List<PostDTO> result = postService.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void getById_whenFound_shouldReturn() {
        Post post = Post.builder().id(1L).title("Title").build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostDTO result = postService.getById(1L);

        assertThat(result.getTitle()).isEqualTo("Title");
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void update_shouldSucceed() {
        Post existing = Post.builder().id(1L).title("Old").content("Old content").build();
        PostDTO dto = PostDTO.builder().title("New").content("New content").build();
        Post saved = Post.builder().id(1L).title("New").content("New content").build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenReturn(saved);

        PostDTO result = postService.update(1L, dto);

        assertThat(result.getTitle()).isEqualTo("New");
    }

    @Test
    void delete_shouldSucceed() {
        when(postRepository.existsById(1L)).thenReturn(true);

        postService.delete(1L);

        verify(postRepository).deleteById(1L);
    }

    @Test
    void delete_whenNotFound_shouldThrow() {
        when(postRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> postService.delete(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void getByTag_shouldReturnPosts() {
        Tag tag = Tag.builder().id(1L).name("sport").build();
        Post post1 = Post.builder().id(1L).title("Post 1").build();
        Post post2 = Post.builder().id(2L).title("Post 2").build();
        tag.setPosts(List.of(post1, post2));

        when(tagRepository.findByName("sport")).thenReturn(tag);

        List<PostDTO> result = postService.getByTag("sport");

        assertThat(result).hasSize(2);
    }

    @Test
    void getTrendingPosts_shouldSortByScoreDesc() {
        Post oldPost = Post.builder().id(1L).title("Old").createdAt(LocalDateTime.now().minusHours(10)).comments(List.of()).build();
        Post newPost = Post.builder().id(2L).title("New").createdAt(LocalDateTime.now().minusHours(1)).comments(List.of(new Comment())).build();

        when(postRepository.findAll()).thenReturn(List.of(oldPost, newPost));

        List<PostDTO> result = postService.getTrendingPosts();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("New");
    }

    @Test
    void calculateTrendScore_shouldComputeCorrectly() {
        Post post = Post.builder()
                .id(1L)
                .createdAt(LocalDateTime.now().minusHours(2))
                .comments(List.of(new Comment(), new Comment(), new Comment()))
                .build();

        double score = postService.calculateTrendScore(post);

        assertThat(score).isEqualTo(15.0);
    }
}
