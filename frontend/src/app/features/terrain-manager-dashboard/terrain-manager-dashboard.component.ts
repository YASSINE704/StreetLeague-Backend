import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth/auth.service';

export interface Terrain {
  id: string;
  name: string;
  address: string;
  surface: string;
  capacity: number;
  status: 'AVAILABLE' | 'OCCUPIED' | 'MAINTENANCE';
  currentMatch?: string;
  image: string;
}

export interface TerrainRequest {
  id: string;
  terrainId: string;
  terrainName: string;
  teamName: string;
  teamLogo: string;
  teamColor: string;
  date: string;
  time: string;
  duration: string;
  requestedBy: string;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
}

@Component({
  selector: 'app-terrain-manager-dashboard',
  templateUrl: './terrain-manager-dashboard.component.html',
  styleUrls: ['./terrain-manager-dashboard.component.css']
})
export class TerrainManagerDashboardComponent implements OnInit {
  activeTab: 'OVERVIEW' | 'TERRAINS' | 'REQUESTS' | 'PROFILE' = 'OVERVIEW';
  editingTerrain: Terrain | null = null;
  showAddForm = false;
  terrainToDelete: Terrain | null = null;

  profileForm!: FormGroup;
  terrainForm!: FormGroup;

  terrains: Terrain[] = [
    {
      id: 't1',
      name: 'Terrain Nord',
      address: '12 Rue des Sports, Paris 18e',
      surface: 'Synthétique',
      capacity: 10,
      status: 'OCCUPIED',
      currentMatch: 'Urban Legends vs Kings FC',
      image: '🏟️'
    },
    {
      id: 't2',
      name: 'The Cage',
      address: '5 Avenue du Stade, Paris 13e',
      surface: 'Béton',
      capacity: 6,
      status: 'AVAILABLE',
      image: '⚽'
    },
    {
      id: 't3',
      name: 'Terrain Sud',
      address: '88 Boulevard du Jeu, Paris 15e',
      surface: 'Gazon naturel',
      capacity: 14,
      status: 'MAINTENANCE',
      image: '🌿'
    }
  ];

  requests: TerrainRequest[] = [
    {
      id: 'r1',
      terrainId: 't2',
      terrainName: 'The Cage',
      teamName: 'Swift Strikers',
      teamLogo: '⚡',
      teamColor: 'linear-gradient(135deg, #10b981, #059669)',
      date: 'Apr 14',
      time: '19:00',
      duration: '1h30',
      requestedBy: 'Alex Martin',
      status: 'PENDING'
    },
    {
      id: 'r2',
      terrainId: 't1',
      terrainName: 'Terrain Nord',
      teamName: 'Red Devils',
      teamLogo: '😈',
      teamColor: 'linear-gradient(135deg, #ef4444, #b91c1c)',
      date: 'Apr 16',
      time: '17:00',
      duration: '2h',
      requestedBy: 'Jordan Dupont',
      status: 'PENDING'
    },
    {
      id: 'r3',
      terrainId: 't2',
      terrainName: 'The Cage',
      teamName: 'Urban Legends',
      teamLogo: '🏙️',
      teamColor: 'linear-gradient(135deg, #3b82f6, #1d4ed8)',
      date: 'Apr 10',
      time: '20:00',
      duration: '1h',
      requestedBy: 'Marcus Kane',
      status: 'ACCEPTED'
    }
  ];

  get stats() {
    return {
      total: this.terrains.length,
      available: this.terrains.filter(t => t.status === 'AVAILABLE').length,
      occupied: this.terrains.filter(t => t.status === 'OCCUPIED').length,
      pending: this.requests.filter(r => r.status === 'PENDING').length
    };
  }

  constructor(private fb: FormBuilder, public authService: AuthService) {}

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
      address: ['', Validators.required],
      surface: ['Synthétique', Validators.required],
      capacity: [10, [Validators.required, Validators.min(2)]]
    });
  }

  openEditTerrain(terrain: Terrain): void {
    this.editingTerrain = terrain;
    this.showAddForm = false;
    this.terrainForm.patchValue({
      name: terrain.name,
      address: terrain.address,
      surface: terrain.surface,
      capacity: terrain.capacity
    });
    this.activeTab = 'TERRAINS';
  }

  openAddForm(): void {
    this.editingTerrain = null;
    this.showAddForm = true;
    this.terrainForm.reset({ surface: 'Synthétique', capacity: 10 });
  }

  saveTerrain(): void {
    if (this.terrainForm.invalid) return;
    const val = this.terrainForm.value;
    if (this.editingTerrain) {
      Object.assign(this.editingTerrain, val);
      this.editingTerrain = null;
    } else {
      const emojis = ['🏟️', '⚽', '🌿', '🏃', '🥅', '🏆'];
      this.terrains.push({
        id: 't' + Date.now(),
        name: val.name,
        address: val.address,
        surface: val.surface,
        capacity: val.capacity,
        status: 'AVAILABLE',
        image: emojis[this.terrains.length % emojis.length]
      });
      this.showAddForm = false;
    }
    this.terrainForm.reset({ surface: 'Synthétique', capacity: 10 });
  }

  cancelEdit(): void {
    this.editingTerrain = null;
    this.showAddForm = false;
    this.terrainForm.reset({ surface: 'Synthétique', capacity: 10 });
  }

  confirmDelete(terrain: Terrain): void {
    this.terrainToDelete = terrain;
  }

  removeTerrain(): void {
    if (!this.terrainToDelete) return;
    this.terrains = this.terrains.filter(t => t.id !== this.terrainToDelete!.id);
    this.requests = this.requests.filter(r => r.terrainId !== this.terrainToDelete!.id);
    this.terrainToDelete = null;
  }

  cancelDelete(): void {
    this.terrainToDelete = null;
  }

  acceptRequest(id: string): void {
    const req = this.requests.find(r => r.id === id);
    if (req) {
      req.status = 'ACCEPTED';
      const terrain = this.terrains.find(t => t.id === req.terrainId);
      if (terrain) terrain.status = 'OCCUPIED';
    }
  }

  declineRequest(id: string): void {
    const req = this.requests.find(r => r.id === id);
    if (req) req.status = 'DECLINED';
  }

  saveProfile(): void {
    if (this.profileForm.valid) {
      alert('Profil mis à jour avec succès !');
    }
  }

  getStatusLabel(status: Terrain['status']): string {
    return { AVAILABLE: 'Disponible', OCCUPIED: 'Occupé', MAINTENANCE: 'Maintenance' }[status];
  }
}
