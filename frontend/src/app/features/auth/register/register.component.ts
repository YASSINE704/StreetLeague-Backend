import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, UserRole } from '../auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css', '../login/login.component.css']
})
export class RegisterComponent implements OnInit {
  selectedRole: UserRole = 'JOUEUR';
  showPassword = false;
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  profilePicture = '';
  profilePictureName = '';
  registerForm!: FormGroup;

  roles: { value: UserRole; label: string; desc: string }[] = [
    { value: 'JOUEUR', label: 'Player', desc: 'Join teams and play matches' },
    { value: 'COACH', label: 'Coach', desc: 'Manage trainings and programs' },
    { value: 'SPORTIF', label: 'Sportif', desc: 'Track training progress' },
    { value: 'TERRAIN_MANAGER', label: 'Terrain Manager', desc: 'Manage fields and bookings' },
    { value: 'ADMIN', label: 'Admin', desc: 'Full platform administration' }
  ];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      prenom: ['', [Validators.required, Validators.minLength(2)]],
      nom: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      age: [18, [Validators.required, Validators.min(6), Validators.max(100)]],
      niveau: ['BEGINNER', Validators.required],
      position: ['MIDFIELDER', Validators.required],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).+$/)
      ]],
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

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  onRegister(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';
    const { prenom, nom, email, password, age, niveau, position } = this.registerForm.value;
    if (this.selectedRole === 'JOUEUR' && !this.profilePicture) {
      this.isLoading = false;
      this.errorMessage = 'Please choose a profile picture for the player.';
      return;
    }

    this.authService.register({
      prenom,
      nom,
      email,
      password,
      role: this.selectedRole,
      age,
      niveau,
      position,
      profilePicture: this.profilePicture
    }).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Account created. Check your email verification code.';
        this.router.navigate(['/auth/verify-email'], { queryParams: { email } });
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = this.authService.getErrorMessage(err, 'Registration failed. Please check your information.');
      }
    });
  }

  onPhotoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }

    if (!file.type.startsWith('image/')) {
      this.errorMessage = 'Please choose an image file.';
      return;
    }

    if (file.size > 1_000_000) {
      this.errorMessage = 'Profile picture must be smaller than 1 MB.';
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      this.profilePicture = String(reader.result);
      this.profilePictureName = file.name;
      this.errorMessage = '';
    };
    reader.readAsDataURL(file);
  }

  hasError(form: FormGroup, field: string, error: string): boolean {
    const ctrl = form.get(field);
    return !!(ctrl?.touched && ctrl?.hasError(error));
  }
}
