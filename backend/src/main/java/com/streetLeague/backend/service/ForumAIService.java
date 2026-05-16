package com.streetLeague.backend.service;

import org.springframework.stereotype.Service;

import com.streetLeague.backend.entity.Post;
import com.streetLeague.backend.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForumAIService {

    private final AIService aiService;
    private final PostRepository postRepository;

    public String suggestReply(Long postId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String context = "Titre: " + post.getTitle() + "\n\nContenu:\n" + post.getContent();
        return aiService.generateSmartReply(context);
    }
}