package com.streetLeague.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streetLeague.backend.dto.CommentDTO;
import com.streetLeague.backend.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> create(@RequestBody CommentDTO dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> getAll() {
        return ResponseEntity.ok(commentService.getAll());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getByPost(postId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}