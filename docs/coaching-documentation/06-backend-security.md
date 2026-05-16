# 06 - Backend Security (Sécurité)

## 🎯 Qu'est-ce que Spring Security ?

**Spring Security** = un framework qui protège votre application. Il gère :
- **Qui** peut accéder (authentification = "qui êtes-vous ?")
- **Quoi** ils peuvent faire (autorisation = "avez-vous le droit ?")

### En mots simples :
Spring Security est comme un **vigile** à l'entrée d'un bâtiment :
1. Il vérifie votre badge (authentification)
2. Il vérifie si vous avez accès à l'étage demandé (autorisation)

---

## 🔐 SecurityConfig

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/config/SecurityConfig.java`

```java
@Configuration               // "Je suis une classe de configuration Spring"
@EnableWebSecurity           // "Active Spring Security"
@EnableMethodSecurity        // "Permet @PreAuthorize sur les méthodes"
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
```

### La chaîne de filtres (SecurityFilterChain) :

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        // 1. CORS : autorise les requêtes cross-origin
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))

        // 2. CSRF désactivé (normal pour une API REST stateless)
        .csrf(AbstractHttpConfigurer::disable)

        // 3. Headers : désactive frameOptions (pour H2 console)
        .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))

        // 4. Session : STATELESS (pas de session côté serveur)
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        // 5. Gestion des erreurs d'authentification
        .exceptionHandling(ex ->
            ex.authenticationEntryPoint(restAuthenticationEntryPoint))

        // 6. RÈGLES D'AUTORISATION
        .authorizeHttpRequests(auth -> auth
            // Routes publiques (pas besoin d'être connecté)
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/h2-console/**").permitAll()

            // Routes coaching : COACH ou ADMIN uniquement
            .requestMatchers("/api/programmes/**").hasAnyRole("COACH", "ADMIN")
            .requestMatchers("/api/seances/**").hasAnyRole("COACH", "ADMIN")
            .requestMatchers("/api/exercices/**").hasAnyRole("COACH", "ADMIN")
            .requestMatchers("/api/seance-exercices/**").hasAnyRole("COACH", "ADMIN")
            .requestMatchers("/api/suivis/**").hasAnyRole("COACH", "ADMIN")
            .requestMatchers("/api/affectations/**").hasAnyRole("COACH", "ADMIN")
            .requestMatchers("/api/coaching/**").hasAnyRole("COACH", "ADMIN")

            // Autres modules
            .requestMatchers("/terrains/**").hasAnyRole("TERRAIN_MANAGER", "ADMIN")
            .requestMatchers("/players/**").hasAnyRole("JOUEUR", "SPORTIF", "COACH", "ADMIN")
            .requestMatchers("/teams/**").hasAnyRole("JOUEUR", "SPORTIF", "COACH", "ADMIN")
            .requestMatchers("/matches/**").hasAnyRole("JOUEUR", "SPORTIF", "COACH", "TERRAIN_MANAGER", "ADMIN")

            // Tout le reste : authentifié
            .anyRequest().authenticated()
        )

        // 7. Ajoute le filtre JWT AVANT le filtre standard
        .addFilterBefore(jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

### Explication visuelle du flux :

```
Requête HTTP
    │
    ▼
┌─────────────────────┐
│  CORS Filter        │  ← Vérifie l'origine (localhost:4200 OK)
└─────────┬───────────┘
          ▼
┌─────────────────────┐
│  JWT Filter         │  ← Vérifie le token JWT dans le header
└─────────┬───────────┘
          ▼
┌─────────────────────┐
│  Authorization      │  ← Vérifie le rôle (COACH? ADMIN?)
└─────────┬───────────┘
          ▼
┌─────────────────────┐
│  Controller         │  ← Traite la requête
└─────────────────────┘
```

---

## 🌐 CORS (Cross-Origin Resource Sharing)

### Le problème :
Par défaut, un navigateur **bloque** les requêtes d'un site vers un autre domaine.
- Angular tourne sur `http://localhost:4200`
- Spring Boot tourne sur `http://localhost:8080`
- Ce sont deux "origines" différentes → le navigateur bloque !

### La solution : CORS
On dit au serveur "accepte les requêtes venant de localhost:4200".

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Quelles origines sont autorisées
    configuration.setAllowedOrigins(List.of("http://localhost:4200"));

    // Quels verbes HTTP sont autorisés
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    // Quels headers sont autorisés
    configuration.setAllowedHeaders(List.of("*"));  // Tous les headers

    // Autorise l'envoi de cookies/credentials
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);  // Pour toutes les URLs
    return source;
}
```

### En mots simples :
CORS = "Je fais confiance à localhost:4200, il peut m'envoyer des requêtes."

---

## 🔑 Comment fonctionne l'authentification JWT

### Qu'est-ce qu'un JWT ?
**JWT** (JSON Web Token) = un "badge numérique" que le serveur donne après connexion.

```
┌─────────────────────────────────────────────────────┐
│  eyJhbGciOiJIUzI1NiJ9.                             │  ← Header (algorithme)
│  eyJzdWIiOiJjb2FjaEBleGFtcGxlLmNvbSIsInJvbGUiOi.  │  ← Payload (données)
│  SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c      │  ← Signature (preuve)
└─────────────────────────────────────────────────────┘
```

### Le flux d'authentification :

```
1. Login : POST /api/auth/login { email, password }
2. Serveur vérifie → renvoie un JWT
3. Frontend stocke le JWT
4. Chaque requête suivante inclut : Authorization: Bearer <JWT>
5. Le JwtAuthenticationFilter vérifie le token à chaque requête
```

---

## 👤 Vérification des rôles avec CoachingRoleService

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/CoachingRoleService.java`

### Comment ça marche :

```java
@Service
@RequiredArgsConstructor
public class CoachingRoleService {

    private final UserRepository userRepository;

    // Méthode 1 : Vérifie COACH ou ADMIN
    public void requireCoachOrAdmin(Integer userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.COACH && user.getRole() != Role.ADMIN) {
            throw new BusinessRuleException("Accès refusé : seuls les COACH et ADMIN...");
        }
    }

    // Méthode 2 : Vérifie SPORTIF, COACH ou ADMIN
    public void requireSportifOrCoachOrAdmin(Integer userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.SPORTIF
                && user.getRole() != Role.COACH
                && user.getRole() != Role.ADMIN) {
            throw new BusinessRuleException("Accès refusé...");
        }
    }
}
```

### Utilisation dans le controller IA :
```java
@PostMapping("/recommend")
public ResponseEntity<Map<String, Object>> recommend(
        @RequestHeader(value = "X-User-Id", required = false) Integer userId,
        @RequestBody Map<String, Object> context) {

    if (userId != null) {
        coachingRoleService.requireCoachOrAdmin(userId);
        // Si l'utilisateur n'est pas COACH/ADMIN → exception → erreur 403
    }
    // ...
}
```

### Le header X-User-Id :
Le frontend Angular envoie l'ID de l'utilisateur connecté dans un header HTTP personnalisé :
```
X-User-Id: 1
```
Le backend utilise cet ID pour vérifier le rôle dans la base de données.

---

## 🔒 BCryptPasswordEncoder

### Qu'est-ce que le hachage ?
**Hacher** un mot de passe = le transformer en une chaîne illisible et irréversible.

```
"monMotDePasse123" → "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
```

### Pourquoi ?
Si un hacker vole la base de données, il ne peut PAS retrouver les mots de passe originaux.

### BCrypt :
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```
- **BCrypt** = algorithme de hachage lent (exprès, pour ralentir les attaques)
- Chaque hash est différent même pour le même mot de passe (grâce au "salt")
- Irréversible : impossible de retrouver le mot de passe original

---

## 📊 Tableau des rôles et accès

| Rôle | Programmes | Séances | Exercices | IA | Suivis |
|------|-----------|---------|-----------|-----|--------|
| ADMIN | ✅ | ✅ | ✅ | ✅ | ✅ |
| COACH | ✅ | ✅ | ✅ | ✅ | ✅ |
| SPORTIF | ❌ | ❌ | ❌ | ❌ | ❌ |
| JOUEUR | ❌ | ❌ | ❌ | ❌ | ❌ |

> Note : Dans la configuration actuelle, seuls COACH et ADMIN ont accès au module coaching via Spring Security.

---

## 📚 Termes clés

| Terme | Définition |
|-------|-----------|
| **Authentification** | Vérifier l'identité ("Qui êtes-vous ?") |
| **Autorisation** | Vérifier les droits ("Pouvez-vous faire ça ?") |
| **JWT** | Token signé contenant les infos utilisateur |
| **CORS** | Mécanisme pour autoriser les requêtes cross-origin |
| **CSRF** | Attaque par falsification de requête (désactivé car API stateless) |
| **Stateless** | Le serveur ne garde pas de session en mémoire |
| **BCrypt** | Algorithme de hachage de mots de passe |
| **Salt** | Valeur aléatoire ajoutée avant le hachage |

---

## 🎓 Questions du professeur

**Q : Pourquoi désactiver CSRF ?**
> R : CSRF est une protection pour les applications avec sessions (cookies). Notre API est stateless (utilise JWT), donc CSRF n'est pas nécessaire. Les tokens JWT dans les headers ne sont pas vulnérables aux attaques CSRF.

**Q : Pourquoi STATELESS ?**
> R : Chaque requête contient toutes les informations nécessaires (le JWT). Le serveur n'a pas besoin de stocker de session. C'est plus scalable (on peut avoir plusieurs serveurs) et plus simple.

**Q : Que se passe-t-il si le JWT expire ?**
> R : Le JwtAuthenticationFilter rejette la requête avec une erreur 401 (Unauthorized). Le frontend doit alors rediriger vers la page de login.

**Q : Pourquoi permitAll() pour /api/auth/** ?**
> R : Les routes d'authentification (login, register) doivent être accessibles SANS être connecté. Sinon, personne ne pourrait se connecter !

**Q : Qu'est-ce que hasAnyRole("COACH", "ADMIN") ?**
> R : L'utilisateur doit avoir le rôle COACH OU le rôle ADMIN pour accéder à cette URL. Si son rôle est SPORTIF, il reçoit une erreur 403 (Forbidden).

**Q : Pourquoi le filtre JWT est ajouté AVANT UsernamePasswordAuthenticationFilter ?**
> R : Pour que le JWT soit vérifié en premier. Si le token est valide, l'utilisateur est authentifié sans avoir besoin de fournir email/mot de passe à chaque requête.
