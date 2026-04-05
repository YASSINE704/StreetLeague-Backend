import { Component } from '@angular/core';
import { NotificationService } from '../../core/services/notification.service';

@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.css']
})
export class MainLayoutComponent {
  showNotifs = false;

  constructor(public notifService: NotificationService) {}

  toggleNotifs(): void {
    this.showNotifs = !this.showNotifs;
  }
}
