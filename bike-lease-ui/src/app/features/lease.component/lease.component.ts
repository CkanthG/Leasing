import { Component, computed, inject, signal, ViewChild, WritableSignal } from '@angular/core';
import { LeaseService } from '../../shared/services/lease.service';
import { LeaseResponse } from '../../shared/models/lease-response.model';
import { MatTable, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatFormFieldModule, MatLabel } from "@angular/material/form-field";
import { MatSortModule } from '@angular/material/sort';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-lease.component',
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
],
  templateUrl: './lease.component.html',
  styleUrl: './lease.component.scss',
})
export class LeaseComponent {

   displayedColumns: string[] = ['leaseId', 'eventId', 'eventType', 'createdAt', 'leaseTenure'];

  leaseDataSource: WritableSignal<LeaseResponse[]> = signal([]);

  leaseDS = signal<LeaseResponse[]>([]);
  search = signal('');

  pageSize = 10;

  pageIndex = 0;

  totalRecords = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  leaseService: LeaseService = inject(LeaseService);

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.search.set(filterValue.trim().toLowerCase());
  }

  constructor(
    leaseService: LeaseService
  ) {
    this.leaseService.getAllLeases(this.pageIndex, this.pageSize).subscribe(leases => {
      this.leaseDataSource.set(leases.content);
      this.totalRecords = leases.totalElements;
    });
  }

  computedData = computed(() => {
      const data = this.leaseDataSource();
      const search = this.search().trim().toLowerCase();

      if (!search) {
        return data;
      }

      const result = data.filter(item =>
        item.leaseId?.toLowerCase().includes(search) ||
        item.eventId?.toLowerCase().includes(search) ||
        item.eventType?.toLowerCase().includes(search) ||
        String(item.leaseTenure).includes(search)
      );

      this.totalRecords = result.length;

      return result;
    });

    pageChanged(
  event: PageEvent
  ) {

    this.pageIndex = event.pageIndex;

    this.pageSize = event.pageSize;

    this.loadLeases();
  }

  loadLeases() {
    this.leaseService.getAllLeases(
      this.pageIndex,
      this.pageSize
    ).subscribe(leases => {
      this.leaseDataSource.set(leases.content);
      this.leaseDS.set(leases.content);
      this.totalRecords = leases.totalElements;
    });
  }

}
