import { Component, ViewChild } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogActions, MatDialogModule } from '@angular/material/dialog';
import { BikeForm } from "../../../bike-form/bike-form";
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../../environments/environment';

@Component({
  selector: 'app-add-bike-dialog-content',
  imports: [MatDialogModule, MatButtonModule, MatDialogActions, BikeForm],
  templateUrl: './add-bike-dialog-content.html',
  styleUrl: './add-bike-dialog-content.scss',
})
export class AddBikeDialogContent {

  bikeCatalogUrl = environment.bikeCatalogApiUrl + '/bikeCatalog';

  @ViewChild(BikeForm) bikeForm!: BikeForm;

  constructor(
    private http: HttpClient
  ) {}

  onSave(event: Event): void {
    const formData = new FormData();

    const data = this.bikeForm.getValue();

    console.log('Form Data before appending to FormData:', data);

    formData.append('brand', data.brand);
    formData.append('model', data.model);
    formData.append('variant', data.variant);
    formData.append('engineCc', data.engineCc);
    formData.append('price', data.price);
    formData.append('leaseAmount', data.leaseAmount);
    formData.append('leaseTenure', data.leaseTenure);
    formData.append('mileage', data.mileage);
    formData.append('availabilityStatus', data.availabilityStatus);
    formData.append('insuranceDetails', data.insuranceDetails);

    if (this.bikeForm.selectedFile) {
      formData.append('images', this.bikeForm.selectedFile);
    }

  console.log('Form Data:', formData);

    // send to backend
    this.http.post(this.bikeCatalogUrl, formData).subscribe( {
      next: (response) => {
        console.log('Bike added successfully', response);
      },
      error: (error) => {
        console.error('Error adding bike', error);
      }
    }
    );
  }

}
