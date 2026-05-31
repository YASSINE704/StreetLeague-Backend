import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { ProductDTO } from '../../../shared/models/marketplace.model';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {
  product: ProductDTO | null = null;
  errorMessage = '';
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadProduct(id);
    }
  }

  loadProduct(id: number): void {
    this.isLoading = true;
    this.productService.getById(id).subscribe({
      next: (data) => {
        this.product = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Produit introuvable.';
        this.isLoading = false;
        setTimeout(() => this.router.navigate(['/marketplace/products']), 2000);
      }
    });
  }

  getImageUrl(): string {
    if (!this.product?.imageUrl) return '';
    return `http://localhost:18080/${this.product.imageUrl}`;
  }

  getSizeInfo(): string {
    if (!this.product) return '';
    if (this.product.pointure) return `Pointure ${this.product.pointure}`;
    if (this.product.taille) return `Taille ${this.product.taille}`;
    return '';
  }

  onEdit(): void {
    this.router.navigate(['/marketplace/products', this.product!.id, 'edit']);
  }

  onDelete(): void {
    if (confirm('Supprimer ce produit ?')) {
      this.productService.delete(this.product!.id!).subscribe({
        next: () => this.router.navigate(['/marketplace/products']),
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression.'
      });
    }
  }

  onBack(): void {
    this.router.navigate(['/marketplace/products']);
  }

  getUserName(): string {
    if (!this.product?.user) return 'Vendeur';
    return [this.product.user.prenom, this.product.user.nom].filter(Boolean).join(' ') || 'Vendeur';
  }

  getCategoryColor(category: string): string {
    const colors: Record<string, string> = {
      football: '#16a34a',
      basketball: '#dc2626',
      volleyball: '#2563eb',
      padel: '#7c3aed',
      tennis: '#d97706',
      running: '#0891b2',
      fitness: '#db2777'
    };
    return colors[category.toLowerCase()] || '#64748b';
  }
}
