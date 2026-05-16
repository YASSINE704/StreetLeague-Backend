package com.streetLeague.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Configuration for StreetLeague Backend API.
 * 
 * Configures cross-origin resource sharing (CORS) to allow the Angular frontend
 * running on localhost:4200 to communicate with this backend API.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Configuration
public class WebConfig {

    /**
     * Configures CORS settings for the application.
     * 
     * Allows requests from the Angular frontend with the following permissions:
     * - Origin: http://localhost:4200 (development frontend)
     * - Methods: GET, POST, PUT, DELETE, OPTIONS
     * - Headers: All headers allowed
     * - Credentials: Not allowed
     * 
     * @return Configured WebMvcConfigurer with CORS mappings
     */
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/uploads/products/**")
                        .addResourceLocations("file:" + uploadDir + "/");
            }
        };
    }
}

