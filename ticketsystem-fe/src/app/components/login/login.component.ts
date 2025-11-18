import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../services/notification.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService
  ) {
    this.loginForm = this.fb.group({
      identifier: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.error = null;

    const { identifier, password } = this.loginForm.value;

    let credentials = {
      identifier: identifier,
      password: password
    }

    this.authService.login(credentials).subscribe({
      next: (res) => {
        console.log('Login riuscito!', res);
        this.notificationService.success('Login effettuato con successo!');
        this.router.navigate(['/home']); // esempio redirect
      },
      error: (err) => {
        // console.log("STAMPO err: ", err)
        this.error = err || 'Errore durante il login';
        this.loading = false;
        this.notificationService.error(err);
      }
    });
  }
}
