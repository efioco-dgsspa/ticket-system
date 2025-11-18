import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  success(message: string, title = 'Successo') {
    Swal.fire({ icon: 'success', title, text: message, confirmButtonText: 'Ok' });
  }

  error(message: string, title = 'Errore') {
    Swal.fire({ icon: 'error', title, text: message, confirmButtonText: 'Chiudi' });
  }
}
