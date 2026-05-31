import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { AuthService } from '../../auth/auth.service';
import { SUBCATEGORIES, POINTURES, TAILLES } from '../../../shared/models/marketplace.model';

@Component({
  selector: 'app-product-create',
  templateUrl: './product-create.component.html',
  styleUrls: ['./product-create.component.css']
})
export class ProductCreateComponent {
  name = '';
  description = '';
  price: number | null = null;
  category = '';
  subcategory = '';
  pointure: number | null = null;
  taille = '';
  selectedFile: File | null = null;
  imagePreview = '';
  errorMessage = '';
  successMessage = '';
  isLoading = false;

  readonly categories = ['football', 'basketball', 'volleyball', 'padel', 'tennis', 'running', 'fitness'];
  readonly SUBCATEGORIES = SUBCATEGORIES;
  readonly POINTURES = POINTURES;
  readonly TAILLES = TAILLES;

  constructor(
    private productService: ProductService,
    private router: Router,
    private authService: AuthService
  ) {}

  get subcats(): string[] {
    return this.SUBCATEGORIES[this.category] || [];
  }

  get showPointure(): boolean {
    return this.subcategory === 'shoes';
  }

  get showTaille(): boolean {
    return this.subcategory === 'pull' || this.subcategory === 'shorts';
  }

  onCategoryChange(): void {
    this.subcategory = '';
    this.pointure = null;
    this.taille = '';
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    if (!file.type.startsWith('image/')) {
      this.errorMessage = 'Please select an image file.';
      return;
    }

    if (file.size > 5_000_000) {
      this.errorMessage = 'Image must be smaller than 5 MB.';
      return;
    }

    this.selectedFile = file;
    this.errorMessage = '';

    const reader = new FileReader();
    reader.onload = () => this.imagePreview = String(reader.result);
    reader.readAsDataURL(file);
  }

  removeImage(): void {
    this.selectedFile = null;
    this.imagePreview = '';
  }

  onSubmit(): void {
    if (!this.name.trim() || !this.description.trim() || this.price === null || !this.category) {
      this.errorMessage = 'Tous les champs sont requis.';
      return;
    }

    const currentUser = this.authService.currentUser;
    if (!currentUser) {
      this.errorMessage = 'Vous devez être connecté pour ajouter un produit.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    const userId = Number(currentUser.id);

    this.productService.create({
      name: this.name,
      description: this.description,
      price: this.price,
      category: this.category,
      subcategory: this.subcategory || undefined,
      pointure: this.showPointure ? this.pointure ?? undefined : undefined,
      taille: this.showTaille ? this.taille : undefined,
      user: { idUser: userId }
    }).subscribe({
      next: (product) => {
        if (this.selectedFile && product.id) {
          this.productService.uploadImage(product.id, this.selectedFile).subscribe({
            next: () => {
              this.isLoading = false;
              this.successMessage = 'Produit créé avec succès !';
              setTimeout(() => this.router.navigate(['/marketplace/products']), 1500);
            },
            error: () => {
              this.isLoading = false;
              this.successMessage = 'Produit créé (sans image)';
              setTimeout(() => this.router.navigate(['/marketplace/products']), 1500);
            }
          });
        } else {
          this.isLoading = false;
          this.successMessage = 'Produit créé avec succès !';
          setTimeout(() => this.router.navigate(['/marketplace/products']), 1500);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Erreur lors de la création.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/marketplace/products']);
  }
}
