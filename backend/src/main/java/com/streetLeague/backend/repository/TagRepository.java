package com.streetLeague.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.streetLeague.backend.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}