import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { FieldService } from '../../../core/services/field.service';
import { Endroit } from '../../../core/models/endroit.model';
import * as L from 'leaflet';

@Component({
  selector: 'app-client-map',
  templateUrl: './client-map.component.html',
  styleUrls: ['./client-map.component.css']
})
export class ClientMapComponent implements OnInit, AfterViewInit {
  endroits: Endroit[] = [];
  private map!: L.Map;
  private icon = L.icon({
    iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
    iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34]
  });

  constructor(private fieldService: FieldService, private router: Router) {}

  ngOnInit(): void {
    this.fieldService.getAllEndroits().subscribe(data => {
      this.endroits = data;
      this.addMarkers();
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
    this.endroits.forEach(e => {
      if (e.latitude && e.longitude) {
        const popup = `
          <div style="min-width:180px">
            <strong>${e.nom}</strong><br>
            <span>${e.type} · ${e.ville}</span><br>
            <span>👥 Capacité: ${e.capacite}</span><br>
            <span class="badge-${e.statut === 'DISPONIBLE' ? 'ok' : 'no'}">${e.statut}</span><br>
            <a href="/client/${e.id}" style="color:#e94560;font-weight:600">Voir & Réserver →</a>
          </div>`;
        L.marker([e.latitude, e.longitude], { icon: this.icon })
          .addTo(this.map)
          .bindPopup(popup);
      }
    });
    if (this.endroits.length > 0) {
      const bounds = this.endroits
        .filter(e => e.latitude && e.longitude)
        .map(e => [e.latitude, e.longitude] as [number, number]);
      if (bounds.length > 0) this.map.fitBounds(bounds, { padding: [50, 50] });
    }
  }

  getStatutClass(statut: string): string {
    return statut === 'DISPONIBLE' ? 'badge-success' : statut === 'MAINTENANCE' ? 'badge-warning' : 'badge-danger';
  }

  navigateTo(id: number): void {
    this.router.navigate(['/client', id]);
  }
}
