package com.streetLeague.backend.controller;

import com.streetLeague.backend.dto.ProductDTO;
import com.streetLeague.backend.security.CustomUserDetailsService;
import com.streetLeague.backend.security.JwtService;
import com.streetLeague.backend.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getAll_shouldReturnList() throws Exception {
        when(productService.getAll()).thenReturn(List.of(
                ProductDTO.builder().id(1L).name("Ball").build()));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getById_shouldReturnProduct() throws Exception {
        when(productService.getById(1L)).thenReturn(
                ProductDTO.builder().id(1L).name("Ball").price(29.99).build());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ball"));
    }

    @Test
    void create_shouldReturnOk() throws Exception {
        ProductDTO request = ProductDTO.builder().name("New Product").price(19.99).build();
        ProductDTO response = ProductDTO.builder().id(1L).name("New Product").price(19.99).build();

        when(productService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void delete_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void uploadImage_shouldReturnOk() throws Exception {
        when(productService.uploadImage(anyLong(), any())).thenReturn(
                ProductDTO.builder().id(1L).name("Ball").imageUrl("uploads/1/img.jpg").build());

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());

        mockMvc.perform(multipart("/api/products/1/image")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value("uploads/1/img.jpg"));
    }
}
