package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.CommentDTO;
import com.streetLeague.backend.security.CustomUserDetailsService;
import com.streetLeague.backend.security.JwtService;
import com.streetLeague.backend.service.CommentService;
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

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getAll_shouldReturnList() throws Exception {
        when(commentService.getAll()).thenReturn(List.of(
                CommentDTO.builder().id(1L).content("Comment 1").build()));

        mockMvc.perform(get("/api/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void create_shouldReturnOk() throws Exception {
        CommentDTO request = CommentDTO.builder().content("Nice post!").build();
        CommentDTO response = CommentDTO.builder().id(1L).content("Nice post!").build();

        when(commentService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Nice post!"));
    }

    @Test
    void getByPost_shouldReturnList() throws Exception {
        when(commentService.getByPost(1L)).thenReturn(List.of(
                CommentDTO.builder().id(1L).content("Comment").build()));

        mockMvc.perform(get("/api/comments/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNoContent());
    }
}
