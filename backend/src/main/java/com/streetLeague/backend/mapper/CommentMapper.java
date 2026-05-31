package com.streetLeague.backend.mapper;

import org.springframework.stereotype.Component;

import com.streetLeague.backend.dto.CommentDTO;
import com.streetLeague.backend.entity.Comment;

@Component
public class CommentMapper {

    public CommentDTO toDTO(Comment comment) {
        if (comment == null) return null;

        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .postId(comment.getPost() != null ? comment.getPost().getId() : null) // avoid full post
                .user(comment.getUser())
                .build();
    }

    public Comment toEntity(CommentDTO dto) {
        if (dto == null) return null;

        return Comment.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .user(dto.getUser()) // ⚠️ override later
                .build();
    }
}
