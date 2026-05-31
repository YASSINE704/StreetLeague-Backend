import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, ProductDTO } from '../../shared/models/marketplace.model';

@Injectable({ providedIn: 'root' })
export class MarketplaceAiService {
  private readonly base = 'http://localhost:18080/api/market-ai';

  constructor(private http: HttpClient) {}

  smartSearch(query: string): Observable<any> {
    return this.http.get(`${this.base}/smart-search`, { params: { query } });
  }

  searchWithAI(query: string, page: number, size: number): Observable<Page<ProductDTO>> {
    return this.http.get<Page<ProductDTO>>(`${this.base}/search`, {
      params: { query, page, size }
    });
  }
}