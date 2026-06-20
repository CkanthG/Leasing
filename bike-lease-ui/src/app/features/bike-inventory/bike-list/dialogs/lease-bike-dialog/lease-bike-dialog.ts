import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogActions, MatDialogModule } from '@angular/material/dialog';

@Component({
  selector: 'app-lease-bike-dialog',
  imports: [MatDialogModule, MatButtonModule, MatDialogActions],
  templateUrl: './lease-bike-dialog.html',
  styleUrl: './lease-bike-dialog.scss',
})
export class LeaseBikeDialog {

}
