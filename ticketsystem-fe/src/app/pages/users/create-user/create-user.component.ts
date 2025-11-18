import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { Role } from '../../../model/role.model';
import { UserService } from '../../../services/user/user.service';
import { RoleService } from '../../../services/role/role.service';
import { UserRequest } from '../../../model/user.request.model';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss'],
  imports: [CommonModule, ReactiveFormsModule]
})
export class CreateUserComponent implements OnInit {
  userForm!: FormGroup;
  roles: Role[] = [];
  loadingRoles = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private roleService: RoleService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadRoles();
  }

  private initForm(): void {
    this.userForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      role: [null, Validators.required] // singolo ruolo
    });
  }

  private loadRoles(): void {
    this.loadingRoles = true;
    this.roleService.getAllRoles().subscribe({
      next: (list) => {
        this.roles = list;
        this.loadingRoles = false;
      },
      error: (err) => {
        this.loadingRoles = false;
        console.error('Errore caricamento ruoli:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    let role = {
      id: this.userForm.value.role
    }

    const request: UserRequest = {
      user: {
        username: this.userForm.value.username,
        email: this.userForm.value.email,
        password: this.userForm.value.password,
        roles: [role] // inserisco in array perché backend si aspetta Role[]
      }
    };

    console.log('[UserService] 🔍 Prima di creare user, token presente?', !!this.authService.getAccessToken());
    console.log('[UserService] 🔍 Refresh token presente?', !!this.authService.getRefreshToken());

    this.userService.createUser(request).subscribe({
      next: () => {
        Swal.fire('Successo!', 'Utente creato correttamente', 'success');
        this.userForm.reset();
      },
      error: (err) => {
        console.error('Errore creazione utente:', err);
        Swal.fire('Errore', 
            err.error?.customerMessage ? err.error?.customerMessage : 'Si è verificato un errore durante la creazione dell\'utente'
        );
      }
    });
  }
}
