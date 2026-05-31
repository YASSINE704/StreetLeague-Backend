package com.streetLeague.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streetLeague.backend.service.ForumAIService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/forum-ai")
@RequiredArgsConstructor
public class ForumAIController {

    private final ForumAIService forumAIService;

    @GetMapping("/smart-reply/{postId}")
    public ResponseEntity<String> smartReply(@PathVariable Long postId) {
        return ResponseEntity.ok(forumAIService.suggestReply(postId));
    }
}