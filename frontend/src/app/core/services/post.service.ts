import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PostDTO } from '../../shared/models/forum.model';

@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly base = 'http://localhost:18080/api/posts';

  constructor(private http: HttpClient) {}

  getAll(): Observable<PostDTO[]> {
    return this.http.get<PostDTO[]>(this.base);
  }

  getById(id: number): Observable<PostDTO> {
    return this.http.get<PostDTO>(`${this.base}/${id}`);
  }

  create(post: PostDTO): Observable<PostDTO> {
    return this.http.post<PostDTO>(this.base, post);
  }

  update(id: number, post: PostDTO): Observable<PostDTO> {
    return this.http.put<PostDTO>(`${this.base}/${id}`, post);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  getByTag(tag: string): Observable<PostDTO[]> {
    return this.http.get<PostDTO[]>(`${this.base}/tag/${tag}`);
  }

  getTrending(): Observable<PostDTO[]> {
    return this.http.get<PostDTO[]>(`${this.base}/trending`);
  }
}
