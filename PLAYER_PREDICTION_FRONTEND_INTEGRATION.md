# Frontend Integration Guide - Player Performance Prediction

## Overview

This guide shows how to integrate the **Future Player Performance Prediction** feature into the Angular frontend.

---

## 1. Create the Prediction Service

Create file: `src/app/shared/services/player-prediction.service.ts`

```typescript
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PlayerPerformanceRequest {
  playerId: number;
  goals: number;
  assists: number;
  tackles: number;
  interceptions: number;
  passesCompleted: number;
  passAccuracy: number;
  distanceCoveredKm: number;
  averageSpeedKmh: number;
  ballPossessionPercent: number;
  foulsCommitted: number;
  shotsOnTarget: number;
}

export interface PlayerPerformanceAIPrediction {
  playerId: number;
  predictedPerformanceRating: number;
  performanceCategory: string;
  interpretation: string;
  strengths: string[];
  weaknesses: string[];
  confidence: string;
  algorithm: string;
}

export interface PredictionResponse {
  status: string;
  type: string;
  algorithm: string;
  confidence?: string;
  prediction: PlayerPerformanceAIPrediction;
  basicPrediction?: any;
}

@Injectable({ providedIn: 'root' })
export class PlayerPredictionService {
  private apiUrl = '/api/players';

  constructor(private http: HttpClient) { }

  /**
   * Get prediction based on player's historical data
   */
  getBasicPrediction(playerId: number): Observable<PredictionResponse> {
    return this.http.get<PredictionResponse>(
      `${this.apiUrl}/${playerId}/prediction`
    );
  }

  /**
   * Get advanced AI prediction from player's historical data
   */
  getAIPrediction(playerId: number): Observable<PredictionResponse> {
    return this.http.get<PredictionResponse>(
      `${this.apiUrl}/${playerId}/ai-prediction`
    );
  }

  /**
   * Predict with custom player statistics
   */
  predictWithCustomStats(stats: PlayerPerformanceRequest): Observable<PredictionResponse> {
    return this.http.post<PredictionResponse>(
      `${this.apiUrl}/ai-predict-custom`,
      stats
    );
  }

  /**
   * Batch prediction for multiple players
   */
  batchPredict(players: PlayerPerformanceRequest[]): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/ai-predict-batch`,
      { players }
    );
  }

  /**
   * Check AI service health
   */
  checkAIHealth(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ai/health`);
  }
}
```

---

## 2. Add Models

Update: `src/app/shared/models/sports.model.ts`

```typescript
// Add these interfaces to your existing file

export interface PlayerPerformanceAIPrediction {
  playerId: number;
  predictedPerformanceRating: number;
  performanceCategory: string;
  interpretation: string;
  strengths: string[];
  weaknesses: string[];
  confidence: string;
  algorithm: string;
}

export interface PlayerPredictionRequest {
  playerId: number;
  goals: number;
  assists: number;
  tackles: number;
  interceptions: number;
  passesCompleted: number;
  passAccuracy: number;
  distanceCoveredKm: number;
  averageSpeedKmh: number;
  ballPossessionPercent: number;
  foulsCommitted: number;
  shotsOnTarget: number;
}
```

---

## 3. Create Prediction Display Component

Create file: `src/app/features/player-prediction/player-prediction.component.ts`

```typescript
import { Component, OnInit, Input } from '@angular/core';
import { PlayerPredictionService, PlayerPerformanceAIPrediction } from '../../shared/services/player-prediction.service';
import { PlayerDTO } from '../../shared/models/sports.model';

@Component({
  selector: 'app-player-prediction',
  templateUrl: './player-prediction.component.html',
  styleUrls: ['./player-prediction.component.css']
})
export class PlayerPredictionComponent implements OnInit {

  @Input() player: PlayerDTO;

  prediction: PlayerPerformanceAIPrediction | null = null;
  loading = false;
  error: string | null = null;
  aiAvailable = false;

  constructor(private predictionService: PlayerPredictionService) { }

  ngOnInit() {
    this.checkAIAvailability();
    this.loadPrediction();
  }

  /**
   * Check if AI service is available
   */
  checkAIAvailability() {
    this.predictionService.checkAIHealth().subscribe({
      next: (response) => {
        this.aiAvailable = response.ai_service_available;
      },
      error: () => {
        this.aiAvailable = false;
      }
    });
  }

  /**
   * Load AI prediction for the player
   */
  loadPrediction() {
    if (!this.player?.id) return;

    this.loading = true;
    this.error = null;

    this.predictionService.getAIPrediction(this.player.id).subscribe({
      next: (response) => {
        if (response.status === 'ok') {
          this.prediction = response.prediction;
        }
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Impossible de charger la prédiction';
        this.loading = false;
        console.error('Prediction error:', err);
      }
    });
  }

  /**
   * Get color for performance category
   */
  getCategoryColor(category: string): string {
    const colors: { [key: string]: string } = {
      'VERY_BAD': '#d32f2f',
      'BAD': '#f57c00',
      'AVERAGE': '#fbc02d',
      'GOOD': '#7cb342',
      'EXCELLENT': '#388e3c',
      'LEGEND': '#6200ea'
    };
    return colors[category] || '#9e9e9e';
  }

  /**
   * Get icon for strength/weakness
   */
  getIcon(type: 'strength' | 'weakness'): string {
    return type === 'strength' ? '✓' : '✗';
  }

  /**
   * Reload prediction
   */
  refresh() {
    this.loadPrediction();
  }
}
```

---

## 4. Create Prediction Display Template

Create file: `src/app/features/player-prediction/player-prediction.component.html`

```html
<div class="prediction-container">
  <!-- Loading State -->
  <div *ngIf="loading" class="loading">
    <p>Chargement de la prédiction...</p>
  </div>

  <!-- Error State -->
  <div *ngIf="error" class="error-message">
    <p>{{ error }}</p>
    <button (click)="refresh()">Réessayer</button>
  </div>

  <!-- Prediction Available -->
  <div *ngIf="prediction && !loading" class="prediction-card">
    
    <!-- Header -->
    <div class="prediction-header">
      <h3>Prédiction de Performance</h3>
      <span class="ai-badge">🤖 IA</span>
    </div>

    <!-- Performance Rating -->
    <div class="performance-rating">
      <div class="rating-circle" [style.background-color]="getCategoryColor(prediction.performanceCategory)">
        <span class="rating-number">{{ prediction.predictedPerformanceRating | number:'1.1-1' }}</span>
        <span class="rating-max">/100</span>
      </div>
      <div class="rating-info">
        <p class="category" [style.color]="getCategoryColor(prediction.performanceCategory)">
          {{ prediction.performanceCategory | titlecase }}
        </p>
        <p class="interpretation">{{ prediction.interpretation }}</p>
      </div>
    </div>

    <!-- Confidence & Algorithm -->
    <div class="metadata">
      <div class="confidence">
        <span class="label">Confiance:</span>
        <span class="value" [ngClass]="'confidence-' + prediction.confidence.toLowerCase()">
          {{ prediction.confidence }}
        </span>
      </div>
      <div class="algorithm">
        <span class="label">Modèle:</span>
        <span class="value">{{ prediction.algorithm }}</span>
      </div>
    </div>

    <!-- Strengths -->
    <div *ngIf="prediction.strengths.length > 0" class="strengths-section">
      <h4>Points Forts</h4>
      <div class="strength-list">
        <div *ngFor="let strength of prediction.strengths" class="strength-item">
          <span class="icon">{{ getIcon('strength') }}</span>
          <span>{{ strength }}</span>
        </div>
      </div>
    </div>

    <!-- Weaknesses -->
    <div *ngIf="prediction.weaknesses.length > 0" class="weaknesses-section">
      <h4>Points à Améliorer</h4>
      <div class="weakness-list">
        <div *ngFor="let weakness of prediction.weaknesses" class="weakness-item">
          <span class="icon">{{ getIcon('weakness') }}</span>
          <span>{{ weakness }}</span>
        </div>
      </div>
    </div>

    <!-- Refresh Button -->
    <div class="actions">
      <button (click)="refresh()" class="refresh-btn">
        🔄 Rafraîchir
      </button>
    </div>

  </div>

  <!-- AI Unavailable -->
  <div *ngIf="!aiAvailable && !loading" class="ai-unavailable">
    <p>⚠️ Service IA indisponible</p>
    <small>Les prédictions ML ne sont pas disponibles pour le moment.</small>
  </div>

</div>
```

---

## 5. Add Styling

Create file: `src/app/features/player-prediction/player-prediction.component.css`

```css
.prediction-container {
  padding: 20px;
  max-width: 600px;
}

.loading {
  text-align: center;
  padding: 40px 20px;
  color: #666;
}

.error-message {
  background-color: #ffebee;
  border: 1px solid #ef5350;
  border-radius: 4px;
  padding: 16px;
  margin-bottom: 16px;
  color: #c62828;
}

.error-message button {
  margin-top: 8px;
  padding: 8px 16px;
  background-color: #ef5350;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.error-message button:hover {
  background-color: #d32f2f;
}

.prediction-card {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.prediction-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 12px;
}

.prediction-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.ai-badge {
  background-color: #6200ea;
  color: white;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.performance-rating {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

.rating-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  flex-shrink: 0;
}

.rating-number {
  font-size: 36px;
}

.rating-max {
  font-size: 14px;
  opacity: 0.8;
}

.rating-info {
  flex: 1;
}

.category {
  font-size: 18px;
  font-weight: bold;
  margin: 0 0 8px 0;
}

.interpretation {
  color: #666;
  margin: 0;
  font-size: 14px;
}

.metadata {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  padding: 12px;
  background-color: #f5f5f5;
  border-radius: 6px;
}

.confidence, .algorithm {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.label {
  font-size: 12px;
  font-weight: bold;
  color: #999;
  text-transform: uppercase;
}

.value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.confidence-high { color: #4caf50; }
.confidence-medium { color: #ff9800; }
.confidence-low { color: #f44336; }

.strengths-section, .weaknesses-section {
  margin-bottom: 16px;
}

.strengths-section h4, .weaknesses-section h4 {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: bold;
  color: #333;
  text-transform: uppercase;
}

.strength-list, .weakness-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.strength-item, .weakness-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background-color: #f9f9f9;
  border-radius: 4px;
  font-size: 14px;
}

.strength-item .icon {
  color: #4caf50;
  font-weight: bold;
}

.weakness-item .icon {
  color: #f44336;
  font-weight: bold;
}

.strength-item {
  border-left: 3px solid #4caf50;
}

.weakness-item {
  border-left: 3px solid #f44336;
}

.actions {
  margin-top: 20px;
  display: flex;
  gap: 8px;
}

.refresh-btn {
  padding: 10px 16px;
  background-color: #6200ea;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
}

.refresh-btn:hover {
  background-color: #5200d5;
}

.ai-unavailable {
  text-align: center;
  padding: 20px;
  background-color: #fff3cd;
  border: 1px solid #ffc107;
  border-radius: 4px;
  color: #856404;
}

.ai-unavailable small {
  display: block;
  margin-top: 8px;
  opacity: 0.8;
}

/* Responsive Design */
@media (max-width: 600px) {
  .performance-rating {
    flex-direction: column;
    text-align: center;
  }

  .rating-circle {
    width: 100px;
    height: 100px;
  }

  .rating-number {
    font-size: 28px;
  }

  .metadata {
    flex-direction: column;
  }
}
```

---

## 6. Use in Player Dashboard

Update: `src/app/features/player-dashboard/player-dashboard.component.html`

```html
<!-- Add to player dashboard template -->
<div class="player-dashboard">
  
  <!-- Existing player info -->
  <div class="player-info">
    <!-- ... existing code ... -->
  </div>

  <!-- Performance Prediction Section -->
  <div class="prediction-section">
    <app-player-prediction [player]="selectedPlayer"></app-player-prediction>
  </div>

  <!-- Other sections -->
  
</div>
```

---

## 7. Use in Team Analysis

Example: `src/app/features/team-analysis/team-analysis.component.ts`

```typescript
export class TeamAnalysisComponent implements OnInit {

  team: TeamDTO;
  playerPredictions: any[] = [];
  loading = false;

  constructor(
    private playerPredictionService: PlayerPredictionService
  ) { }

  /**
   * Get predictions for entire team
   */
  loadTeamPredictions() {
    this.loading = true;

    // Prepare batch request
    const players = this.team.joueurs.map(joueur => ({
      playerId: joueur.id,
      goals: joueur.stats?.goals || 0,
      assists: joueur.stats?.assists || 0,
      tackles: joueur.stats?.tackles || 0,
      interceptions: joueur.stats?.interceptions || 0,
      passesCompleted: joueur.stats?.passesCompleted || 50,
      passAccuracy: joueur.stats?.passAccuracy || 75,
      distanceCoveredKm: joueur.stats?.distanceCoveredKm || 10,
      averageSpeedKmh: joueur.stats?.averageSpeedKmh || 25,
      ballPossessionPercent: joueur.stats?.ballPossessionPercent || 50,
      foulsCommitted: joueur.stats?.foulsCommitted || 0,
      shotsOnTarget: joueur.stats?.shotsOnTarget || 1
    }));

    this.playerPredictionService.batchPredict(players).subscribe({
      next: (response) => {
        this.playerPredictions = response.predictions;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  /**
   * Get team's average predicted rating
   */
  getTeamAverageRating(): number {
    if (!this.playerPredictions.length) return 0;
    const sum = this.playerPredictions.reduce(
      (acc, p) => acc + p.predictedPerformanceRating, 0
    );
    return sum / this.playerPredictions.length;
  }

  /**
   * Get top performers
   */
  getTopPerformers(count: number = 3) {
    return this.playerPredictions
      .sort((a, b) => b.predictedPerformanceRating - a.predictedPerformanceRating)
      .slice(0, count);
  }

}
```

---

## 8. Use in What-If Scenarios

Example: `src/app/features/performance-planner/performance-planner.component.ts`

```typescript
export class PerformancePlannerComponent {

  player: PlayerDTO;
  prediction: PlayerPerformanceAIPrediction | null = null;
  stats = {
    goals: 2,
    assists: 1,
    tackles: 5,
    interceptions: 2,
    passesCompleted: 60,
    passAccuracy: 85,
    distanceCoveredKm: 10,
    averageSpeedKmh: 25,
    ballPossessionPercent: 55,
    foulsCommitted: 1,
    shotsOnTarget": 3
  };

  constructor(private predictionService: PlayerPredictionService) { }

  /**
   * Simulate improved performance
   */
  simulateImprovement() {
    this.stats.goals += 1;
    this.stats.assists += 1;
    this.stats.passes Completed += 5;
    this.stats.passAccuracy += 2;
    this.updatePrediction();
  }

  /**
   * Simulate injury impact
   */
  simulateInjury() {
    this.stats.goals -= 1;
    this.stats.assists -= 1;
    this.stats.tacklesks -= 2;
    this.stats.distanceCoveredKm -= 2;
    this.updatePrediction();
  }

  /**
   * Update prediction with current stats
   */
  updatePrediction() {
    const request = {
      playerId: this.player.id,
      ...this.stats
    };

    this.predictionService.predictWithCustomStats(request).subscribe({
      next: (response) => {
        this.prediction = response.prediction;
      }
    });
  }

}
```

---

## 9. Error Handling Best Practices

```typescript
// Robust error handling
private handlePredictionError(error: any) {
  if (error.status === 404) {
    this.error = 'Joueur non trouvé';
  } else if (error.status === 503) {
    this.error = 'Service IA indisponible';
    // Fall back to basic prediction
    this.loadBasicPrediction();
  } else if (error.status === 0) {
    this.error = 'Erreur de connexion';
  } else {
    this.error = 'Erreur lors du chargement de la prédiction';
  }
}

private loadBasicPrediction() {
  this.predictionService.getBasicPrediction(this.player.id).subscribe({
    next: (response) => {
      this.prediction = response.prediction;
    },
    error: () => {
      this.error = 'Impossible de charger même la prédiction basique';
    }
  });
}
```

---

## 10. Caching Strategy

```typescript
export class PlayerPredictionCacheService {

  private cache = new Map<number, any>();
  private cacheExpiry = 5 * 60 * 1000; // 5 minutes

  getPrediction(playerId: number, predictionFn: () => Observable<any>): Observable<any> {
    const cached = this.cache.get(playerId);
    
    if (cached && Date.now() - cached.timestamp < this.cacheExpiry) {
      return of(cached.data);
    }

    return predictionFn().pipe(
      tap(data => {
        this.cache.set(playerId, {
          data,
          timestamp: Date.now()
        });
      })
    );
  }

  clearCache() {
    this.cache.clear();
  }
}
```

---

## Summary

| Component | File | Purpose |
|-----------|------|---------|
| Service | `player-prediction.service.ts` | API calls |
| Models | `sports.model.ts` | Interfaces |
| Display | `player-prediction.component.*` | UI component |
| Styling | `player-prediction.component.css` | Styling |

---

## Testing

```typescript
describe('PlayerPredictionService', () => {
  let service: PlayerPredictionService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PlayerPredictionService],
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(PlayerPredictionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should fetch AI prediction', () => {
    const mockResponse = {
      status: 'ok',
      prediction: { playerId: 1, predictedPerformanceRating: 78.5 }
    };

    service.getAIPrediction(1).subscribe(data => {
      expect(data.prediction.playerId).toBe(1);
    });

    const req = httpMock.expectOne('/api/players/1/ai-prediction');
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });
});
```

---

**Integration Complete! 🎉**

Your Angular frontend is now ready to use the Player Performance Prediction feature.
