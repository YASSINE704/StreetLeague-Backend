package com.streetLeague.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.streetLeague.backend.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {}