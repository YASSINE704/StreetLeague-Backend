package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.PlayerStats;
import com.streetLeague.backend.entity.Joueur;
import com.streetLeague.backend.entity.Match;
import com.streetLeague.backend.repository.PlayerStatsRepository;
import com.streetLeague.backend.repository.JoueurRepository;
import com.streetLeague.backend.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Data seeding service for populating initial player statistics.
 * 
 * Runs on application startup to seed the database with sample player stats
 * including the new fields: tackles, interceptions, passes, distance, speed.
 * 
 * Only seeds if no stats exist yet to avoid duplicates.
 * 
 * @author StreetLeague Team
 * @version 1.0
 */
@Service
public class PlayerStatsSeedService implements CommandLineRunner {

    @Autowired
    private PlayerStatsRepository playerStatsRepository;

    @Autowired
    private JoueurRepository joueurRepository;

    @Autowired
    private MatchRepository matchRepository;

    /**
     * Seeds player statistics on application startup.
     * 
     * Only runs if the player_stats table is empty to prevent duplicate data.
     * 
     * @param args Command line arguments (unused)
     * @throws Exception if seeding fails
     */
    @Override
    public void run(String... args) throws Exception {
        // Only seed if no stats exist
        if (playerStatsRepository.count() > 0) {
            System.out.println("✓ Player stats already exist. Skipping seed.");
            return;
        }

        System.out.println("🌱 Seeding player statistics...");

        // Get all available players and matches
        List<Joueur> players = joueurRepository.findAll();
        List<Match> matches = matchRepository.findAll();

        if (players.isEmpty()) {
            System.out.println("⚠ No players found. Cannot seed stats.");
            return;
        }

        if (matches.isEmpty()) {
            System.out.println("⚠ No matches found in database.");
            System.out.println("📌 To seed player stats:");
            System.out.println("   1. Create 2-3 teams via API: POST /teams");
            System.out.println("   2. Create 2-3 terrains via API: POST /terrains");
            System.out.println("   3. Create matches via API: POST /matches");
            System.out.println("   4. Complete a match via API: PUT /matches/{id}/result?homeTeamScore=2&awayTeamScore=1");
            System.out.println("   5. Player stats will be auto-recorded when match is completed!");
            System.out.println("\n💡 Or manually complete a match via the UI dashboard.");
            return;
        }

        try {
            // Seed player 1 (FORWARD) - Striker
            if (players.size() > 0 && matches.size() > 0) {
                seedPlayerStats(players.get(0), matches.get(0), 2, 1, 90, 8.5, 3, 2, 45, 10.5, 6.8);
            }
            if (players.size() > 0 && matches.size() > 1) {
                seedPlayerStats(players.get(0), matches.get(1), 1, 2, 75, 7.2, 5, 1, 38, 9.2, 6.5);
            }
            if (players.size() > 0 && matches.size() > 2) {
                seedPlayerStats(players.get(0), matches.get(2), 0, 1, 60, 6.8, 4, 3, 35, 8.5, 6.3);
            }

            // Seed player 2 (MIDFIELDER) - Playmaker
            if (players.size() > 1 && matches.size() > 0) {
                seedPlayerStats(players.get(1), matches.get(0), 1, 0, 90, 7.0, 2, 1, 52, 10.8, 7.0);
            }
            if (players.size() > 1 && matches.size() > 1) {
                seedPlayerStats(players.get(1), matches.get(1), 3, 1, 85, 9.0, 1, 0, 48, 10.2, 6.9);
            }
            if (players.size() > 1 && matches.size() > 2) {
                seedPlayerStats(players.get(1), matches.get(2), 1, 3, 80, 8.7, 2, 2, 51, 10.5, 6.8);
            }

            // Seed player 3 (DEFENDER) - Defensive player
            if (players.size() > 2 && matches.size() > 0) {
                seedPlayerStats(players.get(2), matches.get(0), 0, 0, 45, 5.5, 6, 4, 28, 7.5, 5.8);
            }
            if (players.size() > 2 && matches.size() > 1) {
                seedPlayerStats(players.get(2), matches.get(1), 0, 1, 70, 6.9, 8, 5, 35, 8.8, 6.2);
            }
            if (players.size() > 2 && matches.size() > 2) {
                seedPlayerStats(players.get(2), matches.get(2), 1, 3, 90, 8.2, 2, 2, 41, 9.8, 6.6);
            }

            System.out.println("✅ Player statistics seeded successfully!");
        } catch (Exception e) {
            System.err.println("❌ Error seeding player statistics: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to create and save player stats.
     * 
     * @param joueur The player
     * @param match The match
     * @param goals Goals scored
     * @param assists Assists provided
     * @param minutesPlayed Minutes on field
     * @param performanceRating Performance rating (0-10)
     * @param tackles Tackles made
     * @param interceptions Interceptions
     * @param passesCompleted Passes completed
     * @param distanceCovered Distance covered in km
     * @param averageSpeed Average speed in km/h
     */
    private void seedPlayerStats(
            Joueur joueur, Match match,
            int goals, int assists, int minutesPlayed, double performanceRating,
            int tackles, int interceptions, int passesCompleted,
            double distanceCovered, double averageSpeed) {

        PlayerStats stats = new PlayerStats();
        stats.setJoueur(joueur);
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
            String.format("  ✓ Added stats for %s vs %s (Goals: %d, Rating: %.1f)",
                joueur.getNom(), match.getAwayTeam().getNom(), goals, performanceRating)
        );
    }
}
