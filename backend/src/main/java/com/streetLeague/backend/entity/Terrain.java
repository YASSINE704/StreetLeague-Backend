package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.TypeSport;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Terrain (Field) Entity - Represents a sports field/court.
 * 
 * This entity represents a physical terrain (field/court) where matches
 * can be played. Each terrain has a name, location, and availability schedule.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Entity
@Table(name = "terrain")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Terrain {

    /**
     * Unique identifier for the terrain.
     * Auto-generated on database insert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the terrain/field.
     * Required field - cannot be blank.
     */
    @NotBlank(message = "Terrain name cannot be blank")
    @Column(nullable = false, length = 100)
    private String nom;

    /**
     * Type of sport played on this terrain.
     * Possible values: FOOTBALL, BASKETBALL, VOLLEYBALL, PADEL
     */
    @NotNull(message = "Sport type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeSport typeSport;

    /**
     * Location/City where the terrain is situated.
     * e.g., "Downtown", "Central Park", "Sports Complex"
     */
    @NotBlank(message = "Location cannot be blank")
    @Column(nullable = false, length = 150)
    private String location;

    /**
     * Full address of the terrain including street and postal info.
     * Used for navigation and booking purposes.
     */
    @NotBlank(message = "Address cannot be blank")
    @Column(nullable = false, length = 250)
    private String address;

    /**
     * Start time when the terrain becomes available each day.
     * Stored as LocalDateTime for flexibility in daily scheduling.
     */
    @Column(nullable = true)
    private LocalDateTime availabilityStart;

    /**
     * End time when the terrain availability closes each day.
     * Stored as LocalDateTime for flexibility in daily scheduling.
     */
    @Column(nullable = true)
    private LocalDateTime availabilityEnd;

    /**
     * Matches scheduled on this terrain.
     * One terrain can host many matches over time.
     */
    @OneToMany(mappedBy = "terrain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Match> matches;

}

