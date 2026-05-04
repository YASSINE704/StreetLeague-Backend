package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.MatchStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Match Entity - Represents a sports match/game between two teams.
 * 
 * This entity represents a complete match between two teams played on a specific terrain.
 * Each match has scheduling information, team assignments, results, and player statistics.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Entity
@Table(name = "match_game")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    /**
     * Unique identifier for the match.
     * Auto-generated on database insert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Home team for this match.
     * Many matches can involve one team.
     */
    @NotNull(message = "Home team cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Equipe homeTeam;

    /**
     * Away team for this match.
     * Many matches can involve one team.
     */
    @NotNull(message = "Away team cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id", nullable = false)
    private Equipe awayTeam;

    /**
     * The terrain/field where this match will be played.
     * Many matches can be played on one terrain.
     */
    @NotNull(message = "Terrain cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terrain_id", nullable = false)
    private Terrain terrain;

    /**
     * Date and time when the match is scheduled to be played.
     * Can be past dates for completed matches, future for scheduled matches.
     */
    @NotNull(message = "Match date cannot be null")
    @Column(nullable = false)
    private LocalDateTime matchDate;

    /**
     * Current status of the match (SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED).
     */
    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status = MatchStatus.SCHEDULED;

    /**
     * Goals scored by the home team.
     * Only set when match is COMPLETED.
     */
    @Min(value = 0, message = "Home team score cannot be negative")
    @Column(nullable = true)
    private Integer homeTeamScore;

    /**
     * Goals scored by the away team.
     * Only set when match is COMPLETED.
     */
    @Min(value = 0, message = "Away team score cannot be negative")
    @Column(nullable = true)
    private Integer awayTeamScore;

    /**
     * List of player statistics from this match.
     * One match can generate many player stats records.
     */
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlayerStats> playerStats;

}

