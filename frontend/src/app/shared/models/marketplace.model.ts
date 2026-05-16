export interface ProductDTO {
  id?: number;
  name: string;
  description: string;
  price: number;
  category: string;
  subcategory?: string;
  pointure?: number;
  taille?: string;
  imageUrl?: string;
  user?: { idUser: number; nom?: string; prenom?: string; };
}

export const PRODUCT_CATEGORIES = ['football', 'basketball', 'volleyball', 'padel', 'tennis', 'running', 'fitness'] as const;
export type ProductCategory = typeof PRODUCT_CATEGORIES[number];

export const SUBCATEGORIES: Record<string, string[]> = {
  football: ['shoes', 'pull', 'shorts', 'equipment', 'accessories'],
  basketball: ['shoes', 'pull', 'shorts', 'equipment', 'accessories'],
  volleyball: ['shoes', 'pull', 'shorts', 'equipment'],
  padel: ['shoes', 'pull', 'shorts', 'equipment'],
  tennis: ['shoes', 'pull', 'shorts', 'equipment'],
  running: ['shoes', 'pull', 'shorts', 'accessories'],
  fitness: ['shoes', 'pull', 'shorts', 'equipment']
};

export const POINTURES = [38, 39, 40, 41, 42, 43, 44, 45, 46];
export const TAILLES = ['XS', 'S', 'M', 'L', 'XL', 'XXL'];

export interface ProductSearchRequest {
  query?: string;
  category?: string;
  subcategory?: string;
  minPrice?: number;
  maxPrice?: number;
  pointure?: number;
  taille?: string;
  sortBy?: string;
  sortDir?: string;
  page?: number;
  size?: number;
}

export interface Page<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}
