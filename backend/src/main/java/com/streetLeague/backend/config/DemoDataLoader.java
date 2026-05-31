package com.streetLeague.backend.config;

import com.streetLeague.backend.entity.*;
import com.streetLeague.backend.enums.*;
import com.streetLeague.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final EndroitRepository endroitRepository;
    private final SousEspaceRepository sousEspaceRepository;
    private final ExerciceRepository exerciceRepository;
    private final ProgrammeEntrainementRepository programmeRepository;
    private final SeanceEntrainementRepository seanceRepository;

    @Bean
    CommandLineRunner seedDemoData() {
        return args -> {
            // Seed endroits if not already present
            if (endroitRepository.count() == 0) {
                seedEndroitsAndSousEspaces();
            }

            // Seed exercices if not already present
            if (exerciceRepository.count() == 0) {
                seedExercices();
            }

            // Skip match data if already exists
            if (matchRepository.count() > 0) {
                return;
            }

            System.out.println("🏟️  Seeding demo data...");

            // 1. Create Terrain
            Terrain terrain = Terrain.builder()
                    .nom("Central Field - Downtown")
                    .location("Downtown Tunis")
                    .address("Avenue Habib Bourguiba, Tunis 1000")
                    .typeSport(TypeSport.FOOTBALL)
                    .location("Downtown")
                    .address("123 Main Street, Downtown")
                    .build();
            terrain = terrainRepository.save(terrain);

            // 2. Create Demo Players
            List<Joueur> allPlayers = createDemoPlayers();

            // 3. Create Teams and assign players
            List<Equipe> teams = createTeamsWithPlayers(allPlayers);

            // 4. Create Completed Matches
            List<Match> matches = createDemoMatches(teams, terrain);

            // 5. Create Player Stats
            createPlayerStatsForMatches(matches, allPlayers);

            System.out.println("✨ Demo data seeding completed!");
        };
    }

    private void seedEndroitsAndSousEspaces() {
        System.out.println("📍 Seeding endroits et sous-espaces...");

        // Endroit 1 : Complexe Sportif Tunis (plein air)
        Endroit e1 = new Endroit();
        e1.setNom("Complexe Sportif El Menzah");
        e1.setType(TypeEndroit.STADE);
        e1.setAdresse("Rue du Stade, El Menzah");
        e1.setVille("Tunis");
        e1.setLatitude(36.8232);
        e1.setLongitude(10.1655);
        e1.setCapacite(200);
        e1.setStatut(StatutEndroit.DISPONIBLE);
        e1.setDescription("Complexe sportif avec terrains extérieurs et piste d'athlétisme");
        e1 = endroitRepository.save(e1);

        SousEspace se1 = new SousEspace();
        se1.setNom("Terrain Football A (extérieur)");
        se1.setType(TypeSousEspace.TERRAIN);
        se1.setCapacite(30);
        se1.setStatut(StatutEndroit.DISPONIBLE);
        se1.setEndroit(e1);
        sousEspaceRepository.save(se1);

        SousEspace se2 = new SousEspace();
        se2.setNom("Piste Athlétisme (extérieur)");
        se2.setType(TypeSousEspace.TERRAIN);
        se2.setCapacite(50);
        se2.setStatut(StatutEndroit.DISPONIBLE);
        se2.setEndroit(e1);
        sousEspaceRepository.save(se2);

        // Endroit 2 : Salle de Sport Indoor
        Endroit e2 = new Endroit();
        e2.setNom("Salle Omnisports Ariana");
        e2.setType(TypeEndroit.SALLE_SPORT);
        e2.setAdresse("Avenue de la République, Ariana");
        e2.setVille("Ariana");
        e2.setLatitude(36.8625);
        e2.setLongitude(10.1956);
        e2.setCapacite(100);
        e2.setStatut(StatutEndroit.DISPONIBLE);
        e2.setDescription("Salle couverte avec équipements de musculation et salle de cours");
        e2 = endroitRepository.save(e2);

        SousEspace se3 = new SousEspace();
        se3.setNom("Salle Musculation");
        se3.setType(TypeSousEspace.SALLE);
        se3.setCapacite(20);
        se3.setStatut(StatutEndroit.DISPONIBLE);
        se3.setEndroit(e2);
        sousEspaceRepository.save(se3);

        SousEspace se4 = new SousEspace();
        se4.setNom("Salle Cours Collectifs");
        se4.setType(TypeSousEspace.SALLE);
        se4.setCapacite(15);
        se4.setStatut(StatutEndroit.DISPONIBLE);
        se4.setEndroit(e2);
        sousEspaceRepository.save(se4);

        // Endroit 3 : Terrain Sousse (plein air)
        Endroit e3 = new Endroit();
        e3.setNom("Terrain Municipal Sousse");
        e3.setType(TypeEndroit.TERRAIN);
        e3.setAdresse("Boulevard 14 Janvier, Sousse");
        e3.setVille("Sousse");
        e3.setLatitude(35.8256);
        e3.setLongitude(10.6369);
        e3.setCapacite(60);
        e3.setStatut(StatutEndroit.DISPONIBLE);
        e3.setDescription("Terrain de football en gazon synthétique, éclairé la nuit");
        e3 = endroitRepository.save(e3);

        SousEspace se5 = new SousEspace();
        se5.setNom("Terrain Gazon Synthétique (extérieur)");
        se5.setType(TypeSousEspace.TERRAIN);
        se5.setCapacite(22);
        se5.setStatut(StatutEndroit.DISPONIBLE);
        se5.setEndroit(e3);
        sousEspaceRepository.save(se5);

        // Endroit 4 : Centre Sportif Sfax
        Endroit e4 = new Endroit();
        e4.setNom("Centre Sportif Sfax");
        e4.setType(TypeEndroit.SALLE_SPORT);
        e4.setAdresse("Route de Tunis km 3, Sfax");
        e4.setVille("Sfax");
        e4.setLatitude(34.7406);
        e4.setLongitude(10.7603);
        e4.setCapacite(80);
        e4.setStatut(StatutEndroit.DISPONIBLE);
        e4.setDescription("Centre sportif moderne avec salle indoor et terrain extérieur");
        e4 = endroitRepository.save(e4);

        SousEspace se6 = new SousEspace();
        se6.setNom("Salle Indoor Polyvalente");
        se6.setType(TypeSousEspace.SALLE);
        se6.setCapacite(25);
        se6.setStatut(StatutEndroit.DISPONIBLE);
        se6.setEndroit(e4);
        sousEspaceRepository.save(se6);

        SousEspace se7 = new SousEspace();
        se7.setNom("Terrain Extérieur Sfax");
        se7.setType(TypeSousEspace.TERRAIN);
        se7.setCapacite(30);
        se7.setStatut(StatutEndroit.DISPONIBLE);
        se7.setEndroit(e4);
        sousEspaceRepository.save(se7);

        System.out.println("✅ 4 endroits + 7 sous-espaces créés (Tunis, Ariana, Sousse, Sfax)");
    }

    private void seedExercices() {
        System.out.println("💪 Seeding exercices...");

        exerciceRepository.save(Exercice.builder().nom("Pompes explosives").description("Pompes avec phase explosive").type(TypeExercice.FORCE).build());
        exerciceRepository.save(Exercice.builder().nom("Squats sautés").description("Squats avec saut pour les jambes").type(TypeExercice.FORCE).build());
        exerciceRepository.save(Exercice.builder().nom("Gainage dynamique").description("Gainage avec mouvements").type(TypeExercice.FORCE).build());
        exerciceRepository.save(Exercice.builder().nom("Sprint intervalles").description("Alternance sprint/repos 30s").type(TypeExercice.CARDIO).build());
        exerciceRepository.save(Exercice.builder().nom("Burpees").description("Exercice complet cardio").type(TypeExercice.CARDIO).build());
        exerciceRepository.save(Exercice.builder().nom("Corde à sauter").description("Cardio et coordination").type(TypeExercice.CARDIO).build());
        exerciceRepository.save(Exercice.builder().nom("Étirements dynamiques").description("Mobilité articulaire").type(TypeExercice.MOBILITE).build());
        exerciceRepository.save(Exercice.builder().nom("Yoga sportif").description("Récupération et souplesse").type(TypeExercice.MOBILITE).build());
        exerciceRepository.save(Exercice.builder().nom("Dribble slalom").description("Contrôle de balle entre plots").type(TypeExercice.TECHNIQUE).build());
        exerciceRepository.save(Exercice.builder().nom("Passes courtes").description("Passes en mouvement").type(TypeExercice.TECHNIQUE).build());
        exerciceRepository.save(Exercice.builder().nom("Tirs cadrés").description("Précision de tir au but").type(TypeExercice.TECHNIQUE).build());
        exerciceRepository.save(Exercice.builder().nom("Jeu réduit 3v3").description("Match en effectif réduit").type(TypeExercice.TECHNIQUE).build());

        System.out.println("✅ 12 exercices créés");
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
