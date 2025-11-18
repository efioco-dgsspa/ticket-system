import { Component, OnInit } from '@angular/core';
import { LoaderService } from '../../services/loader.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-loader',
  standalone: true,  // ✅ Aggiungi questo
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss'],
  imports: [CommonModule]
})
export class LoaderComponent {
  constructor(public loaderService: LoaderService) {}
}