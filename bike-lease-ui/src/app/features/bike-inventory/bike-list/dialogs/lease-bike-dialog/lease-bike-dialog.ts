import { Component, Inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogActions, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { LeaseService } from '../../../../../shared/services/lease.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-lease-bike-dialog',
  imports: [
    MatDialogModule, 
    MatButtonModule, 
    MatDialogActions, 
    MatFormFieldModule, 
    ReactiveFormsModule,
    MatInputModule,
  ],
  templateUrl: './lease-bike-dialog.html',
  styleUrl: './lease-bike-dialog.scss',
})
export class LeaseBikeDialog {
  leaseForm: FormGroup;
  email: string = "";
  bikeCatalogId: number;

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { bikeCatalogId: number },
    private formBuilder: FormBuilder,
    private leaseService: LeaseService
  ) {
    this.email = localStorage.getItem('email') as string
    this.leaseForm = this.formBuilder.group({
      leaseTenure: [null],
      email: [this.email]
    });
    this.bikeCatalogId = data.bikeCatalogId;
  }

  onLease() {
    
    this.leaseService.applyLease(
      {
        "bikeCatalogId" : this.bikeCatalogId,
        "email": this.email,
        "leaseTenure": this.leaseForm.value.leaseTenure!
      }
    )
  .subscribe({
    next: response => {
      console.log(response);
    },
    error: err => {
      console.error(err);
    }
  });
  }

}
