import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CommentDTO } from '../../shared/models/forum.model';

@Injectable({ providedIn: 'root' })
export class CommentService {
  private readonly base = 'http://localhost:18080/api/comments';

  constructor(private http: HttpClient) {}

  getAll(): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(this.base);
  }

  getByPost(postId: number): Observable<CommentDTO[]> {
    return this.http.get<CommentDTO[]>(`${this.base}/post/${postId}`);
  }

  create(comment: CommentDTO): Observable<CommentDTO> {
    return this.http.post<CommentDTO>(this.base, comment);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
