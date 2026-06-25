import { Component } from '@angular/core';
import { MatSidenav, MatSidenavContent, MatSidenavContainer } from '@angular/material/sidenav';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from "../header/header.component/header.component";
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { SidebarComponent } from "../sidebar/sidebar.component/sidebar.component";

@Component({
  selector: 'app-layout',
  imports: [
    MatSidenav,
    MatSidenavContent,
    RouterOutlet,
    MatSidenavContainer,
    SidebarComponent,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    SidebarComponent
],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})
export class LayoutComponent {

}
