package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.Niveau;
import com.streetLeague.backend.enums.Position;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Joueur (Player) Entity - Represents a sports player.
 * 
 * This entity represents a player who can be associated with a team (Equipe).
 * Each player has personal attributes, performance tracking, and match history.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Entity
@Table(name = "joueur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Joueur {

    /**
     * Unique identifier for the player.
     * Auto-generated on database insert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Full name of the player.
     * Required field - cannot be blank.
     */
    @NotBlank(message = "Player name cannot be blank")
    @Column(nullable = false, length = 100)
    private String nom;

    /**
     * Age of the player.
     * Must be between 6 and 100 years old.
     */
    @Min(value = 6, message = "Player age must be at least 6")
    @Max(value = 100, message = "Player age cannot exceed 100")
    @Column(nullable = false)
    private int age;

    /**
     * Skill level of the player.
     * Possible values: BEGINNER, INTERMEDIATE, ADVANCED
     */
    @NotNull(message = "Skill level cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Niveau niveau;

    /**
     * Position of the player on the field.
     * Possible values: GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD, UTILITY
     */
    @NotNull(message = "Position cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    /**
     * The team this player belongs to.
     * Many players can belong to one team.
     * This relationship is optional (a player might not be assigned to a team yet).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id")
    private Equipe equipe;

    /**
     * Performance statistics for each match the player participated in.
     * One player can have many performance records (one per match).
     */
    @OneToMany(mappedBy = "joueur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlayerStats> playerStats;

}

