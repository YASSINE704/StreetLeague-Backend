import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SuiviService } from '../../../../core/services/suivi.service';
import { SuiviSeance } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-suivi-create',
  templateUrl: './suivi-create.component.html',
  styleUrls: ['./suivi-create.component.css']
})
export class SuiviCreateComponent implements OnInit {
  suivi: SuiviSeance = {
    seanceId: 0,
    ressenti: 5,
    fatigue: 5,
    commentaire: ''
  };
  errorMessage = '';

  constructor(
    private suiviService: SuiviService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.suivi.seanceId = Number(this.route.snapshot.paramMap.get('seanceId'));
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.suiviService.create(this.suivi).subscribe({
      next: () => this.router.navigate(['/coaching/seances', this.suivi.seanceId]),
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la création du suivi';
      }
    });
  }

  onCancel(): void {
    this.router.navigate(['/coaching/seances', this.suivi.seanceId]);
  }
}
