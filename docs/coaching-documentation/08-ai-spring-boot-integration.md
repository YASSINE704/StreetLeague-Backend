# 08 - Intégration IA : Spring Boot ↔ Python

## 🎯 Vue d'ensemble

Ce document explique comment **Spring Boot appelle le service Python** et comment le flux complet fonctionne de bout en bout.

---

## 🔄 Le flux complet

```
┌──────────┐    HTTP POST     ┌──────────────┐    HTTP POST     ┌──────────┐
│  Angular │ ──────────────→  │  Spring Boot │ ──────────────→  │  Flask   │
│  (4200)  │                  │   (8080)     │                  │  (5000)  │
│          │  ← JSON ──────── │              │  ← JSON ──────── │          │
└──────────┘                  └──────────────┘                  └──────────┘
```

### Étapes détaillées :

```
1. Le coach clique "✨ Proposer avec IA" dans Angular
2. Angular envoie POST /api/coaching/ai/recommend à Spring Boot (port 8080)
3. Spring Boot vérifie le rôle (COACH/ADMIN) via X-User-Id
4. Spring Boot appelle POST /api/ai/recommend sur Flask (port 5000)
5. Flask calcule les scores et renvoie 6 recommandations
6. Spring Boot reçoit la réponse et la transmet à Angular
7. Angular affiche les recommandations dans le panneau IA
8. Le coach clique "Valider et ajouter" sur un exercice
9. Angular crée l'exercice dans le catalogue (POST /api/exercices)
10. Angular lie l'exercice à la séance (POST /api/seance-exercices)
11. La séance est rechargée avec le nouvel exercice
```

---

## 🔧 RestTemplate : le client HTTP de Spring

### Qu'est-ce que RestTemplate ?
**RestTemplate** = un outil Spring pour faire des requêtes HTTP depuis Java (comme un navigateur, mais en code).

### Configuration :
📍 `backend/src/main/java/com/streetLeague/backend/config/RestTemplateConfig.java`

```java
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### En mots simples :
RestTemplate est comme un **facteur** : Spring Boot lui donne une lettre (requête) et une adresse (URL), et il va la livrer au service Python puis rapporte la réponse.

---

## 📡 AIRecommendationService (le pont vers Python)

### 📍 Fichier : `backend/src/main/java/com/streetLeague/backend/service/AIRecommendationService.java`

```java
@Service
@RequiredArgsConstructor
@Slf4j
public class AIRecommendationService {

    private static final String AI_SERVICE_BASE_URL = "http://localhost:5000/api/ai";
    private final RestTemplate restTemplate;

    public Map<String, Object> getRecommendations(Map<String, Object> context) {
        try {
            // Envoie une requête POST au service Python
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    AI_SERVICE_BASE_URL + "/recommend",  // URL du service Python
                    context,                              // Corps de la requête (JSON)
                    Map.class                             // Type de réponse attendu
            );

            // Si la réponse est OK (code 2xx)
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();  // Retourne les recommandations
            }

            // Si la réponse est invalide → fallback
            return buildFallbackResponse("Réponse invalide du service AI");

        } catch (Exception e) {
            // Si le service Python est en panne → fallback
            log.warn("Service AI indisponible : {}", e.getMessage());
            return buildFallbackResponse("Service AI temporairement indisponible");
        }
    }
}
```

### Explication ligne par ligne :

| Ligne | Ce qu'elle fait |
|-------|----------------|
| `restTemplate.postForEntity(...)` | Envoie un POST HTTP au service Python |
| `AI_SERVICE_BASE_URL + "/recommend"` | L'URL complète : http://localhost:5000/api/ai/recommend |
| `context` | Le JSON envoyé (objectif, type, intensité...) |
| `Map.class` | On attend une Map (objet JSON) en retour |
| `response.getStatusCode().is2xxSuccessful()` | Vérifie que le code est 200-299 |
| `catch (Exception e)` | Attrape TOUTE erreur (réseau, timeout, etc.) |

---

## 🛡️ Le mécanisme de Fallback

### Qu'est-ce qu'un fallback ?
Un **fallback** = un plan B quand le plan A échoue.

### Quand le fallback se déclenche :
1. Le service Python n'est pas démarré
2. Le service Python est en erreur (crash)
3. Le réseau est coupé
4. Le service Python met trop de temps à répondre (timeout)

### La réponse fallback :
```java
private Map<String, Object> buildFallbackResponse(String message) {
    Map<String, Object> fallback = new HashMap<>();
    fallback.put("status", "fallback");           // Indique que c'est un fallback
    fallback.put("message", message);             // Explication pour l'utilisateur
    fallback.put("nbRecommandations", 0);         // Pas de recommandations
    fallback.put("recommandations", Collections.emptyList());  // Liste vide
    return fallback;
}
```

### Ce que l'utilisateur voit :
```
ℹ️ Service IA indisponible. Vous pouvez continuer avec l'ajout manuel.
```

### En mots simples :
Si l'IA est en panne, l'application ne plante PAS. Elle dit simplement "l'IA n'est pas disponible, utilisez l'ajout manuel".

---

## 🏥 Health Check (vérification de santé)

```java
public boolean isAvailable() {
    try {
        ResponseEntity<Map> response = restTemplate.getForEntity(
                AI_SERVICE_BASE_URL + "/health",  // GET /api/ai/health
                Map.class
        );
        return response.getStatusCode().is2xxSuccessful();
    } catch (Exception e) {
        log.warn("Health check AI échoué : {}", e.getMessage());
        return false;
    }
}
```

### Utilisé par le controller :
```java
@GetMapping("/health")
public ResponseEntity<Map<String, Object>> health() {
    boolean available = aiRecommendationService.isAvailable();
    // Renvoie { "available": true/false, "message": "..." }
}
```

Le frontend peut appeler `/api/coaching/ai/health` pour savoir si l'IA est disponible AVANT de tenter une recommandation.

---

## 📊 Diagramme de séquence complet

```
Coach (Angular)          Spring Boot (8080)         Flask (5000)
     │                         │                         │
     │  POST /api/coaching/    │                         │
     │  ai/recommend           │                         │
     │  + X-User-Id: 1        │                         │
     │  + body: {context}     │                         │
     │ ───────────────────────→│                         │
     │                         │                         │
     │                         │  Vérifie rôle COACH     │
     │                         │  (CoachingRoleService)  │
     │                         │                         │
     │                         │  POST /api/ai/recommend │
     │                         │  + body: {context}      │
     │                         │ ───────────────────────→│
     │                         │                         │
     │                         │                         │ Calcule scores
     │                         │                         │ Trie exercices
     │                         │                         │ Prend top 6
     │                         │                         │
     │                         │  200 OK                 │
     │                         │  {recommandations: [...]}│
     │                         │ ←───────────────────────│
     │                         │                         │
     │  200 OK                 │                         │
     │  {recommandations: [...]}                         │
     │ ←───────────────────────│                         │
     │                         │                         │
     │  Affiche les 6 cartes   │                         │
     │  de recommandation      │                         │
     │                         │                         │
     │  Coach clique "Valider" │                         │
     │                         │                         │
     │  POST /api/exercices    │                         │
     │  (crée l'exercice)      │                         │
     │ ───────────────────────→│                         │
     │ ←─── 201 Created ──────│                         │
     │                         │                         │
     │  POST /api/seance-      │                         │
     │  exercices              │                         │
     │  (lie à la séance)      │                         │
     │ ───────────────────────→│                         │
     │ ←─── 201 Created ──────│                         │
     │                         │                         │
     │  GET /api/seances/{id}  │                         │
     │  (recharge la séance)   │                         │
     │ ───────────────────────→│                         │
     │ ←─── 200 OK ───────────│                         │
```

---

## ⚠️ Gestion des erreurs

### Scénario 1 : Service Python éteint
```
Angular → Spring Boot → [ERREUR: Connection refused] → Fallback
Angular ← { status: "fallback", message: "Service AI indisponible" }
```

### Scénario 2 : Service Python renvoie une erreur
```
Angular → Spring Boot → Flask → [500 Internal Server Error]
Spring Boot catch l'exception → Fallback
Angular ← { status: "fallback", message: "Réponse invalide" }
```

### Scénario 3 : Utilisateur non autorisé
```
Angular → Spring Boot → CoachingRoleService → BusinessRuleException
Angular ← 403 Forbidden { message: "Accès refusé" }
```

### Scénario 4 : Tout fonctionne ✅
```
Angular → Spring Boot → Flask → 200 OK avec recommandations
Angular ← 200 OK { status: "ok", recommandations: [...] }
```

---

## 🎓 Questions du professeur

**Q : Pourquoi Spring Boot appelle Python et pas Angular directement ?**
> R : Pour la sécurité et la séparation des responsabilités. Spring Boot vérifie les droits (rôle COACH) avant d'appeler l'IA. Si Angular appelait Python directement, n'importe qui pourrait utiliser l'IA sans authentification.

**Q : Qu'est-ce que RestTemplate ?**
> R : C'est un client HTTP fourni par Spring. Il permet à une application Java de faire des requêtes HTTP (GET, POST, PUT, DELETE) vers d'autres services. C'est l'équivalent de HttpClient en Angular.

**Q : Pourquoi utiliser un fallback plutôt que renvoyer une erreur 500 ?**
> R : Pour la résilience. L'utilisateur peut continuer à travailler (ajout manuel d'exercices) même si l'IA est en panne. Une erreur 500 bloquerait l'interface et donnerait une mauvaise expérience.

**Q : Comment Spring Boot sait que le service Python est sur le port 5000 ?**
> R : L'URL est codée en dur dans AIRecommendationService : `http://localhost:5000/api/ai`. En production, on utiliserait un fichier de configuration (application.properties) ou des variables d'environnement.

**Q : Que se passe-t-il si le service Python met 30 secondes à répondre ?**
> R : Par défaut, RestTemplate a un timeout. Si le délai est dépassé, une exception est lancée et le fallback est activé. L'utilisateur voit "Service AI indisponible".

**Q : Pourquoi Map<String, Object> et pas un DTO typé pour la réponse IA ?**
> R : Pour la flexibilité. Le service Python peut évoluer (ajouter des champs) sans qu'on doive modifier le code Java. Map accepte n'importe quelle structure JSON.
