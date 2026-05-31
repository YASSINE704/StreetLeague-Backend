import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { MarketplaceAiService } from '../../../core/services/marketplace-ai.service';
import { ProductDTO, ProductSearchRequest, Page, PRODUCT_CATEGORIES } from '../../../shared/models/marketplace.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit, OnDestroy {
  pageResult: Page<ProductDTO> | null = null;
  errorMessage = '';
  successMessage = '';

  searchTerm = '';
  filterCategory = '';
  maxPrice: number | null = null;

  aiSearchQuery = '';
  isSearchingAi = false;

  sortField = 'name';
  sortDir: 'asc' | 'desc' = 'asc';

  page = 0;
  pageSize = 6;

  categoriesList = PRODUCT_CATEGORIES;

  private searchTimeout: any;

  get averagePrice(): number {
    if (!this.pageResult || this.pageResult.content.length === 0) return 0;
    return this.pageResult.content.reduce((sum, p) => sum + p.price, 0) / this.pageResult.content.length;
  }

  get categories(): string[] {
    if (!this.pageResult) return [];
    const cats = new Set(this.pageResult.content.map(p => p.category));
    return Array.from(cats).sort();
  }

  constructor(
    private productService: ProductService,
    private marketplaceAiService: MarketplaceAiService,
    private router: Router
  ) {}

  ngOnInit(): void { this.searchProducts(); }

  ngOnDestroy(): void {
    clearTimeout(this.searchTimeout);
  }

  onFilterChange(): void {
    clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => this.searchProducts(true), 300);
  }

  searchProducts(resetPage = true): void {
    this.errorMessage = '';
    if (resetPage) this.page = 0;

    const params: ProductSearchRequest = {
      query: this.searchTerm || undefined,
      category: this.filterCategory || undefined,
      maxPrice: this.maxPrice ?? undefined,
      sortBy: this.sortField,
      sortDir: this.sortDir,
      page: this.page,
      size: this.pageSize
    };

    this.productService.search(params).subscribe({
      next: (data) => { this.pageResult = data; },
      error: () => this.errorMessage = 'Erreur de connexion au serveur. Vérifiez que le backend est lancé.'
    });
  }

  goToPage(n: number): void {
    if (n < 0 || (this.pageResult && n >= this.pageResult.totalPages)) return;
    this.page = n;
    this.searchProducts(false);
  }

  onSort(field: string): void {
    if (this.sortField === field) { this.sortDir = this.sortDir === 'asc' ? 'desc' : 'asc'; }
    else { this.sortField = field; this.sortDir = 'asc'; }
    this.searchProducts(true);
  }

  sortIcon(field: string): string {
    if (this.sortField !== field) return '\u2195';
    return this.sortDir === 'asc' ? '\u2191' : '\u2193';
  }

  onDetails(id: number): void { this.router.navigate(['/marketplace/products', id]); }
  onEdit(id: number): void { this.router.navigate(['/marketplace/products', id, 'edit']); }

  onDelete(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce produit ?')) {
      this.productService.delete(id).subscribe({
        next: () => { this.searchProducts(false); this.showToast('Produit supprimé avec succès'); },
        error: (err) => this.errorMessage = err.error?.message || 'Erreur lors de la suppression'
      });
    }
  }

  onCreate(): void { this.router.navigate(['/marketplace/products/create']); }

  smartSearch(): void {
    if (!this.aiSearchQuery.trim()) return;
    this.isSearchingAi = true;
    this.marketplaceAiService.smartSearch(this.aiSearchQuery).subscribe({
      next: (filters) => {
        if (filters.category) this.filterCategory = filters.category;
        if (filters.priceMax) this.maxPrice = Number(filters.priceMax);
        if (filters.searchTerm) this.searchTerm = filters.searchTerm;
        this.isSearchingAi = false;
        this.searchProducts(true);
      },
      error: () => {
        this.isSearchingAi = false;
        this.errorMessage = 'Erreur recherche IA. Vérifiez qu\'Ollama est lancé.';
      }
    });
  }

  showToast(msg: string): void {
    this.successMessage = msg;
    setTimeout(() => this.successMessage = '', 3000);
  }

  getImageUrl(product: ProductDTO): string {
    if (!product.imageUrl) return '';
    return `http://localhost:18080/${product.imageUrl}`;
  }

  getSizeInfo(product: ProductDTO): string {
    if (product.pointure) return `Pointure ${product.pointure}`;
    if (product.taille) return `Taille ${product.taille}`;
    return '';
  }

  getUserName(product: ProductDTO): string {
    if (!product.user) return 'Vendeur';
    return [product.user.prenom, product.user.nom].filter(Boolean).join(' ') || 'Vendeur';
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
