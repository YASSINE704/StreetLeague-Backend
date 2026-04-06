package com.streetLeague.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PlayerStats Entity - Tracks player performance statistics for individual matches.
 * 
 * Records detailed performance metrics for each player in each match,
 * including goals, assists, minutes played, and overall performance rating.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Entity
@Table(name = "player_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStats {

    /**
     * Unique identifier for the player stats record.
     * Auto-generated on database insert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Reference to the player who performed.
     * Many stats records can belong to one player.
     */
    @NotNull(message = "Player cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joueur_id", nullable = false)
    private Joueur joueur;

    /**
     * Reference to the match in which these stats were recorded.
     * Many stats can be recorded from one match.
     */
    @NotNull(message = "Match cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    /**
     * Number of goals scored by the player in this match.
     * Minimum value is 0.
     */
    @Min(value = 0, message = "Goals cannot be negative")
    @Column(nullable = false)
    private int goals = 0;

    /**
     * Number of assists contributed by the player in this match.
     * Minimum value is 0.
     */
    @Min(value = 0, message = "Assists cannot be negative")
    @Column(nullable = false)
    private int assists = 0;

    /**
     * Number of minutes the player participated in this match.
     * Minimum value is 0, maximum is typically 90+ (with extra time).
     */
    @Min(value = 0, message = "Minutes played cannot be negative")
    @Column(nullable = false)
    private int minutesPlayed = 0;

    /**
     * Overall performance rating for this match (0-10 scale).
     * Used for evaluating player performance and consistency.
     */
    @Min(value = 0, message = "Rating cannot be negative")
    @Column(nullable = false)
    private double performanceRating = 0.0;

}
