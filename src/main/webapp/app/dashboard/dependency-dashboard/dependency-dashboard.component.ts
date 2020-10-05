import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-dependency-dashboard',
  templateUrl: './dependency-dashboard.component.html',
  styleUrls: ['./dependency-dashboard.component.scss'],
})
export class DependencyDashboardComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  onSelect(event: any): void {
    // eslint-disable-next-line no-console
    console.log(event);
  }
}
