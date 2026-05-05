import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SeanceService } from '../../../../core/services/seance.service';
import { ExerciceService } from '../../../../core/services/exercice.service';
import { AiRecommendationService, AIExerciseRecommendation } from '../../../../core/services/ai-recommendation.service';
import { AuthService } from '../../../auth/auth.service';
import { SeanceEntrainement, Exercice, SeanceExercice, TypeExercice } from '../../../../shared/models/programme-entrainement.model';

@Component({
  selector: 'app-seance-details',
  templateUrl: './seance-details.component.html',
  styleUrls: ['./seance-details.component.css']
})
export class SeanceDetailsComponent implements OnInit {
  seance!: SeanceEntrainement;
  catalogueExercices: Exercice[] = [];
  showAddForm = false;
  showAiPanel = false;
  errorMessage = '';
  successMessage = '';

  aiRecommendations: AIExerciseRecommendation[] = [];
  aiLoading = false;
  aiMessage = '';
  acceptingAiIndex: number | null = null;
  readonly aiTypes = Object.values(TypeExercice);

  aiContext = {
    objectifProgramme: '',
    typeSeance: TypeExercice.FORCE,
    niveauJoueurs: 'Tous niveaux'
  };

  newExercice: SeanceExercice = {
    seanceId: 0,
    exerciceId: 0,
    series: 3,
    repetitions: 10,
    charge: 0,
    tempsSecondes: 0,
    ordre: 1
  };

  constructor(
    private seanceService: SeanceService,
    private exerciceService: ExerciceService,
    private aiService: AiRecommendationService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCatalogueExercices();
    this.loadSeance();
  }

  get canManageExercises(): boolean {
    const role = this.authService.userRole;
    return (role === 'COACH' || role === 'ADMIN') && this.seance?.statut !== 'REALISEE';
  }

  loadSeance(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.seanceService.getById(id).subscribe({
      next: (data) => {
        this.seance = data;
        this.newExercice.seanceId = data.idSeance!;
        this.newExercice.ordre = this.getNextOrder(data);

        if (!this.aiContext.objectifProgramme) {
          this.aiContext.objectifProgramme = `${data.programmeTitre || 'Programme'} - ${data.titreSeance || 'Séance'}`;
        }
        this.aiContext.typeSeance = this.inferTypeFromSeance(data);
      },
      error: () => this.errorMessage = 'Erreur chargement séance'
    });
  }

  loadCatalogueExercices(): void {
    this.exerciceService.getAll().subscribe({
      next: (data) => this.catalogueExercices = data,
      error: () => this.errorMessage = 'Erreur lors du chargement du catalogue des exercices'
    });
  }

  toggleAddForm(): void {
    this.showAddForm = !this.showAddForm;
    if (this.showAddForm) {
      this.showAiPanel = false;
      if (this.catalogueExercices.length === 0) {
        this.loadCatalogueExercices();
      }
    }
  }

  toggleAiPanel(): void {
    this.showAiPanel = !this.showAiPanel;
    if (this.showAiPanel) {
      this.showAddForm = false;
      this.aiMessage = '';
      if (this.catalogueExercices.length === 0) {
        this.loadCatalogueExercices();
      }
    }
  }

  onAddExercice(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.canManageExercises) {
      this.errorMessage = 'Seul un coach ou un administrateur peut ajouter des exercices à une séance';
      return;
    }

    if (Number(this.newExercice.exerciceId) === 0) {
      this.errorMessage = 'Veuillez sélectionner un exercice';
      return;
    }

    const payload = this.buildSeanceExercicePayload(this.newExercice);
    if (!payload.repetitions && !payload.tempsSecondes) {
      this.errorMessage = 'Indiquez au moins des répétitions ou un temps en secondes';
      return;
    }

    this.exerciceService.addToSeance(payload).subscribe({
      next: () => {
        this.showAddForm = false;
        this.resetManualForm();
        this.showSuccess('Exercice ajouté à la séance avec succès');
        this.loadSeance();
      },
      error: (err) => this.errorMessage = this.extractErrorMessage(err, 'Erreur lors de l\'ajout')
    });
  }

  generateAIExercises(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.aiMessage = '';

    if (!this.canManageExercises) {
      this.errorMessage = 'Seul un coach ou un administrateur peut utiliser l\'assistant IA';
      return;
    }

    if (!this.seance?.idSeance) {
      this.errorMessage = 'Séance introuvable';
      return;
    }

    this.aiLoading = true;
    this.aiRecommendations = [];

    const context = {
      objectifProgramme: this.aiContext.objectifProgramme || `${this.seance.programmeTitre || ''} ${this.seance.titreSeance || ''}`.trim() || 'entraînement général',
      typeSeance: this.aiContext.typeSeance,
      intensite: this.seance.intensite || 'MOYENNE',
      nbParticipants: this.seance.maxParticipants || 5,
      dureeSeanceMinutes: this.seance.dureeMinutes || 60,
      niveauJoueurs: this.aiContext.niveauJoueurs || 'Tous niveaux',
      lieuType: this.seance.lieuNom || this.seance.endroitNom || 'NON_DEFINI',
      enPleinAir: !!this.seance.enPleinAir
    };

    this.aiService.recommend(context).subscribe({
      next: (res) => {
        this.aiLoading = false;
        this.aiRecommendations = res.recommandations || [];
        this.aiMessage = res.status === 'fallback'
          ? (res.message || "Service IA indisponible. Vous pouvez continuer avec l\'ajout manuel.")
          : '';
      },
      error: (err) => {
        this.aiLoading = false;
        this.errorMessage = this.extractErrorMessage(err, 'Erreur lors de la génération IA');
      }
    });
  }

  acceptAIExercise(recommendation: AIExerciseRecommendation, index: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.canManageExercises) {
      this.errorMessage = 'Seul un coach ou un administrateur peut valider les exercices proposés par IA';
      return;
    }

    this.acceptingAiIndex = index;
    const existing = this.findExistingExercise(recommendation);

    if (existing?.idExercice) {
      this.attachAIExerciseToSeance(existing.idExercice, recommendation, index);
      return;
    }

    const exerciceToCreate: Exercice = {
      nom: recommendation.nom,
      description: recommendation.description || recommendation.objectif || 'Exercice proposé par l\'assistant IA',
      type: this.toTypeExercice(recommendation.type),
      videoUrl: undefined
    };

    this.exerciceService.create(exerciceToCreate).subscribe({
      next: (created) => {
        this.catalogueExercices.push(created);
        this.attachAIExerciseToSeance(created.idExercice!, recommendation, index);
      },
      error: (err) => {
        this.acceptingAiIndex = null;
        this.errorMessage = this.extractErrorMessage(err, 'Erreur lors de la création de l\'exercice IA');
      }
    });
  }

  onRemoveExercice(id: number): void {
    if (!this.canManageExercises) {
      this.errorMessage = 'Seul un coach ou un administrateur peut retirer un exercice';
      return;
    }

    if (confirm('Retirer cet exercice de la séance ?')) {
      this.exerciceService.removeFromSeance(id).subscribe({
        next: () => {
          this.showSuccess('Exercice retiré de la séance');
          this.loadSeance();
        },
        error: (err) => this.errorMessage = this.extractErrorMessage(err, 'Erreur lors de la suppression')
      });
    }
  }

  onAddSuivi(): void {
    this.router.navigate(['/coaching/suivis/create', this.seance.idSeance]);
  }

  onBack(): void {
    if (this.seance?.programmeId) {
      this.router.navigate(['/coaching/programmes', this.seance.programmeId]);
      return;
    }
    this.router.navigate(['/coaching/seances']);
  }

  getTotalSeries(): number {
    if (!this.seance?.exercices) return 0;
    return this.seance.exercices.reduce((sum, ex) => sum + (ex.series || 0), 0);
  }

  private attachAIExerciseToSeance(exerciceId: number, recommendation: AIExerciseRecommendation, index: number): void {
    const alreadyAttached = (this.seance.exercices || []).some(ex =>
      Number(ex.exerciceId) === Number(exerciceId) ||
      this.normalizeText(ex.exerciceNom) === this.normalizeText(recommendation.nom)
    );

    if (alreadyAttached) {
      this.acceptingAiIndex = null;
      this.aiRecommendations.splice(index, 1);
      this.showSuccess(`L'exercice "${recommendation.nom}" est déjà programmé dans cette séance`);
      return;
    }

    const type = this.toTypeExercice(recommendation.type);
    const isTimed = type === TypeExercice.CARDIO || type === TypeExercice.MOBILITE;
    const durationSeconds = Math.max(60, (recommendation.dureeMinutes || 5) * 60);

    const link: SeanceExercice = {
      seanceId: this.seance.idSeance!,
      exerciceId,
      series: isTimed ? 1 : 3,
      repetitions: isTimed ? undefined : 12,
      charge: 0,
      tempsSecondes: isTimed ? durationSeconds : undefined,
      ordre: this.getNextOrder(this.seance)
    };

    this.exerciceService.addToSeance(this.buildSeanceExercicePayload(link)).subscribe({
      next: () => {
        this.acceptingAiIndex = null;
        this.aiRecommendations.splice(index, 1);
        this.showSuccess(`Exercice IA "${recommendation.nom}" ajouté à la séance`);
        this.loadSeance();
      },
      error: (err) => {
        this.acceptingAiIndex = null;
        this.errorMessage = this.extractErrorMessage(err, 'Erreur lors de l\'ajout de l\'exercice IA à la séance');
      }
    });
  }

  private buildSeanceExercicePayload(source: SeanceExercice): SeanceExercice {
    const payload: SeanceExercice = {
      seanceId: Number(source.seanceId),
      exerciceId: Number(source.exerciceId),
      series: source.series && source.series > 0 ? Number(source.series) : 1,
      charge: source.charge != null && source.charge >= 0 ? Number(source.charge) : 0,
      ordre: source.ordre && source.ordre > 0 ? Number(source.ordre) : this.getNextOrder(this.seance)
    };

    if (source.repetitions && source.repetitions > 0) {
      payload.repetitions = Number(source.repetitions);
    }
    if (source.tempsSecondes && source.tempsSecondes > 0) {
      payload.tempsSecondes = Number(source.tempsSecondes);
    }

    return payload;
  }

  private resetManualForm(): void {
    this.newExercice = {
      seanceId: this.seance.idSeance!,
      exerciceId: 0,
      series: 3,
      repetitions: 10,
      charge: 0,
      tempsSecondes: 0,
      ordre: this.getNextOrder(this.seance) + 1
    };
  }

  private findExistingExercise(recommendation: AIExerciseRecommendation): Exercice | undefined {
    const recName = this.normalizeText(recommendation.nom);
    const recType = this.toTypeExercice(recommendation.type);
    return this.catalogueExercices.find(ex =>
      this.normalizeText(ex.nom) === recName && (!ex.type || ex.type === recType)
    );
  }

  private normalizeText(value?: string): string {
    return (value || '').trim().toLowerCase();
  }

  private toTypeExercice(value?: string | TypeExercice): TypeExercice {
    const raw = String(value || '').toUpperCase();
    return this.aiTypes.includes(raw as TypeExercice) ? raw as TypeExercice : TypeExercice.FORCE;
  }

  private inferTypeFromSeance(seance: SeanceEntrainement): TypeExercice {
    if (seance.exercices?.length) {
      const existing = seance.exercices[0].exerciceNom;
      const fromCatalogue = this.catalogueExercices.find(ex => ex.nom === existing);
      if (fromCatalogue?.type) return fromCatalogue.type;
    }

    switch (seance.intensite) {
      case 'FORTE': return TypeExercice.FORCE;
      case 'FAIBLE': return TypeExercice.MOBILITE;
      default: return TypeExercice.CARDIO;
    }
  }

  private getNextOrder(seance: SeanceEntrainement): number {
    const exercices = seance?.exercices || [];
    if (exercices.length === 0) return 1;
    return Math.max(...exercices.map(ex => ex.ordre || 0)) + 1;
  }

  private showSuccess(message: string): void {
    this.successMessage = message;
    setTimeout(() => this.successMessage = '', 3500);
  }

  private extractErrorMessage(err: any, fallback: string): string {
    if (err?.error?.message) return err.error.message;
    if (err?.error?.error) return err.error.error;
    if (typeof err?.error === 'string') return err.error;
    return fallback;
  }
}
