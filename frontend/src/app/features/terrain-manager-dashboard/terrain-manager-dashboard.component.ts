import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';
import { TerrainService } from '../../core/services/terrain.service';
import { MatchService } from '../../core/services/match.service';
import { TerrainDTO, MatchDTO, TypeSport, TerrainRequest } from '../../shared/models/sports.model';

// Local view model — maps backend DTO to what the template expects
export interface TerrainView {
  id: number;
  name: string;
  address: string;
  location: string;
  typeSport: TypeSport;
  status: 'AVAILABLE' | 'OCCUPIED' | 'MAINTENANCE';
  currentMatch?: string;
  image: string;
  bookings: MatchDTO[];
}

@Component({
  selector: 'app-terrain-manager-dashboard',
  templateUrl: './terrain-manager-dashboard.component.html',
  styleUrls: ['./terrain-manager-dashboard.component.css']
})
export class TerrainManagerDashboardComponent implements OnInit {
  activeTab: 'OVERVIEW' | 'TERRAINS' | 'REQUESTS' | 'PROFILE' = 'OVERVIEW';
  editingTerrain: TerrainView | null = null;
  showAddForm = false;
  terrainToDelete: TerrainView | null = null;

  profileForm!: FormGroup;
  terrainForm!: FormGroup;

  terrains: TerrainView[] = [];
  upcomingMatches: MatchDTO[] = [];
  loading = false;
  error: string | null = null;

  readonly sportTypes: TypeSport[] = ['FOOTBALL', 'BASKETBALL', 'VOLLEYBALL', 'PADEL'];
  readonly terrainEmojis = ['🏟️', '⚽', '🌿', '🏃', '🥅', '🏆', '🏀', '🎾'];

  get stats() {
    return {
      total: this.terrains.length,
      available: this.terrains.filter(t => t.status === 'AVAILABLE').length,
      occupied: this.terrains.filter(t => t.status === 'OCCUPIED').length,
      pending: this.upcomingMatches.length
    };
  }

  constructor(
    private fb: FormBuilder,
    public authService: AuthService,
    private terrainService: TerrainService,
    private matchService: MatchService
  ) {}

  ngOnInit(): void {
    const user = this.authService.user;
    this.profileForm = this.fb.group({
      name: [user?.username || '', Validators.required],
      username: [user?.username || '', Validators.required],
      email: [user?.email || '', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      organization: ['', Validators.required],
      bio: ['Gestionnaire de terrains passionné par le street football.']
    });

    this.terrainForm = this.fb.group({
      name: ['', Validators.required],
      location: ['', Validators.required],
      address: ['', Validators.required],
      typeSport: ['FOOTBALL', Validators.required]
    });

    this.loadTerrains();
    this.loadUpcomingMatches();
  }

  loadTerrains(): void {
    this.loading = true;
    this.terrainService.getAll().subscribe({
      next: (dtos) => {
        this.terrains = dtos.map((dto, i) => this.toView(dto, i));
        // For each terrain, load its bookings to determine status
        this.terrains.forEach(t => this.loadTerrainBookings(t));
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Impossible de charger les terrains.';
        this.loading = false;
      }
    });
  }

  loadTerrainBookings(terrain: TerrainView): void {
    this.terrainService.getBookings(terrain.id).subscribe({
      next: (matches) => {
        terrain.bookings = matches;
        const now = new Date();
        const activeMatch = matches.find(m => {
          const d = new Date(m.matchDate);
          const end = new Date(d.getTime() + 2 * 60 * 60 * 1000);
          return m.status === 'IN_PROGRESS' || (d <= now && now <= end);
        });
        if (activeMatch) {
          terrain.status = 'OCCUPIED';
          terrain.currentMatch = `${activeMatch.homeTeamName} vs ${activeMatch.awayTeamName}`;
        }
      },
      error: () => { terrain.bookings = []; }
    });
  }

  loadUpcomingMatches(): void {
    this.matchService.getScheduled().subscribe({
      next: (matches) => { this.upcomingMatches = matches; },
      error: () => { this.upcomingMatches = []; }
    });
  }

  private toView(dto: TerrainDTO, index: number): TerrainView {
    return {
      id: dto.id,
      name: dto.nom,
      address: dto.address,
      location: dto.location,
      typeSport: dto.typeSport,
      status: 'AVAILABLE',
      image: this.terrainEmojis[index % this.terrainEmojis.length],
      bookings: []
    };
  }

  openEditTerrain(terrain: TerrainView): void {
    this.editingTerrain = terrain;
    this.showAddForm = false;
    this.terrainForm.patchValue({
      name: terrain.name,
      location: terrain.location,
      address: terrain.address,
      typeSport: terrain.typeSport
    });
    this.activeTab = 'TERRAINS';
  }

  openAddForm(): void {
    this.editingTerrain = null;
    this.showAddForm = true;
    this.terrainForm.reset({ typeSport: 'FOOTBALL' });
  }

  saveTerrain(): void {
    if (this.terrainForm.invalid) return;
    const val = this.terrainForm.value;
    const payload: TerrainRequest = {
      nom: val.name,
      location: val.location,
      address: val.address,
      typeSport: val.typeSport as TypeSport
    };

    if (this.editingTerrain) {
      this.terrainService.update(this.editingTerrain.id, payload).subscribe({
        next: (dto) => {
          const idx = this.terrains.findIndex(t => t.id === dto.id);
          if (idx !== -1) {
            this.terrains[idx] = { ...this.terrains[idx], ...this.toView(dto, idx) };
          }
          this.editingTerrain = null;
          this.terrainForm.reset({ typeSport: 'FOOTBALL' });
        },
        error: (err) => {
          const msg = err?.error?.message || err?.error?.error || 'Erreur lors de la mise à jour du terrain.';
          this.error = msg;
          alert('❌ ' + msg);
        }
      });
    } else {
      this.terrainService.create(payload).subscribe({
        next: (dto) => {
          this.terrains.push(this.toView(dto, this.terrains.length));
          this.showAddForm = false;
          this.terrainForm.reset({ typeSport: 'FOOTBALL' });
        },
        error: (err) => {
          const msg = err?.error?.message || err?.error?.error || 'Erreur lors de la création du terrain.';
          this.error = msg;
          alert('❌ ' + msg);
        }
      });
    }
  }

  cancelEdit(): void {
    this.editingTerrain = null;
    this.showAddForm = false;
    this.terrainForm.reset({ typeSport: 'FOOTBALL' });
  }

  confirmDelete(terrain: TerrainView): void {
    this.terrainToDelete = terrain;
  }

  removeTerrain(): void {
    if (!this.terrainToDelete) return;
    this.terrainService.delete(this.terrainToDelete.id).subscribe({
      next: () => {
        this.terrains = this.terrains.filter(t => t.id !== this.terrainToDelete!.id);
        this.terrainToDelete = null;
      },
      error: (err) => {
        const msg = err?.error?.message || err?.error?.error || 'Erreur lors de la suppression du terrain.';
        this.error = msg;
        alert('❌ ' + msg);
        this.terrainToDelete = null;
      }
    });
  }

  cancelDelete(): void {
    this.terrainToDelete = null;
  }

  // Requests tab now shows upcoming scheduled matches
  get requests(): MatchDTO[] {
    return this.upcomingMatches;
  }

  saveProfile(): void {
    if (this.profileForm.valid) {
      alert('Profil mis à jour avec succès !');
    }
  }

  getStatusLabel(status: TerrainView['status']): string {
    return { AVAILABLE: 'Disponible', OCCUPIED: 'Occupé', MAINTENANCE: 'Maintenance' }[status];
  }

  dismissError(): void {
    this.error = null;
  }
}
