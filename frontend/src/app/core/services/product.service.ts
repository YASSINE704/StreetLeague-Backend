import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDTO, ProductSearchRequest, Page } from '../../shared/models/marketplace.model';

@Injectable({ providedIn: 'root' })
export class ProductService {
  private readonly base = 'http://localhost:18080/api/products';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ProductDTO[]> {
    return this.http.get<ProductDTO[]>(this.base);
  }

  search(request: ProductSearchRequest): Observable<Page<ProductDTO>> {
    const params: any = {};
    if (request.query) params.query = request.query;
    if (request.category) params.category = request.category;
    if (request.subcategory) params.subcategory = request.subcategory;
    if (request.minPrice !== undefined) params.minPrice = request.minPrice;
    if (request.maxPrice !== undefined) params.maxPrice = request.maxPrice;
    if (request.pointure !== undefined) params.pointure = request.pointure;
    if (request.taille) params.taille = request.taille;
    if (request.sortBy) params.sortBy = request.sortBy;
    if (request.sortDir) params.sortDir = request.sortDir;
    if (request.page !== undefined) params.page = request.page;
    if (request.size !== undefined) params.size = request.size;
    return this.http.get<Page<ProductDTO>>(`${this.base}/search`, { params });
  }

  getById(id: number): Observable<ProductDTO> {
    return this.http.get<ProductDTO>(`${this.base}/${id}`);
  }

  create(product: ProductDTO): Observable<ProductDTO> {
    return this.http.post<ProductDTO>(this.base, product);
  }

  update(id: number, product: ProductDTO): Observable<ProductDTO> {
    return this.http.put<ProductDTO>(`${this.base}/${id}`, product);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  uploadImage(id: number, file: File): Observable<ProductDTO> {
    const fd = new FormData();
    fd.append('file', file);
    return this.http.post<ProductDTO>(`${this.base}/${id}/image`, fd);
  }
}
