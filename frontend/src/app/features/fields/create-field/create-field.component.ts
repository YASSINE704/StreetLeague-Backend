import { Component, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { FieldService } from '../../../core/services/field.service';
import { TypeEndroit, StatutEndroit } from '../../../core/models/endroit.model';
import * as L from 'leaflet';

@Component({
  selector: 'app-create-field',
  templateUrl: './create-field.component.html',
  styleUrls: ['./create-field.component.css']
})
export class CreateFieldComponent implements AfterViewInit {
  form: FormGroup;
  types = Object.values(TypeEndroit);
  statuts = Object.values(StatutEndroit);
  selectedFile: File | null = null;
  private map!: L.Map;
  private marker!: L.Marker;

  constructor(private fb: FormBuilder, private fieldService: FieldService, private router: Router) {
    this.form = this.fb.group({
      nom: ['', Validators.required],
      type: ['STADE', Validators.required],
      adresse: ['', Validators.required],
      ville: ['', Validators.required],
      latitude: [36.8, Validators.required],
      longitude: [10.18, Validators.required],
      capacite: [0, [Validators.required, Validators.min(1)]],
      statut: ['DISPONIBLE', Validators.required],
      description: ['']
    });
  }

  ngAfterViewInit(): void {
    this.initMap(36.8, 10.18);
  }

  private initMap(lat: number, lng: number): void {
    this.map = L.map('map').setView([lat, lng], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(this.map);

    const icon = L.icon({
      iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
      shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
      iconSize: [25, 41], iconAnchor: [12, 41]
    });

    this.marker = L.marker([lat, lng], { icon, draggable: true }).addTo(this.map);
    this.marker.on('dragend', () => {
      const pos = this.marker.getLatLng();
      this.form.patchValue({ latitude: +pos.lat.toFixed(6), longitude: +pos.lng.toFixed(6) });
    });

    this.map.on('click', (e: L.LeafletMouseEvent) => {
      this.marker.setLatLng(e.latlng);
      this.form.patchValue({ latitude: +e.latlng.lat.toFixed(6), longitude: +e.latlng.lng.toFixed(6) });
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) this.selectedFile = input.files[0];
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.fieldService.createEndroit(this.form.value).subscribe({
        next: (endroit) => {
          if (this.selectedFile && endroit.id) {
            this.fieldService.uploadImage(endroit.id, this.selectedFile).subscribe({
              next: () => this.router.navigate(['/endroits']),
              error: () => this.router.navigate(['/endroits'])
            });
          } else {
            this.router.navigate(['/endroits']);
          }
        },
        error: (err) => console.error('Erreur:', err)
      });
    }
  }
}
