package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.CommentDTO;
import com.streetLeague.backend.entity.Comment;
import com.streetLeague.backend.entity.Post;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.mapper.CommentMapper;
import com.streetLeague.backend.repository.CommentRepository;
import com.streetLeague.backend.repository.PostRepository;
import com.streetLeague.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @Test
    void create_shouldSucceed() {
        User user = User.builder().idUser(1).nom("Test").build();
        Post post = Post.builder().id(1L).build();
        CommentDTO dto = CommentDTO.builder().content("Nice post!").postId(1L).user(user).build();
        Comment entity = Comment.builder().content("Nice post!").build();
        Comment saved = Comment.builder().id(1L).content("Nice post!").createdAt(LocalDateTime.now()).user(user).post(post).build();
        CommentDTO resultDto = CommentDTO.builder().id(1L).content("Nice post!").postId(1L).user(user).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentMapper.toEntity(dto)).thenReturn(entity);
        when(commentRepository.save(any(Comment.class))).thenReturn(saved);
        when(commentMapper.toDTO(saved)).thenReturn(resultDto);

        CommentDTO result = commentService.create(dto);

        assertThat(result.getContent()).isEqualTo("Nice post!");
    }

    @Test
    void create_whenUserNull_shouldThrow() {
        CommentDTO dto = CommentDTO.builder().content("test").build();

        assertThatThrownBy(() -> commentService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User is required");
    }

    @Test
    void create_whenUserNotFound_shouldThrow() {
        User user = User.builder().idUser(99).build();
        CommentDTO dto = CommentDTO.builder().content("test").postId(1L).user(user).build();

        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void create_whenPostIdNull_shouldThrow() {
        User user = User.builder().idUser(1).build();
        CommentDTO dto = CommentDTO.builder().content("test").user(user).build();

        assertThatThrownBy(() -> commentService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("PostId is required");
    }

    @Test
    void create_whenPostNotFound_shouldThrow() {
        User user = User.builder().idUser(1).build();
        CommentDTO dto = CommentDTO.builder().content("test").postId(99L).user(user).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.create(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Post not found");
    }

    @Test
    void getAll_shouldReturnList() {
        when(commentRepository.findAll()).thenReturn(List.of(
                Comment.builder().id(1L).build(), Comment.builder().id(2L).build()
        ));
        when(commentMapper.toDTO(any())).thenReturn(
                CommentDTO.builder().id(1L).build(),
                CommentDTO.builder().id(2L).build()
        );

        List<CommentDTO> result = commentService.getAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void getByPost_shouldReturnList() {
        when(postRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findByPostId(1L)).thenReturn(List.of(
                Comment.builder().id(1L).build()
        ));
        when(commentMapper.toDTO(any())).thenReturn(CommentDTO.builder().id(1L).build());

        List<CommentDTO> result = commentService.getByPost(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getByPost_whenPostNotFound_shouldThrow() {
        when(postRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> commentService.getByPost(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Post not found");
    }

    @Test
    void delete_shouldSucceed() {
        when(commentRepository.existsById(1L)).thenReturn(true);

        commentService.delete(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void delete_whenNotFound_shouldThrow() {
        when(commentRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> commentService.delete(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Comment not found");
    }
}
