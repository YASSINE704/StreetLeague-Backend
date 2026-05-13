import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Interface for Player Performance Prediction
 */
export interface PlayerPrediction {
  playerId: number;
  predictedPerformanceRating: number;
  predictedGoals: number;
  predictedAssists: number;
  predictedTackles: number;
  predictedInterceptions: number;
  averageRating: number;
  averageGoals: number;
  averageAssists: number;
  totalMatchesAnalyzed: number;
  performanceCategory: string;
  reliability: number;
  trendDirection: string;
}

/**
 * Service for retrieving player performance predictions.
 * 
 * Uses linear regression analysis of historical player statistics to predict:
 * - Next match performance rating (0-100)
 * - Specific metrics (goals, assists, tackles, etc.)
 * - Performance category and trend direction
 * - Reliability confidence of the prediction
 */
@Injectable({
  providedIn: 'root'
})
export class PlayerPredictionService {
  private readonly base = 'http://localhost:18080/players';

  constructor(private http: HttpClient) {}

  /**
   * Get predicted performance for a player's next match
   * 
   * @param playerId The player ID
   * @returns Observable of PlayerPrediction
   */
  getPrediction(playerId: number): Observable<PlayerPrediction> {
    return this.http.get<PlayerPrediction>(`${this.base}/${playerId}/prediction`);
  }

  /**
   * Map performance category enum to display label
   * 
   * @param category The performance category
   * @returns Human-readable label
   */
  getCategoryLabel(category: string): string {
    const categoryMap: { [key: string]: string } = {
      'VERY_BAD': 'Very Bad',
      'BAD': 'Bad',
      'AVERAGE': 'Average',
      'GOOD': 'Good',
      'EXCELLENT': 'Excellent',
      'LEGEND': 'Legend / Pro',
      'NO_DATA': 'Insufficient Data'
    };
    return categoryMap[category] || category;
  }

  /**
   * Get color for performance category
   * 
   * @param category The performance category
   * @returns Color value for UI display
   */
  getCategoryColor(category: string): string {
    const colorMap: { [key: string]: string } = {
      'VERY_BAD': '#d32f2f',
      'BAD': '#f57c00',
      'AVERAGE': '#fbc02d',
      'GOOD': '#388e3c',
      'EXCELLENT': '#1976d2',
      'LEGEND': '#7b1fa2',
      'NO_DATA': '#9e9e9e'
    };
    return colorMap[category] || '#9e9e9e';
  }

  /**
   * Get trend icon/symbol
   * 
   * @param trend The trend direction
   * @returns Trend symbol
   */
  getTrendSymbol(trend: string): string {
    const trendMap: { [key: string]: string } = {
      'IMPROVING': '📈',
      'STABLE': '📊',
      'DECLINING': '📉',
      'INSUFFICIENT_DATA': '❓',
      'UNKNOWN': '❓'
    };
    return trendMap[trend] || '❓';
  }

  /**
   * Get trend description
   * 
   * @param trend The trend direction
   * @returns Trend description
   */
  getTrendDescription(trend: string): string {
    const trendMap: { [key: string]: string } = {
      'IMPROVING': 'Improving',
      'STABLE': 'Stable',
      'DECLINING': 'Declining',
      'INSUFFICIENT_DATA': 'Not enough data',
      'UNKNOWN': 'Unknown'
    };
    return trendMap[trend] || 'Unknown';
  }
}
