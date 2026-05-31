package com.streetLeague.backend.dto;

import java.time.LocalDateTime;
import com.streetLeague.backend.entity.User;
import lombok.*;

@Getter @Setter @Builder
public class CommentDTO {

    private Long id;
    private String content;
    private LocalDateTime createdAt;

    private Long postId;
    private User user;
}