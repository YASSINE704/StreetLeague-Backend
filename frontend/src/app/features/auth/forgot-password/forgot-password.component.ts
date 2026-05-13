import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css', '../login/login.component.css']
})
export class ForgotPasswordComponent {
  requestForm: FormGroup;
  resetForm: FormGroup;
  codeSent = false;
  showPassword = false;
  isRequesting = false;
  isResetting = false;
  errorMessage = '';
  successMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.requestForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

    this.resetForm = this.fb.group({
      code: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]],
      newPassword: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).+$/)
      ]]
    });
  }

  requestCode(): void {
    if (this.requestForm.invalid) {
      this.requestForm.markAllAsTouched();
      return;
    }

    this.isRequesting = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.forgotPassword(this.requestForm.value.email).subscribe({
      next: (response) => {
        this.isRequesting = false;
        this.codeSent = true;
        this.successMessage = response.message || 'If the email exists, a reset code has been sent.';
      },
      error: (err) => {
        this.isRequesting = false;
        this.errorMessage = this.authService.getErrorMessage(err, 'Could not request password reset.');
      }
    });
  }

  resetPassword(): void {
    if (this.resetForm.invalid) {
      this.resetForm.markAllAsTouched();
      return;
    }

    this.isResetting = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.resetPassword(
      this.requestForm.value.email,
      this.resetForm.value.code,
      this.resetForm.value.newPassword
    ).subscribe({
      next: (response) => {
        this.isResetting = false;
        this.successMessage = response.message || 'Password reset successful. You can sign in now.';
        this.resetForm.reset();
      },
      error: (err) => {
        this.isResetting = false;
        this.errorMessage = this.authService.getErrorMessage(err, 'Password reset failed.');
      }
    });
  }

  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }

  hasError(form: FormGroup, field: string, error: string): boolean {
    const ctrl = form.get(field);
    return !!(ctrl?.touched && ctrl?.hasError(error));
  }
}
