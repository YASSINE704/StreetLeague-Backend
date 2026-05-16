import { Component, OnInit, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FieldService } from '../../../core/services/field.service';
import { TypeEndroit, StatutEndroit } from '../../../core/models/endroit.model';
import * as L from 'leaflet';

@Component({
  selector: 'app-edit-field',
  templateUrl: './edit-field.component.html',
  styleUrls: ['./edit-field.component.css']
})
export class EditFieldComponent implements OnInit, AfterViewInit {
  form: FormGroup;
  types = Object.values(TypeEndroit);
  statuts = Object.values(StatutEndroit);
  id!: number;
  currentImageUrl: string | null = null;
  selectedFile: File | null = null;
  private map!: L.Map;
  private marker!: L.Marker;
  private mapReady = false;
  private pendingCoords: [number, number] | null = null;

  constructor(
    private fb: FormBuilder,
    private fieldService: FieldService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      nom: ['', Validators.required],
      type: ['', Validators.required],
      adresse: ['', Validators.required],
      ville: ['', Validators.required],
      latitude: [36.8, Validators.required],
      longitude: [10.18, Validators.required],
      capacite: [0, [Validators.required, Validators.min(1)]],
      statut: ['', Validators.required],
      description: ['']
    });
  }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id')!;
    this.fieldService.getEndroitById(this.id).subscribe(endroit => {
      this.form.patchValue(endroit);
      this.currentImageUrl = endroit.imageUrl || null;
      if (this.mapReady) {
        this.updateMapPosition(endroit.latitude, endroit.longitude);
      } else {
        this.pendingCoords = [endroit.latitude, endroit.longitude];
      }
    });
  }

  ngAfterViewInit(): void {
    const coords = this.pendingCoords || [36.8, 10.18];
    this.initMap(coords[0], coords[1]);
    this.mapReady = true;
    this.pendingCoords = null;
  }

  private initMap(lat: number, lng: number): void {
    this.map = L.map('map-edit').setView([lat, lng], 13);
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

  private updateMapPosition(lat: number, lng: number): void {
    if (lat && lng) {
      this.map.setView([lat, lng], 13);
      this.marker.setLatLng([lat, lng]);
    }
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) this.selectedFile = input.files[0];
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.fieldService.updateEndroit(this.id, this.form.value).subscribe({
        next: () => {
          if (this.selectedFile) {
            this.fieldService.uploadImage(this.id, this.selectedFile).subscribe({
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
