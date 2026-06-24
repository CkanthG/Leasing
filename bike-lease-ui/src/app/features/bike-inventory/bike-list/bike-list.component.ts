import { AfterViewInit, Component, computed, effect, inject, OnInit, signal, ViewChild, WritableSignal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { HeaderComponent } from '../../../core/layout/header.component/header.component';
import { BikeCatalogService } from '../../../shared/services/bike-catalog.service';
import { BikeCatalog } from '../../../shared/models/bike-catalog.model';
import { MatDialog } from '@angular/material/dialog';
import { AddBikeDialogContent } from './dialogs/add-bike-dialog-content/add-bike-dialog-content';
import { MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardActions } from "@angular/material/card";
import { MatTableDataSource } from '@angular/material/table';
import { LeaseBikeDialog } from './dialogs/lease-bike-dialog/lease-bike-dialog';

@Component({
  selector: 'app-bike-list',
  imports: [
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    HeaderComponent,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardSubtitle,
    MatCardActions
],
  templateUrl: './bike-list.component.html',
  styleUrl: './bike-list.component.scss',
})
export class BikeListComponent implements AfterViewInit {
  bikeCatalogDataSource: WritableSignal<BikeCatalog[]> = signal([]);
  readonly dialog = inject(MatDialog);

  displayedColumns: string[] = ['id', 'brand', 'model', 'variant', 'engineCc', 'price', 'leaseAmount', 'leaseTenure', 'mileage', 'availabilityStatus', 'insuranceDetails'];

  bikeDataSource = signal<BikeCatalog[]>([]);
  search = signal('');

  totalRecords = 0;

  pageSize = 10;

  pageIndex = 0;

  userRole = signal('')

  dataSource = new MatTableDataSource<BikeCatalog>(this.bikeCatalogDataSource());

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  bikeCatalogService: BikeCatalogService = inject(BikeCatalogService);

  ngAfterViewInit() {
    this.dataSource.data = this.computedData();
    this.dataSource.paginator = this.paginator;
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.search.set(filterValue.trim().toLowerCase());
  }

  constructor(
    bikeCatalogService: BikeCatalogService
  ) { 
    bikeCatalogService.getAllBikes(this.pageIndex, this.pageSize).subscribe(bikes => {
      this.bikeCatalogDataSource.set(bikes.content);
      this.bikeDataSource.set(bikes.content);
    });

    this.userRole.set(localStorage.getItem('role') as string)
   }

   computedData = computed(() => {
      const data = this.bikeDataSource();
      const search = this.search().trim().toLowerCase();

      if (!search) {
        return data;
      }

      return data.filter(item =>
        item.brand?.toLowerCase().includes(search) ||
        item.model?.toLowerCase().includes(search) ||
        item.variant?.toLowerCase().includes(search) ||
        String(item.engineCc).includes(search) ||
        String(item.price).includes(search) ||
        String(item.leaseAmount).includes(search)
      );
    });

   openDialog() {
    const dialogRef = this.dialog.open(AddBikeDialogContent, {
      width: '800px',
      height: '600px'
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`);
    });
  }

  openLeaseDialog(bikeCatalogId: number) {
    const leaseDialogRef = this.dialog.open(LeaseBikeDialog, {
      width: '800px',
      height: '300px',
      data: {
        bikeCatalogId: bikeCatalogId
      }
    });

    leaseDialogRef.afterClosed().subscribe(result => {
      console.log(`Lease Dialof Result : ${result}`);
    })
  }

  pageChanged(
  event: PageEvent
  ) {

    this.pageIndex = event.pageIndex;

    this.pageSize = event.pageSize;

    this.loadBikeCatalog();
  }

  loadBikeCatalog() {
    this.bikeCatalogService.getAllBikes(
      this.pageIndex,
      this.pageSize
    ).subscribe(bikes => {
      this.bikeCatalogDataSource.set(bikes.content);
      this.bikeDataSource.set(bikes.content);
      this.totalRecords = bikes.totalElements;
    });
  }
}
