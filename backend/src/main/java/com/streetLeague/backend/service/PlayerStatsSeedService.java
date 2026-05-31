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
            // Enhanced seed data with more matches for better trend analysis and predictions
            
            // Seed player 1 (FORWARD) - Striker - 8 matches for trend calculation
            if (players.size() > 0 && matches.size() > 0) {
                int matchIndex = 0;
                // Progressive performance improvement (IMPROVING trend)
                seedPlayerStats(players.get(0), matches.get(matchIndex % matches.size()), 1, 0, 60, 6.2, 2, 1, 35, 8.5, 6.3);
                seedPlayerStats(players.get(0), matches.get((matchIndex + 1) % matches.size()), 1, 1, 70, 6.5, 2, 1, 40, 9.0, 6.4);
                seedPlayerStats(players.get(0), matches.get((matchIndex + 2) % matches.size()), 2, 1, 80, 7.2, 3, 2, 45, 9.5, 6.6);
                seedPlayerStats(players.get(0), matches.get((matchIndex + 3) % matches.size()), 2, 2, 90, 8.0, 3, 1, 48, 10.2, 6.8);
                seedPlayerStats(players.get(0), matches.get((matchIndex + 4) % matches.size()), 3, 2, 90, 8.5, 3, 2, 50, 10.5, 6.9);
                seedPlayerStats(players.get(0), matches.get((matchIndex + 5) % matches.size()), 2, 1, 85, 8.3, 2, 1, 45, 10.0, 6.8);
                seedPlayerStats(players.get(0), matches.get((matchIndex + 6) % matches.size()), 3, 1, 90, 8.7, 4, 2, 52, 11.0, 7.0);
                seedPlayerStats(players.get(0), matches.get((matchIndex + 7) % matches.size()), 2, 2, 88, 8.4, 3, 2, 48, 10.3, 6.9);
            }

            // Seed player 2 (MIDFIELDER) - Playmaker - 8 matches
            if (players.size() > 1 && matches.size() > 1) {
                int matchIndex = 1;
                // Stable high performance
                seedPlayerStats(players.get(1), matches.get(matchIndex % matches.size()), 1, 3, 85, 8.2, 1, 1, 55, 10.8, 7.0);
                seedPlayerStats(players.get(1), matches.get((matchIndex + 1) % matches.size()), 1, 2, 88, 8.1, 2, 0, 58, 11.0, 7.1);
                seedPlayerStats(players.get(1), matches.get((matchIndex + 2) % matches.size()), 2, 3, 90, 8.5, 1, 1, 60, 11.2, 7.2);
                seedPlayerStats(players.get(1), matches.get((matchIndex + 3) % matches.size()), 1, 4, 90, 8.8, 2, 1, 62, 11.5, 7.3);
                seedPlayerStats(players.get(1), matches.get((matchIndex + 4) % matches.size()), 2, 3, 87, 8.6, 1, 0, 59, 11.0, 7.2);
                seedPlayerStats(players.get(1), matches.get((matchIndex + 5) % matches.size()), 1, 3, 88, 8.4, 2, 1, 57, 10.8, 7.1);
                seedPlayerStats(players.get(1), matches.get((matchIndex + 6) % matches.size()), 2, 4, 90, 8.9, 1, 0, 63, 11.3, 7.3);
                seedPlayerStats(players.get(1), matches.get((matchIndex + 7) % matches.size()), 1, 2, 85, 8.3, 2, 1, 54, 10.5, 7.0);
            }

            // Seed player 3 (DEFENDER) - Defensive player - 8 matches
            if (players.size() > 2 && matches.size() > 2) {
                int matchIndex = 2;
                // Declining performance (needs improvement)
                seedPlayerStats(players.get(2), matches.get(matchIndex % matches.size()), 0, 0, 90, 8.0, 9, 5, 35, 9.8, 6.8);
                seedPlayerStats(players.get(2), matches.get((matchIndex + 1) % matches.size()), 0, 0, 88, 7.8, 8, 4, 33, 9.5, 6.7);
                seedPlayerStats(players.get(2), matches.get((matchIndex + 2) % matches.size()), 0, 1, 90, 7.5, 7, 3, 32, 9.2, 6.6);
                seedPlayerStats(players.get(2), matches.get((matchIndex + 3) % matches.size()), 1, 0, 85, 7.2, 6, 3, 30, 8.8, 6.4);
                seedPlayerStats(players.get(2), matches.get((matchIndex + 4) % matches.size()), 0, 1, 80, 6.9, 5, 2, 28, 8.5, 6.3);
                seedPlayerStats(players.get(2), matches.get((matchIndex + 5) % matches.size()), 0, 0, 75, 6.6, 4, 2, 26, 8.0, 6.1);
                seedPlayerStats(players.get(2), matches.get((matchIndex + 6) % matches.size()), 1, 1, 70, 6.8, 5, 3, 29, 8.3, 6.2);
                seedPlayerStats(players.get(2), matches.get((matchIndex + 7) % matches.size()), 0, 0, 60, 6.4, 3, 1, 24, 7.8, 6.0);
            }

            // Additional players if they exist - for better variety in test data
            if (players.size() > 3 && matches.size() > 3) {
                int matchIndex = 3;
                seedPlayerStats(players.get(3), matches.get(matchIndex % matches.size()), 1, 1, 80, 7.5, 4, 2, 42, 9.8, 6.7);
                seedPlayerStats(players.get(3), matches.get((matchIndex + 1) % matches.size()), 2, 0, 90, 7.8, 3, 1, 45, 10.0, 6.9);
                seedPlayerStats(players.get(3), matches.get((matchIndex + 2) % matches.size()), 1, 2, 85, 7.9, 4, 2, 46, 10.2, 7.0);
                seedPlayerStats(players.get(3), matches.get((matchIndex + 3) % matches.size()), 2, 1, 88, 8.2, 3, 1, 48, 10.5, 7.1);
                seedPlayerStats(players.get(3), matches.get((matchIndex + 4) % matches.size()), 1, 3, 82, 8.0, 4, 3, 50, 10.8, 7.2);
                seedPlayerStats(players.get(3), matches.get((matchIndex + 5) % matches.size()), 3, 1, 90, 8.5, 2, 0, 52, 11.0, 7.3);
            }

            System.out.println("✅ Player statistics seeded successfully with comprehensive test data!");
            System.out.println("📊 AI predictions now have enough data for trend analysis!");
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
