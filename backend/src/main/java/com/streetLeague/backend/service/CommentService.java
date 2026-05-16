package com.streetLeague.backend.service;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.streetLeague.backend.dto.CommentDTO;
import com.streetLeague.backend.entity.Comment;
import com.streetLeague.backend.entity.Post;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.mapper.CommentMapper;
import com.streetLeague.backend.repository.CommentRepository;
import com.streetLeague.backend.repository.PostRepository;
import com.streetLeague.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentDTO create(CommentDTO dto) {

        if (dto.getUser() == null || dto.getUser().getIdUser() == null) {
            throw new RuntimeException("User is required");
        }

        if (dto.getPostId() == null) {
            throw new RuntimeException("PostId is required");
        }

        User user = userRepository.findById(dto.getUser().getIdUser())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = commentMapper.toEntity(dto);
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        return commentMapper.toDTO(commentRepository.save(comment));
    }

    public List<CommentDTO> getAll() {
        return commentRepository.findAll()
                .stream()
                .map(commentMapper::toDTO)
                .toList();
    }

    public List<CommentDTO> getByPost(Long postId) {

        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post not found");
        }

        return commentRepository.findByPostId(postId)
                .stream()
                .map(commentMapper::toDTO)
                .toList();
    }

    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comment not found");
        }
        commentRepository.deleteById(id);
    }
}