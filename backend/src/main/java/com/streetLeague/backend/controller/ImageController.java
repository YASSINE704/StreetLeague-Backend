package com.streetLeague.backend.controller;

import java.net.URLConnection;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.streetLeague.backend.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/uploads/products/{productId}/{filename:.+}")
    public ResponseEntity<Resource> getProductImage(@PathVariable Long productId,
                                                     @PathVariable String filename) {
        String imageUrl = "uploads/products/" + productId + "/" + filename;
        Resource resource = imageService.loadImageAsResource(imageUrl);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }
        String contentType = URLConnection.guessContentTypeFromName(filename);
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }
}
