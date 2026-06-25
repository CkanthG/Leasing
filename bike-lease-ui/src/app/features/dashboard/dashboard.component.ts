import { Component, OnInit, signal } from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import { DashboardService } from '../../shared/services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  imports: [MatCardModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  totalUsers = signal(0);
  totalBikes = signal(0);
  activeBikes = signal(0);
  disabledUsers = signal(0);

  constructor(
    private dashboardService: DashboardService
  ) {}

  ngOnInit(): void {

    this.dashboardService
      .loadDashboardData()
      .subscribe({

        next: data => {

          this.totalUsers.set(
            data.users.length
          );
          console.log("users ", data.users);
          this.disabledUsers.set(
            data.users.filter((user: any) => user.enabled === false).length
          );

          this.totalBikes.set(
            data.bikes.content.length
          );

          this.activeBikes.set(
            data.bikes.content.filter((bike: any) => bike.availabilityStatus === true).length
          );

        },

        error: err => {
          console.error(err);
        }
      });
  }

}
