import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { SeanceEntrainement } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-seance-details',
  templateUrl: './seance-details.component.html',
  styleUrls: ['./seance-details.component.css']
})
export class SeanceDetailsComponent implements OnInit {
  seance!: SeanceEntrainement;

  constructor(
    private seanceService: SeanceService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.seanceService.getById(id).subscribe({
      next: (data) => this.seance = data,
      error: (err) => console.error('Erreur chargement séance', err)
    });
  }

  onAddSuivi(): void {
    this.router.navigate(['/coaching/suivis/create', this.seance.idSeance]);
  }

  onBack(): void {
    this.router.navigate(['/coaching/seances']);
  }
}
