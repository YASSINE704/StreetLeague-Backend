import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { FieldService } from '../../../core/services/field.service';
import { Endroit, SousEspace } from '../../../core/models/endroit.model';
import * as L from 'leaflet';

@Component({
  selector: 'app-client-map',
  templateUrl: './client-map.component.html',
  styleUrls: ['./client-map.component.css']
})
export class ClientMapComponent implements OnInit, AfterViewInit {
  endroits: Endroit[] = [];
  filteredEndroits: Endroit[] = [];
  sousEspaces: SousEspace[] = [];
  filteredSousEspaces: SousEspace[] = [];
  activeTab: 'endroits' | 'sous-espaces' = 'endroits';

  searchTerm = '';
  typeFilter = '';
  villeFilter = '';
  statutFilter = '';
  villes: string[] = [];
  private map!: L.Map;
  private markers: L.Marker[] = [];
  private icon = L.icon({
    iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
    iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34]
  });

  constructor(private fieldService: FieldService, private router: Router) {}

  ngOnInit(): void {
    this.fieldService.getAllEndroits().subscribe(data => {
      this.endroits = data;
      this.filteredEndroits = data;
      this.villes = [...new Set(data.map(e => e.ville).filter(v => v))];
      this.addMarkers();
    });
    this.fieldService.getAllSousEspaces().subscribe(data => {
      this.sousEspaces = data;
      this.filteredSousEspaces = data;
    });
  }

  ngAfterViewInit(): void {
    this.map = L.map('client-map').setView([36.8, 10.18], 12);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(this.map);
  }

  private addMarkers(): void {
    if (!this.map) return;
    this.markers.forEach(m => m.remove());
    this.markers = [];
    this.filteredEndroits.forEach(e => {
      if (e.latitude && e.longitude) {
        const popup = `
          <div style="min-width:180px">
            <strong>${e.nom}</strong><br>
            <span>${e.type} · ${e.ville}</span><br>
            <span>👥 Capacité: ${e.capacite}</span><br>
            <span>${e.statut}</span><br>
            <a href="/client/${e.id}" style="color:#e94560;font-weight:600">Voir & Réserver →</a>
          </div>`;
        const marker = L.marker([e.latitude, e.longitude], { icon: this.icon })
          .addTo(this.map)
          .bindPopup(popup);
        this.markers.push(marker);
      }
    });
  }

  applyFilters(): void {
    const term = this.searchTerm.toLowerCase();

    this.filteredEndroits = this.endroits.filter(e => {
      const matchSearch = !term || e.nom.toLowerCase().includes(term) || e.adresse?.toLowerCase().includes(term);
      const matchType = !this.typeFilter || e.type === this.typeFilter;
      const matchVille = !this.villeFilter || e.ville === this.villeFilter;
      const matchStatut = !this.statutFilter || e.statut === this.statutFilter;
      return matchSearch && matchType && matchVille && matchStatut;
    });

    this.filteredSousEspaces = this.sousEspaces.filter(se => {
      const matchSearch = !term || se.nom.toLowerCase().includes(term) || (se.endroitNom && se.endroitNom.toLowerCase().includes(term));
      const matchStatut = !this.statutFilter || se.statut === this.statutFilter;
      return matchSearch && matchStatut;
    });

    this.addMarkers();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.typeFilter = '';
    this.villeFilter = '';
    this.statutFilter = '';
    this.filteredEndroits = this.endroits;
    this.filteredSousEspaces = this.sousEspaces;
    this.addMarkers();
  }

  getStatutClass(statut: string): string {
    return statut === 'DISPONIBLE' ? 'badge-success' : statut === 'MAINTENANCE' ? 'badge-warning' : 'badge-danger';
  }

  getStatutBadge(statut: string): string {
    switch (statut) {
      case 'DISPONIBLE': return 'sl-badge--green';
      case 'MAINTENANCE': return 'sl-badge--orange';
      default: return 'sl-badge--red';
    }
  }

  navigateTo(id: number): void {
    this.router.navigate(['/client', id]);
  }

  navigateToSe(id: number): void {
    this.router.navigate(['/client/sous-espace', id]);
  }
}
