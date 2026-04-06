import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, UserRole } from '../auth.service';

type AuthMode = 'login' | 'register';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  mode: AuthMode = 'login';
  selectedRole: UserRole = 'JOUEUR';
  showPassword = false;
  isLoading = false;
  errorMessage = '';

  loginForm!: FormGroup;
  registerForm!: FormGroup;

  roles: { value: UserRole; label: string; icon: string; desc: string }[] = [
    { value: 'JOUEUR',           label: 'Joueur',           icon: '⚽', desc: 'Rejoins une équipe, participe aux matchs' },
    { value: 'TERRAIN_MANAGER',  label: 'Terrain Manager',  icon: '🏟️', desc: 'Gère les terrains et les disponibilités' },
    { value: 'ADMIN',            label: 'Administrateur',   icon: '🛡️', desc: 'Gère l\'ensemble de la plateforme' },
    { value: 'COACH',            label: 'Coach',            icon: '🏆', desc: 'Gère les programmes et exercices' },
    { value: 'SPORTIF',          label: 'Sportif',          icon: '🏃', desc: 'Suit ses séances d\'entraînement' }
  ];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn) {
      this.authService.redirectAfterLogin();
      return;
    }

    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });

    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(group: FormGroup) {
    const pw = group.get('password')?.value;
    const cpw = group.get('confirmPassword')?.value;
    return pw === cpw ? null : { mismatch: true };
  }

  selectRole(role: UserRole): void {
    this.selectedRole = role;
  }

  switchMode(mode: AuthMode): void {
    this.mode = mode;
    this.errorMessage = '';
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onLogin(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    const { email, password } = this.loginForm.value;

    this.authService.loginWithBackend(email, password).subscribe({
      next: () => {
        this.isLoading = false;
        this.authService.redirectAfterLogin();
      },
      error: (err) => {
        this.isLoading = false;
        const msg = err?.error?.error;
        this.errorMessage = msg === 'Invalid credentials'
          ? 'Identifiants incorrects. Veuillez réessayer.'
          : 'Erreur de connexion. Vérifiez vos identifiants.';
      }
    });
  }

  onRegister(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    this.errorMessage = '';
    const { username, email, password } = this.registerForm.value;

    this.authService.registerWithBackend(username, email, password, this.selectedRole).subscribe({
      next: () => {
        this.isLoading = false;
        this.authService.redirectAfterLogin();
      },
      error: (err) => {
        this.isLoading = false;
        const msg = err?.error?.error;
        this.errorMessage = msg === 'Email already in use'
          ? 'Cet email est déjà utilisé.'
          : 'Erreur lors de l\'inscription. Veuillez réessayer.';
      }
    });
  }

  hasError(form: FormGroup, field: string, error: string): boolean {
    const ctrl = form.get(field);
    return !!(ctrl?.touched && ctrl?.hasError(error));
  }
}
