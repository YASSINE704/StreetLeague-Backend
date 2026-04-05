import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'PI_StreetLeague';
  sidebarCollapsed = false;
  isAuthRoute = false;

  constructor(private router: Router) {
    this.router.events
      .pipe(filter((event): event is NavigationEnd => event instanceof NavigationEnd))
      .subscribe((event) => {
        this.isAuthRoute = event.urlAfterRedirects.startsWith('/auth');
      });

    this.isAuthRoute = this.router.url.startsWith('/auth');
  }
}
