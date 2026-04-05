# Tests API - StreetLeague Backend

## Démarrage

```bash
cd backend
./mvnw spring-boot:run
```

L'application démarre sur: `http://localhost:8080`

## 1. Tests ENDROIT

### Créer un endroit (POST)
```
POST http://localhost:8080/api/endroits
Content-Type: application/json

{
  "nom": "Stade Municipal",
  "type": "STADE",
  "adresse": "123 Rue du Sport",
  "ville": "Tunis",
  "latitude": 36.8065,
  "longitude": 10.1815,
  "capacite": 5000,
  "statut": "DISPONIBLE",
  "description": "Grand stade municipal avec plusieurs terrains"
}
```

### Lister tous les endroits (GET)
```
GET http://localhost:8080/api/endroits
```

### Obtenir un endroit par ID (GET)
```
GET http://localhost:8080/api/endroits/1
```

### Filtrer par type (GET)
```
GET http://localhost:8080/api/endroits/type/STADE
GET http://localhost:8080/api/endroits/type/SALLE_SPORT
GET http://localhost:8080/api/endroits/type/TERRAIN
```

### Filtrer par statut (GET)
```
GET http://localhost:8080/api/endroits/statut/DISPONIBLE
GET http://localhost:8080/api/endroits/statut/INDISPONIBLE
GET http://localhost:8080/api/endroits/statut/MAINTENANCE
```

### Filtrer par ville (GET)
```
GET http://localhost:8080/api/endroits/ville/Tunis
```

### Modifier un endroit (PUT)
```
PUT http://localhost:8080/api/endroits/1
Content-Type: application/json

{
  "nom": "Stade Municipal Rénové",
  "type": "STADE",
  "adresse": "123 Rue du Sport",
  "ville": "Tunis",
  "latitude": 36.8065,
  "longitude": 10.1815,
  "capacite": 6000,
  "statut": "DISPONIBLE",
  "description": "Stade rénové avec nouvelles installations"
}
```

### Supprimer un endroit (DELETE)
```
DELETE http://localhost:8080/api/endroits/1
```

---

## 2. Tests SOUS-ESPACE

### Créer un sous-espace (POST)
```
POST http://localhost:8080/api/sous-espaces/endroit/1
Content-Type: application/json

{
  "nom": "Terrain de Football A",
  "type": "TERRAIN",
  "capacite": 22,
  "statut": "DISPONIBLE"
}
```

### Lister tous les sous-espaces (GET)
```
GET http://localhost:8080/api/sous-espaces
```

### Obtenir un sous-espace par ID (GET)
```
GET http://localhost:8080/api/sous-espaces/1
```

### Lister les sous-espaces d'un endroit (GET)
```
GET http://localhost:8080/api/sous-espaces/endroit/1
```

### Filtrer par type (GET)
```
GET http://localhost:8080/api/sous-espaces/type/TERRAIN
GET http://localhost:8080/api/sous-espaces/type/SALLE
GET http://localhost:8080/api/sous-espaces/type/COURT
GET http://localhost:8080/api/sous-espaces/type/ZONE
```

### Filtrer par statut (GET)
```
GET http://localhost:8080/api/sous-espaces/statut/DISPONIBLE
```

### Modifier un sous-espace (PUT)
```
PUT http://localhost:8080/api/sous-espaces/1
Content-Type: application/json

{
  "nom": "Terrain de Football A - Premium",
  "type": "TERRAIN",
  "capacite": 22,
  "statut": "DISPONIBLE"
}
```

### Supprimer un sous-espace (DELETE)
```
DELETE http://localhost:8080/api/sous-espaces/1
```

---

## 3. Tests EQUIPEMENT

### Créer un équipement (POST)
```
POST http://localhost:8080/api/equipements/sous-espace/1
Content-Type: application/json

{
  "nom": "Ballon de Football",
  "quantite": 10
}
```

### Lister tous les équipements (GET)
```
GET http://localhost:8080/api/equipements
```

### Obtenir un équipement par ID (GET)
```
GET http://localhost:8080/api/equipements/1
```

### Lister les équipements d'un sous-espace (GET)
```
GET http://localhost:8080/api/equipements/sous-espace/1
```

### Modifier un équipement (PUT)
```
PUT http://localhost:8080/api/equipements/1
Content-Type: application/json

{
  "nom": "Ballon de Football",
  "quantite": 15
}
```

### Supprimer un équipement (DELETE)
```
DELETE http://localhost:8080/api/equipements/1
```

---

## 4. Tests RESERVATION

### Créer une réservation (POST)
```
POST http://localhost:8080/api/reservations/sous-espace/1
Content-Type: application/json

{
  "dateDebut": "2026-03-15T10:00:00",
  "dateFin": "2026-03-15T12:00:00"
}
```

### Lister toutes les réservations (GET)
```
GET http://localhost:8080/api/reservations
```

### Obtenir une réservation par ID (GET)
```
GET http://localhost:8080/api/reservations/1
```

### Lister les réservations d'un sous-espace (GET)
```
GET http://localhost:8080/api/reservations/sous-espace/1
```

### Filtrer par statut (GET)
```
GET http://localhost:8080/api/reservations/statut/EN_ATTENTE
GET http://localhost:8080/api/reservations/statut/CONFIRMEE
GET http://localhost:8080/api/reservations/statut/ANNULEE
```

### Confirmer une réservation (PATCH)
```
PATCH http://localhost:8080/api/reservations/1/confirmer
```

### Annuler une réservation (PATCH)
```
PATCH http://localhost:8080/api/reservations/1/annuler?motif=Client indisponible
```

### Modifier une réservation (PUT)
```
PUT http://localhost:8080/api/reservations/1
Content-Type: application/json

{
  "dateDebut": "2026-03-15T14:00:00",
  "dateFin": "2026-03-15T16:00:00",
  "statut": "CONFIRMEE"
}
```

### Supprimer une réservation (DELETE)
```
DELETE http://localhost:8080/api/reservations/1
```

---

## Scénario de test complet

1. Créer un endroit (Stade)
2. Créer 2 sous-espaces pour cet endroit (Terrain A, Terrain B)
3. Ajouter des équipements aux sous-espaces
4. Créer une réservation pour le Terrain A
5. Confirmer la réservation
6. Créer une autre réservation
7. Annuler cette réservation

## Outils recommandés

- **Postman**: https://www.postman.com/downloads/
- **Insomnia**: https://insomnia.rest/download
- **Thunder Client** (Extension VS Code)
- **cURL** (ligne de commande)

## Console H2 Database

Accéder à: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:streetleague`
- Username: `sa`
- Password: (laisser vide)
