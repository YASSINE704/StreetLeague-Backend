import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../../../core/services/product.service';
import { SUBCATEGORIES, POINTURES, TAILLES, ProductDTO } from '../../../shared/models/marketplace.model';

@Component({
  selector: 'app-product-edit',
  templateUrl: './product-edit.component.html',
  styleUrls: ['./product-edit.component.css']
})
export class ProductEditComponent implements OnInit {
  name = '';
  description = '';
  price: number | null = null;
  category = '';
  subcategory = '';
  pointure: number | null = null;
  taille = '';
  existingImageUrl = '';
  selectedFile: File | null = null;
  imagePreview = '';
  errorMessage = '';
  successMessage = '';
  isLoading = false;
  productId!: number;

  readonly categories = ['football', 'basketball', 'volleyball', 'padel', 'tennis', 'running', 'fitness'];
  readonly SUBCATEGORIES = SUBCATEGORIES;
  readonly POINTURES = POINTURES;
  readonly TAILLES = TAILLES;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService
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

  ngOnInit(): void {
    this.productId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.productId) {
      this.loadProduct();
    }
  }

  loadProduct(): void {
    this.productService.getById(this.productId).subscribe({
      next: (product) => {
        this.name = product.name;
        this.description = product.description;
        this.price = product.price;
        this.category = product.category;
        this.subcategory = product.subcategory || '';
        this.pointure = product.pointure || null;
        this.taille = product.taille || '';
        this.existingImageUrl = product.imageUrl || '';
      },
      error: () => {
        this.errorMessage = 'Produit introuvable.';
        setTimeout(() => this.router.navigate(['/marketplace/products']), 2000);
      }
    });
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
    this.existingImageUrl = '';
  }

  onSubmit(): void {
    if (!this.name.trim() || !this.description.trim() || this.price === null || !this.category) {
      this.errorMessage = 'Tous les champs sont requis.';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.productService.update(this.productId, {
      name: this.name,
      description: this.description,
      price: this.price,
      category: this.category,
      subcategory: this.subcategory || undefined,
      pointure: this.showPointure ? this.pointure ?? undefined : undefined,
      taille: this.showTaille ? this.taille : undefined
    }).subscribe({
      next: () => {
        if (this.selectedFile) {
          this.productService.uploadImage(this.productId, this.selectedFile).subscribe({
            next: () => {
              this.isLoading = false;
              this.successMessage = 'Produit mis à jour avec succès !';
              setTimeout(() => this.router.navigate(['/marketplace/products', this.productId]), 1500);
            },
            error: () => {
              this.isLoading = false;
              this.successMessage = 'Produit mis à jour (sans image)';
              setTimeout(() => this.router.navigate(['/marketplace/products', this.productId]), 1500);
            }
          });
        } else {
          this.isLoading = false;
          this.successMessage = 'Produit mis à jour avec succès !';
          setTimeout(() => this.router.navigate(['/marketplace/products', this.productId]), 1500);
        }
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour.';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/marketplace/products', this.productId]);
  }
}
