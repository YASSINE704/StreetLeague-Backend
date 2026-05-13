package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.TypeSport;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Equipe (Team) Entity - Represents a sports team.
 * 
 * This entity represents a team that consists of multiple players (Joueurs).
 * Each team has a name, plays a specific sport type, has players, and tracks statistics.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Entity
@Table(name = "equipe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipe {

    /**
     * Unique identifier for the team.
     * Auto-generated on database insert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the team.
     * Required field - cannot be blank.
     */
    @NotBlank(message = "Team name cannot be blank")
    @Column(nullable = false, length = 100)
    private String nom;

    /**
     * Type of sport this team plays.
     * Possible values: FOOTBALL, BASKETBALL, VOLLEYBALL, PADEL
     */
    @NotNull(message = "Sport type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeSport typeSport;

    /**
     * List of players belonging to this team.
     * One team can have many players.
     * Cascade all operations (delete team -> delete associated joueurs).
     */
    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Joueur> joueurs;

    /**
     * Number of matches won by this team.
     * Used for team statistics and rankings.
     */
    @Min(value = 0, message = "Wins cannot be negative")
    @Column(nullable = false)
    private int wins = 0;

    /**
     * Number of matches lost by this team.
     * Used for team statistics and rankings.
     */
    @Min(value = 0, message = "Losses cannot be negative")
    @Column(nullable = false)
    private int losses = 0;

    /**
     * Number of matches drawn/tied by this team.
     * Used for team statistics and rankings.
     */
    @Min(value = 0, message = "Draws cannot be negative")
    @Column(nullable = false)
    private int draws = 0;

    /**
     * Total number of goals scored by this team.
     * Aggregate statistic for team performance.
     */
    @Min(value = 0, message = "Goals for cannot be negative")
    @Column(nullable = false)
    private int goalsFor = 0;

    /**
     * Total number of goals conceded/against by this team.
     * Aggregate statistic for team performance.
     */
    @Min(value = 0, message = "Goals against cannot be negative")
    @Column(nullable = false)
    private int goalsAgainst = 0;

    /**
     * Matches where this team is the home team.
     * One team can play many home matches.
     */
    @OneToMany(mappedBy = "homeTeam", fetch = FetchType.LAZY)
    private List<Match> homeMatches;

    /**
     * Matches where this team is the away team.
     * One team can play many away matches.
     */
    @OneToMany(mappedBy = "awayTeam", fetch = FetchType.LAZY)
    private List<Match> awayMatches;

}

