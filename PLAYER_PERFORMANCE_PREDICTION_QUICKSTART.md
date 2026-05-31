# Future Player Performance Prediction - Quick Start Guide

## 🚀 5-Minute Setup

### Prerequisites
- Python 3.8+ with pip
- Java 17+ with Maven
- Git (optional)

### Step 1: Train the Model (2 minutes)

```bash
# Navigate to AI service
cd PI_StreetLeague/ai-service

# Install/verify dependencies
pip install -r requirements.txt

# Train the model
python train_player_prediction_model.py
```

Expected: Model files created in `model/` folder ✓

### Step 2: Start Python AI Service (1 minute)

```bash
# From ai-service folder
python app.py
```

Expected output:
```
[AI Service] ✓ Modèle de prédiction de performance chargé avec succès
[AI Service] Démarrage sur http://localhost:5000
```

### Step 3: Start Java Backend (2 minutes)

```bash
# Navigate to backend
cd PI_StreetLeague/backend

# Run Spring Boot
./mvnw spring-boot:run
```

Expected: Backend running on `http://localhost:8080` ✓

---

## ✅ Verify Installation

### Test Endpoints

**1. Check AI Service Health**
```bash
curl http://localhost:5000/api/ai/health
```

Expected response:
```json
{
  "status": "ok",
  "player_prediction_model_loaded": true,
  "prediction_mode": "ml_model"
}
```

**2. Predict Player Performance**
```bash
curl http://localhost:8080/api/players/1/ai-prediction
```

Expected response:
```json
{
  "status": "ok",
  "prediction": {
    "playerId": 1,
    "predictedPerformanceRating": 78.5,
    "performanceCategory": "EXCELLENT"
  }
}
```

**3. Custom Prediction**
```bash
curl -X POST http://localhost:8080/api/players/ai-predict-custom \
  -H "Content-Type: application/json" \
  -d '{
    "playerId": 1,
    "goals": 3,
    "assists": 2,
    "tackles": 5,
    "interceptions": 2,
    "passesCompleted": 60,
    "passAccuracy": 85.5,
    "distanceCoveredKm": 10.5,
    "averageSpeedKmh": 26.2,
    "ballPossessionPercent": 55,
    "foulsCommitted": 1,
    "shotsOnTarget": 4
  }'
```

---

## 📚 Common Use Cases

### 1. Get Team's Next Match Prediction

```bash
# Predict performance for all players on team
curl -X POST http://localhost:8080/api/players/ai-predict-batch \
  -H "Content-Type: application/json" \
  -d '{
    "players": [
      {"playerId": 1, "goals": 2, "assists": 1, "tackles": 5, "interceptions": 2, "passesCompleted": 60, "passAccuracy": 85, "distanceCoveredKm": 10, "averageSpeedKmh": 25, "ballPossessionPercent": 55, "foulsCommitted": 1, "shotsOnTarget": 3},
      {"playerId": 2, "goals": 1, "assists": 0, "tackles": 3, "interceptions": 1, "passesCompleted": 45, "passAccuracy": 80, "distanceCoveredKm": 9.5, "averageSpeedKmh": 24, "ballPossessionPercent": 50, "foulsCommitted": 2, "shotsOnTarget": 1}
    ]
  }'
```

### 2. What-If Analysis (Injury Impact)

```bash
# Predict with reduced stats (simulating injury)
curl -X POST http://localhost:8080/api/players/ai-predict-custom \
  -H "Content-Type: application/json" \
  -d '{
    "playerId": 1,
    "goals": 1,          # Reduced from 3
    "assists": 0,        # Reduced from 2
    "tackles": 2,        # Reduced from 5
    "interceptions": 1,  # Reduced from 2
    "passesCompleted": 40,    # Reduced from 60
    "passAccuracy": 75,       # Reduced from 85
    "distanceCoveredKm": 7,   # Reduced from 10
    "averageSpeedKmh": 20,    # Reduced from 25
    "ballPossessionPercent": 40,
    "foulsCommitted": 2,
    "shotsOnTarget": 1
  }'
```

### 3. Performance Form Check

```bash
# Get both basic and AI predictions
curl http://localhost:8080/api/players/1/ai-prediction
```

This returns:
- Basic prediction (from historical averages)
- AI prediction (from ML model)
- Comparison between both

---

## 🔧 Configuration

### Python Service Port (Default: 5000)

Edit `ai-service/app.py` line:
```python
app.run(host="0.0.0.0", port=5000, debug=False)
```

### Java Backend Port (Default: 8080)

Edit `backend/src/main/resources/application.properties`:
```properties
server.port=8080
```

### AI Service URL in Backend

Edit `backend/src/main/java/com/streetLeague/backend/service/PlayerPerformanceAIService.java`:
```java
private static final String AI_SERVICE_BASE_URL = "http://localhost:5000/api/ai";
```

---

## 📊 Architecture Overview

```
┌─────────────────────────────┐
│   Frontend (Angular)        │
│   Dashboard / Analysis      │
└──────────────┬──────────────┘
               │
        REST API (Port 8080)
               │
┌──────────────▼──────────────┐
│   Spring Boot Backend        │
│   PlayerPerformance...       │
│   Controller & Service       │
└──────────────┬──────────────┘
               │
        HTTP (Port 5000)
               │
┌──────────────▼──────────────┐
│   Python Flask AI Service    │
│   - ML Models                │
│   - Feature Engineering      │
│   - Predictions              │
└─────────────────────────────┘
```

---

## 📁 Feature Files

| File | Purpose |
|------|---------|
| `train_player_prediction_model.py` | Train ML model |
| `app.py` | Flask app with AI endpoints |
| `PlayerPerformancePredictionController.java` | REST controller |
| `PlayerPerformanceAIService.java` | Python service integration |
| `PlayerPerformanceRequestDTO.java` | Request format |
| `PlayerPerformanceAIPredictionDTO.java` | Response format |

---

## 🐛 Troubleshooting

### Error: "Model not found"
```bash
# Make sure you ran the training script
python train_player_prediction_model.py

# Verify files exist
ls -la ai-service/model/
```

### Error: "Connection refused (localhost:5000)"
```bash
# Make sure Python service is running
python ai-service/app.py

# Check if port 5000 is available
lsof -i :5000
```

### Error: "No route found for /api/players"
```bash
# Make sure backend is running
./mvnw spring-boot:run

# Check if port 8080 is available
lsof -i :8080
```

### Predictions seem inaccurate
- Ensure player has at least 3 matches of history
- Check that statistics are realistic (0-100 scale for percentages)
- Retrain model with more/better data

---

## 📖 Next Steps

1. **Review Full Documentation:** See `PLAYER_PERFORMANCE_PREDICTION_README.md`
2. **Integrate with Frontend:** Use the provided Angular service example
3. **Customize ML Model:** Edit `train_player_prediction_model.py` for different algorithms
4. **Monitor Performance:** Track prediction accuracy with real data
5. **Production Deployment:** Use Docker for containerization

---

## 🎯 Key Endpoints Summary

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/players/{id}/prediction` | Basic prediction |
| GET | `/api/players/{id}/ai-prediction` | Advanced AI prediction |
| POST | `/api/players/ai-predict-custom` | Custom statistics prediction |
| POST | `/api/players/ai-predict-batch` | Multiple players prediction |
| GET | `/api/players/ai/health` | Service health check |

---

## 💡 Tips & Best Practices

✅ **DO:**
- Start with basic prediction first to verify setup
- Use batch endpoint for multiple players
- Check AI health before making predictions
- Cache predictions in frontend to reduce load

❌ **DON'T:**
- Call prediction endpoints for every page load
- Send predictions without proper error handling
- Ignore fallback mechanism (handle service unavailability)
- Use old data (update stats frequently)

---

## 📞 Support

**Setup Issues?** → Check troubleshooting section
**API Questions?** → See full documentation
**Model Questions?** → Check ML Model Details section

---

**Ready to predict! 🚀**

Next: Review the full [PLAYER_PERFORMANCE_PREDICTION_README.md](./PLAYER_PERFORMANCE_PREDICTION_README.md) for detailed documentation.
