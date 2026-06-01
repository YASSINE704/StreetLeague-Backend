package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.PostDTO;
import com.streetLeague.backend.security.CustomUserDetailsService;
import com.streetLeague.backend.security.JwtService;
import com.streetLeague.backend.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getAll_shouldReturnList() throws Exception {
        when(postService.getAll()).thenReturn(List.of(
                PostDTO.builder().id(1L).title("Post 1").build(),
                PostDTO.builder().id(2L).title("Post 2").build()
        ));

        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getById_shouldReturnPost() throws Exception {
        when(postService.getById(1L)).thenReturn(
                PostDTO.builder().id(1L).title("Test Post").content("Content").build());

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    void create_shouldReturnOk() throws Exception {
        PostDTO request = PostDTO.builder().title("New Post").content("Content").build();
        PostDTO response = PostDTO.builder().id(1L).title("New Post").content("Content").build();

        when(postService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Post"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTrending_shouldReturnList() throws Exception {
        when(postService.getTrendingPosts()).thenReturn(List.of(
                PostDTO.builder().id(1L).title("Trending").build()));

        mockMvc.perform(get("/api/posts/trending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getByTag_shouldReturnList() throws Exception {
        when(postService.getByTag("sport")).thenReturn(List.of(
                PostDTO.builder().id(1L).title("Sport Post").build()));

        mockMvc.perform(get("/api/posts/tag/sport"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
