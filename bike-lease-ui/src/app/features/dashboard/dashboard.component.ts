import { Component, OnInit, signal } from '@angular/core';
import {MatCardModule} from '@angular/material/card';
import { DashboardService } from '../../shared/services/dashboard.service';
import { HeaderComponent } from '../../core/layout/header.component/header.component';

@Component({
  selector: 'app-dashboard',
  imports: [MatCardModule, HeaderComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  totalUsers = signal(0);
  totalBikes = signal(0);

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

          this.totalBikes.set(
            data.bikes.content.length
          );

        },

        error: err => {
          console.error(err);
        }
      });
  }

}
