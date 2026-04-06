package com.streetLeague.backend.dto;

import com.streetLeague.backend.enums.TypeSport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for terrain information.
 *
 * Used for API responses to avoid exposing JPA relationships directly.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerrainDTO {

    private Long id;
    private String nom;
    private TypeSport typeSport;
    private String location;
    private String address;
    private LocalDateTime availabilityStart;
    private LocalDateTime availabilityEnd;
}
