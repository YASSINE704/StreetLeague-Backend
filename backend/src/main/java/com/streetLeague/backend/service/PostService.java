package com.streetLeague.backend.service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


import com.streetLeague.backend.dto.PostDTO;
import com.streetLeague.backend.entity.Post;
import com.streetLeague.backend.entity.Tag;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.mapper.PostMapper;
import com.streetLeague.backend.repository.PostRepository;
import com.streetLeague.backend.repository.UserRepository;
import com.streetLeague.backend.repository.TagRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;
    private final TagRepository tagRepository;

    @Transactional
public PostDTO create(PostDTO dto) {

    User user = userRepository.findById(dto.getUser().getIdUser())
            .orElseThrow(() -> new RuntimeException("User not found"));

    Post post = new Post();
    post.setTitle(dto.getTitle());
    post.setContent(dto.getContent());
    post.setCreatedAt(LocalDateTime.now());
    post.setUser(user);

    List<Tag> tags = new ArrayList<>();

    if (dto.getTags() != null) {
        for (String tagName : dto.getTags()) {

            Tag tag = tagRepository.findByName(tagName);

            if (tag == null) {
                tag = Tag.builder()
                        .name(tagName)
                        .build();
                tagRepository.save(tag);
            }

            tags.add(tag);
        }
    }

    post.setTags(tags);

    return postMapper.toDTO(postRepository.save(post));
}

    public List<PostDTO> getAll() {
        return postRepository.findAll()
                .stream()
                .map(PostMapper::toDTO)
                .toList();
    }

    public PostDTO getById(Long id) {
        return postRepository.findById(id)
                .map(PostMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public PostDTO update(Long id, PostDTO dto) {

        Post existing = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        existing.setTitle(dto.getTitle());
        existing.setContent(dto.getContent());

        return PostMapper.toDTO(postRepository.save(existing));
    }

    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found");
        }
        postRepository.deleteById(id);
    }
    public double calculateTrendScore(Post post) {

        int commentCount = post.getComments() == null ? 0 : post.getComments().size();

        long hoursOld = ChronoUnit.HOURS.between(
                post.getCreatedAt(),
                LocalDateTime.now()
        );

        if (hoursOld == 0) hoursOld = 1;

        return (commentCount * 10.0) / hoursOld;
    }

    public List<PostDTO> getTrendingPosts() {
        return postRepository.findAll()
                .stream()
                .sorted((a, b) ->
                        Double.compare(calculateTrendScore(b), calculateTrendScore(a)))
                .map(post -> {
                    PostDTO dto = PostMapper.toDTO(post);
                    dto.setTrendScore(calculateTrendScore(post));
                    return dto;
                })
                .toList();
    }
    public List<PostDTO> getByTag(String tagName) {

    Tag tag = tagRepository.findByName(tagName);

    return tag.getPosts()
            .stream()
            .map(PostMapper::toDTO)
            .toList();
}
}