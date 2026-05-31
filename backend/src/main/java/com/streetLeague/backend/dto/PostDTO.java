package com.streetLeague.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.streetLeague.backend.entity.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class PostDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<String> tags;
    private User user;
    private int commentCount;
    private double trendScore;
}