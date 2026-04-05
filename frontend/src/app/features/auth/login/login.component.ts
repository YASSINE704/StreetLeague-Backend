import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email = 'coach@streetleague.tn';
  password = 'coach123';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    if (!this.email || !this.password) {
      this.errorMessage = 'Email et mot de passe obligatoires';
      return;
    }

    this.authService.login(this.email, this.password);
    this.router.navigate(['/coaching/programmes']);
  }
}
