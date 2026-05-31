package com.streetLeague.backend.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.streetLeague.backend.dto.PostDTO;
import com.streetLeague.backend.entity.Post;
import com.streetLeague.backend.entity.Tag;

@Component
public class PostMapper {

    public static PostDTO toDTO(Post post) {
        if (post == null) return null;

        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .user(post.getUser()) // same approach as Product
                .tags(
                post.getTags() != null
                ? post.getTags().stream()
                    .map(Tag::getName)
                    .toList()
                : new ArrayList<>()
            )
                .commentCount(post.getComments() != null ? post.getComments().size() : 0)
                .build();
    }

    public static Post toEntity(PostDTO dto) {
        if (dto == null) return null;

        return Post.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .createdAt(dto.getCreatedAt())
                .user(dto.getUser()) // ⚠️ will be overridden in service
                .build();
    }
}