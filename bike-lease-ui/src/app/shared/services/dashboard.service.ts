import { Injectable } from '@angular/core';
import { forkJoin } from 'rxjs/internal/observable/forkJoin';
import { BikeCatalogService } from './bike-catalog.service';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  constructor(
    private userService: UserService,
    private bikeService: BikeCatalogService
  ) {}

  loadDashboardData() {

    return forkJoin({
      users: this.userService.getUsers(),
      bikes: this.bikeService.getAllBikes(0, 1000)
    });
  }
}
