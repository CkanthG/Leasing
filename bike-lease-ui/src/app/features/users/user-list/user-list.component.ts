import { Component, WritableSignal, computed, signal } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { HeaderComponent } from '../../../core/layout/header.component/header.component';
import { UserService } from '../../../shared/services/user.service';
import { User } from '../../../shared/models/user.model';

@Component({
  selector: 'app-user-list',
  imports: [
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    HeaderComponent
  ],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.scss',
})
export class UserListComponent {
  userDataSource: WritableSignal<User[]> = signal([]);

  displayedColumns: string[] = ['id', 'email', 'role', 'enabled', 'createdAt'];

  dataSource = signal<User[]>([]);
  search = signal('');

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.search.set(filterValue.trim().toLowerCase());
  }

  constructor(
    userService: UserService
  ) {
    userService.getUsers().subscribe((users: User[]) => {
      this.userDataSource.set(users);
      this.dataSource.set(users);
    });
  }

  computedData = computed(() => {
      const data = this.dataSource();
      console.log('Computed data recalculated:', data);
      const search = this.search().trim().toLowerCase();

      if (!search) {
        return data;
      }
      return data.filter(item =>
        item.email.toLowerCase().includes(search) ||
        item.role.toLowerCase().includes(search) ||
        item.enabled.toString().toLowerCase().includes(search) ||
        item.createdAt.toString().toLowerCase().includes(search)
      );
    });
}
