package com.streetLeague.backend.config;

import com.streetLeague.backend.entity.*;
import com.streetLeague.backend.enums.MatchStatus;
import com.streetLeague.backend.enums.Niveau;
import com.streetLeague.backend.enums.Position;
import com.streetLeague.backend.enums.TypeSport;
import com.streetLeague.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DemoDataLoader {

    private final JoueurRepository joueurRepository;
    private final EquipeRepository equipeRepository;
    private final TerrainRepository terrainRepository;
    private final MatchRepository matchRepository;
    private final PlayerStatsRepository playerStatsRepository;

    @Bean
    CommandLineRunner seedDemoData() {
        return args -> {
            // Skip if matches already exist (prevents duplicate seeding)
            if (matchRepository.count() > 0) {
                return;
            }

            System.out.println("🏟️  Seeding demo data with players, teams, matches, and player stats...");

            // 1. Create Terrain
            Terrain terrain = Terrain.builder()
                    .nom("Central Field - Downtown")
                    .typeSport(TypeSport.FOOTBALL)
                    .build();
            terrain = terrainRepository.save(terrain);
            System.out.println("✅ Created terrain: " + terrain.getNom());

            // 2. Create Demo Players
            List<Joueur> allPlayers = createDemoPlayers();
            System.out.println("✅ Created " + allPlayers.size() + " demo players");

            // 3. Create Teams and assign players
            List<Equipe> teams = createTeamsWithPlayers(allPlayers);
            System.out.println("✅ Created " + teams.size() + " demo teams");

            // 4. Create Completed Matches with historic dates
            List<Match> matches = createDemoMatches(teams, terrain);
            System.out.println("✅ Created " + matches.size() + " completed matches");

            // 5. Create Player Stats for each match
            createPlayerStatsForMatches(matches, allPlayers);
            System.out.println("✅ Created player stats for all matches");

            System.out.println("\n✨ Demo data seeding completed successfully!");
            System.out.println("📊 Database now contains historical match data for performance prediction testing.\n");
        };
    }

    private List<Joueur> createDemoPlayers() {
        List<Joueur> players = new ArrayList<>();
        String[] firstNames = {"Ahmed", "Mohamed", "Ali", "Hassan", "Omar", "Karim", "Nabil", 
                               "Youssef", "Ibrahim", "Samir", "Khaled", "Amira", "Fatima", "Layla", "Zainab"};
        String[] lastNames = {"Ben Ali", "Trabelsi", "Saidane", "Malkawi", "El Amrani", "Bourguiba", 
                              "Mestiri", "Ragheb", "Azzabi", "El Hadj"};

        Position[] positions = {Position.GOALKEEPER, Position.DEFENDER, Position.MIDFIELDER, 
                                Position.FORWARD, Position.UTILITY};
        Niveau[] niveaux = {Niveau.BEGINNER, Niveau.INTERMEDIATE, Niveau.ADVANCED};

        Random random = new Random();
        int id = 1;

        for (String firstName : firstNames) {
            for (String lastName : lastNames) {
                if (id > 30) break; // Create 30 players

                Joueur joueur = Joueur.builder()
                        .nom(firstName + " " + lastName)
                        .age(random.nextInt(35) + 18) // Age 18-52
                        .niveau(niveaux[random.nextInt(niveaux.length)])
                        .position(positions[random.nextInt(positions.length)])
                        .build();

                players.add(joueurRepository.save(joueur));
                id++;
            }
            if (id > 30) break;
        }

        return players;
    }

    private List<Equipe> createTeamsWithPlayers(List<Joueur> allPlayers) {
        List<Equipe> teams = new ArrayList<>();
        String[] teamNames = {"Phoenix United", "Dragons FC", "Titans Warriors", "Eagles Squad", 
                              "Panthers FC", "Lions Force", "Thunder Strikers"};

        int playersPerTeam = 11;
        int playerIndex = 0;

        for (String teamName : teamNames) {
            Equipe team = Equipe.builder()
                    .nom(teamName)
                    .typeSport(TypeSport.FOOTBALL)
                    .joueurs(new ArrayList<>())
                    .build();

            team = equipeRepository.save(team);

            // Assign 11 players to each team
            for (int i = 0; i < playersPerTeam && playerIndex < allPlayers.size(); i++) {
                Joueur player = allPlayers.get(playerIndex++);
                player.setEquipe(team);
                team.getJoueurs().add(player);
                joueurRepository.save(player);
            }

            teams.add(team);
        }

        return teams;
    }

    private List<Match> createDemoMatches(List<Equipe> teams, Terrain terrain) {
        List<Match> matches = new ArrayList<>();
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();

        // Create 20 completed matches with past dates
        for (int i = 0; i < 20 && teams.size() >= 2; i++) {
            int homeTeamIdx = random.nextInt(teams.size());
            int awayTeamIdx = random.nextInt(teams.size());

            // Ensure different teams
            while (awayTeamIdx == homeTeamIdx) {
                awayTeamIdx = random.nextInt(teams.size());
            }

            Equipe homeTeam = teams.get(homeTeamIdx);
            Equipe awayTeam = teams.get(awayTeamIdx);

            // Create match with past date (1 to 60 days ago)
            LocalDateTime matchDate = now.minusDays(random.nextInt(60) + 1);

            Match match = Match.builder()
                    .homeTeam(homeTeam)
                    .awayTeam(awayTeam)
                    .terrain(terrain)
                    .matchDate(matchDate)
                    .status(MatchStatus.COMPLETED)
                    .homeTeamScore(random.nextInt(6)) // 0-5 goals
                    .awayTeamScore(random.nextInt(6)) // 0-5 goals
                    .build();

            matches.add(matchRepository.save(match));
        }

        return matches;
    }

    private void createPlayerStatsForMatches(List<Match> matches, List<Joueur> allPlayers) {
        Random random = new Random();

        for (Match match : matches) {
            // Get players from both teams
            List<Joueur> homeTeamPlayers = new ArrayList<>(match.getHomeTeam().getJoueurs());
            List<Joueur> awayTeamPlayers = new ArrayList<>(match.getAwayTeam().getJoueurs());

            // Create stats for home team players
            for (Joueur player : homeTeamPlayers) {
                createPlayerStatForMatch(player, match, random);
            }

            // Create stats for away team players
            for (Joueur player : awayTeamPlayers) {
                createPlayerStatForMatch(player, match, random);
            }
        }
    }

    private void createPlayerStatForMatch(Joueur joueur, Match match, Random random) {
        PlayerStats stats = PlayerStats.builder()
                .joueur(joueur)
                .match(match)
                .goals(random.nextInt(4)) // 0-3 goals
                .assists(random.nextInt(3)) // 0-2 assists
                .minutesPlayed(45 + random.nextInt(45)) // 45-90 minutes
                .performanceRating(4.0 + random.nextDouble() * 6.0) // 4.0-10.0 rating
                .tackles(random.nextInt(8)) // 0-7 tackles
                .interceptions(random.nextInt(5)) // 0-4 interceptions
                .build();

        playerStatsRepository.save(stats);
    }
}
