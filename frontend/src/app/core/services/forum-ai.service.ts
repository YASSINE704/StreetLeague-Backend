import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ForumAiService {
  private readonly base = 'http://localhost:18080/api/forum-ai';

  constructor(private http: HttpClient) {}

  getSmartReply(postId: number): Observable<string> {
    return this.http.get(`${this.base}/smart-reply/${postId}`, { responseType: 'text' });
  }
}