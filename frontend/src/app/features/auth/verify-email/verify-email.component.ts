import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.css', '../login/login.component.css']
})
export class VerifyEmailComponent implements OnInit {
  form!: FormGroup;
  isLoading = false;
  isResending = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      email: [this.route.snapshot.queryParamMap.get('email') ?? '', [Validators.required, Validators.email]],
      code: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]]
    });
  }

  onVerify(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';
    const { email, code } = this.form.value;

    this.authService.verifyEmail(email, code).subscribe({
      next: () => {
        this.isLoading = false;
        this.successMessage = 'Email verified. You can now sign in.';
        setTimeout(() => this.router.navigate(['/auth/login'], { queryParams: { email } }), 800);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = this.authService.getErrorMessage(err, 'Verification failed. Check the code and try again.');
      }
    });
  }

  resendCode(): void {
    const email = this.form.get('email')?.value;
    if (!email || this.form.get('email')?.invalid) {
      this.form.get('email')?.markAsTouched();
      return;
    }

    this.isResending = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.resendVerification(email).subscribe({
      next: (response) => {
        this.isResending = false;
        this.successMessage = response.message || 'Verification code sent.';
      },
      error: (err) => {
        this.isResending = false;
        this.errorMessage = this.authService.getErrorMessage(err, 'Could not resend the verification code.');
      }
    });
  }

  hasError(field: string, error: string): boolean {
    const ctrl = this.form.get(field);
    return !!(ctrl?.touched && ctrl?.hasError(error));
  }
}
