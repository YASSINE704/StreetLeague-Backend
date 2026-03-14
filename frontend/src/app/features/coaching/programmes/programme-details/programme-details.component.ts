import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProgrammeService } from '../../../../core/services/programme.service';
import { ProgrammeEntrainement } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-programme-details',
  templateUrl: './programme-details.component.html',
  styleUrls: ['./programme-details.component.css']
})
export class ProgrammeDetailsComponent implements OnInit {
  programme!: ProgrammeEntrainement;

  constructor(
    private programmeService: ProgrammeService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.programmeService.getById(id).subscribe({
      next: (data) => this.programme = data,
      error: (err) => console.error('Erreur chargement programme', err)
    });
  }

  onAddSeance(): void {
    this.router.navigate(['/coaching/seances/create', this.programme.idProgramme]);
  }

  onSeanceDetails(seanceId: number): void {
    this.router.navigate(['/coaching/seances', seanceId]);
  }

  onBack(): void {
    this.router.navigate(['/coaching/programmes']);
  }
}
