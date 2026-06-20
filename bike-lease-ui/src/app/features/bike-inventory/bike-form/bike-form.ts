import { Component, inject, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from "@angular/material/icon";
import { MatCardModule } from '@angular/material/card';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-bike-form',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule, 
    MatButtonModule, 
    MatIconModule, 
    MatCardModule],
  templateUrl: './bike-form.html',
  styleUrl: './bike-form.scss',
})
export class BikeForm {

  bikeForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.bikeForm = this.fb.group({
      brand: [''],
      model: [''],
      variant: [''],
      engineCc: [''],
      price: [''],
      leaseAmount: [''],
      leaseTenure: [''],
      mileage: [''],
      availabilityStatus: [true],
      images: [null],
      insuranceDetails: ['']
    });
  }

  getValue() {
    return this.bikeForm.value;
  }

  selectedFile: File | null = null;
  insuranceFile: File | null = null;

  onFileSelected(event: Event): void {
    const file = (event.target as HTMLInputElement).files?.[0];

    if (file) {
      this.selectedFile = file;
    }
  }
}
