import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user/user.service';
import { NotificationService } from '../../../services/notification.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormControl } from '@angular/forms';
import { User } from '../../../model/user.model';
import { Role } from '../../../model/role.model';
import { RoleService } from '../../../services/role/role.service';

@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.scss']
})
export class UserDetailComponent implements OnInit {
  userForm!: FormGroup<{
    username: FormControl<string | null>;
    email: FormControl<string | null>;
    role: FormControl<string | null>;
    passwordCorrente: FormControl<string | null>;
    nuovaPassword: FormControl<string | null>;
    active: FormControl<boolean | null>;
  }>;

  roles: Role[] = [];
  mode: 'view' | 'edit' = 'view';
  loading = true;
  user!: User;

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private userService: UserService,
    private roleService: RoleService,
    private notificationService: NotificationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.mode = params['mode'] === 'edit' ? 'edit' : 'view';
    });

    const username = this.route.snapshot.paramMap.get('username');
    if (!username) {
      this.notificationService.error('Parametro username mancante');
      return;
    }

    this.loadRoles().then(() => {
      this.userService.getUserByUsername(username).subscribe({
        next: (user) => {
          this.user = user;
          this.buildForm(user);
          this.loading = false;
        },
        error: () => {
          this.notificationService.error('Impossibile caricare utente');
          this.loading = false;
        }
      });
    });
  }

  private async loadRoles(): Promise<void> {
    return new Promise((resolve) => {
      this.roleService.getAllRoles().subscribe({
        next: (roles) => {
          this.roles = roles;
          resolve();
        },
        error: () => {
          this.notificationService.error('Errore durante il caricamento dei ruoli');
          resolve();
        }
      });
    });
  }

  /** 💾 Salvataggio modifiche */
  onSubmit(): void {
    if (this.mode === 'view') return;

    if (!this.userForm.valid) {
      if (this.userForm.errors?.['passwordMismatch']) {
        this.notificationService.error('Se vuoi modificare la password, compila entrambi i campi.');
      } else {
        this.notificationService.error('Controlla i campi obbligatori.');
      }
      return;
    }

    const formValue = this.userForm.getRawValue();
    const selectedRole = this.roles.find(r => r.id === formValue.role);

    const updatedUser: User = {
      ...this.user,
      username: formValue.username!,
      email: formValue.email!,
      passwordCorrente: formValue.passwordCorrente || undefined,
      nuovaPassword: formValue.nuovaPassword || undefined,
      roles: selectedRole ? [selectedRole] : [],
      active: formValue.active ?? false
    };

    this.userService.updateUser({ user: updatedUser }).subscribe({
      next: () => {
        this.notificationService.success('Utente aggiornato con successo.');
        this.router.navigate(['/users']); // ✅ redirect automatico
      },
      error: (err) => {
        this.notificationService.error(err.error?.message || 'Errore durante aggiornamento.');
      }
    });
  }

  /** 🧱 Costruisce il form */
  buildForm(user: User): void {
    const currentRole = user.roles?.[0]?.id || null;
    this.userForm = this.fb.group({
      username: this.fb.control(
        { value: user.username, disabled: this.mode === 'view' },
        Validators.required
      ),
      email: this.fb.control(
        { value: user.email, disabled: this.mode === 'view' },
        [Validators.required, Validators.email]
      ),
      role: this.fb.control(
        { value: currentRole, disabled: this.mode === 'view' },
        Validators.required
      ),
      passwordCorrente: this.fb.control(''),
      nuovaPassword: this.fb.control(''),
      active: this.fb.control(
        { value: user.active ?? true, disabled: this.mode === 'view' }
      )
    }, { validators: this.passwordsValidator }) as FormGroup<{
      username: FormControl<string | null>;
      email: FormControl<string | null>;
      role: FormControl<string | null>;
      passwordCorrente: FormControl<string | null>;
      nuovaPassword: FormControl<string | null>;
      active: FormControl<boolean | null>;
    }>;
  }

  private passwordsValidator(form: FormGroup) {
    const current = form.get('passwordCorrente')?.value;
    const newPass = form.get('nuovaPassword')?.value;
    if ((current && !newPass) || (!current && newPass)) {
      return { passwordMismatch: true };
    }
    return null;
  }

  get f() {
    return this.userForm.controls;
  }
}
