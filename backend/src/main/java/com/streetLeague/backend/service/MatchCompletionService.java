package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.PlayerStats;
import com.streetLeague.backend.entity.Joueur;
import com.streetLeague.backend.entity.Match;
import com.streetLeague.backend.repository.PlayerStatsRepository;
import com.streetLeague.backend.enums.MatchStatus;
import com.streetLeague.backend.enums.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Service for recording player statistics after a match is completed.
 * 
 * Automatically generates player stats for all players in a match
 * when the match status is changed to COMPLETED.
 * 
 * In a real application, stats would come from:
 * - Live tracking system
 * - Coach input via UI
 * - Third-party sports analytics API
 * 
 * For now, generates realistic random values for demonstration.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
public class MatchCompletionService {

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    private static final Random random = new Random();

    /**
     * Records player statistics for all players in a completed match.
     * 
     * Called when a match status changes to COMPLETED.
     * Creates PlayerStats records for each player who participated.
     * 
     * @param match The completed match
     */
    public void recordMatchStatistics(Match match) {
        if (match.getStatus() != MatchStatus.COMPLETED) {
            return; // Only record stats for completed matches
        }

        System.out.println("📊 Recording player statistics for match: " + match.getId());

        // Record stats for home team players
        if (match.getHomeTeam() != null && match.getHomeTeam().getJoueurs() != null) {
            for (Joueur player : match.getHomeTeam().getJoueurs()) {
                recordPlayerStats(player, match);
            }
        }

        // Record stats for away team players
        if (match.getAwayTeam() != null && match.getAwayTeam().getJoueurs() != null) {
            for (Joueur player : match.getAwayTeam().getJoueurs()) {
                recordPlayerStats(player, match);
            }
        }

        System.out.println("✅ Match statistics recorded successfully!");
    }

    /**
     * Records individual player performance for a match.
     * 
     * Generates realistic performance metrics based on:
     * - Player position (defenders have more tackles, strikers more goals)
     * - Match context (home/away advantage)
     * - Performance variability
     * 
     * In production, these values would come from:
     * - Live GPS tracking data
     * - Video analysis system
     * - Coach observations
     * - Third-party sports data provider
     * 
     * @param player The player
     * @param match The match
     */
    private void recordPlayerStats(Joueur player, Match match) {
        // Check if stats already exist for this player-match combination
        List<PlayerStats> existing = playerStatsRepository.findByJoueurId(player.getId())
                .stream()
                .filter(s -> s.getMatch().getId().equals(match.getId()))
                .toList();

        if (!existing.isEmpty()) {
            System.out.println("  ⚠ Stats already exist for " + player.getNom() + " in this match");
            return;
        }

        // Generate realistic stats based on position
        int goals = generateGoalsForPosition(player.getPosition());
        int assists = random.nextInt(3); // 0-2 assists
        int minutesPlayed = random.nextBoolean() ? 90 : 45 + random.nextInt(46); // 45-90 mins
        double performanceRating = 5.0 + random.nextDouble() * 5.0; // 5.0-10.0
        
        // Defensive stats based on position
        int tackles = generateTacklesForPosition(player.getPosition());
        int interceptions = generateInterceptionsForPosition(player.getPosition());
        
        // Physical metrics
        int passesCompleted = 20 + random.nextInt(50); // 20-70 passes
        double distanceCovered = 8.0 + random.nextDouble() * 4.0; // 8-12 km
        double averageSpeed = 6.0 + random.nextDouble() * 2.0; // 6-8 km/h

        // Create and save stats
        PlayerStats stats = new PlayerStats();
        stats.setJoueur(player);
        stats.setMatch(match);
        stats.setGoals(goals);
        stats.setAssists(assists);
        stats.setMinutesPlayed(minutesPlayed);
        stats.setPerformanceRating(performanceRating);
        stats.setTackles(tackles);
        stats.setInterceptions(interceptions);
        stats.setPassesCompleted(passesCompleted);
        stats.setDistanceCovered(distanceCovered);
        stats.setAverageSpeed(averageSpeed);

        playerStatsRepository.save(stats);
        System.out.println(
            String.format("  ✓ Recorded stats for %s - Goals: %d, Rating: %.1f, Tackles: %d",
                player.getNom(), goals, performanceRating, tackles)
        );
    }

    /**
     * Generates realistic goal count based on player position.
     * 
     * @param position Player position
     * @return Number of goals (0-3)
     */
    private int generateGoalsForPosition(Position position) {
        return switch (position) {
            case FORWARD -> random.nextInt(3); // 0-2 goals
            case MIDFIELDER -> random.nextInt(2); // 0-1 goals
            case DEFENDER, GOALKEEPER -> random.nextInt(1); // 0 goals
            default -> 0;
        };
    }

    /**
     * Generates realistic tackle count based on player position.
     * 
     * @param position Player position
     * @return Number of tackles (0-8)
     */
    private int generateTacklesForPosition(Position position) {
        return switch (position) {
            case DEFENDER -> 3 + random.nextInt(6); // 3-8 tackles
            case MIDFIELDER -> 2 + random.nextInt(4); // 2-5 tackles
            case GOALKEEPER -> 1 + random.nextInt(3); // 1-3 tackles
            case FORWARD -> random.nextInt(2); // 0-1 tackles
            default -> random.nextInt(3);
        };
    }

    /**
     * Generates realistic interception count based on player position.
     * 
     * @param position Player position
     * @return Number of interceptions (0-5)
     */
    private int generateInterceptionsForPosition(Position position) {
        return switch (position) {
            case DEFENDER -> 1 + random.nextInt(4); // 1-4 interceptions
            case GOALKEEPER -> 1 + random.nextInt(5); // 1-5 interceptions
            case MIDFIELDER -> random.nextInt(3); // 0-2 interceptions
            case FORWARD -> random.nextInt(2); // 0-1 interceptions
            default -> random.nextInt(2);
        };
    }
}
