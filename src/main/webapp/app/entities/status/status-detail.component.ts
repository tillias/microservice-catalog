import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStatus } from 'app/shared/model/status.model';

@Component({
  selector: 'jhi-status-detail',
  templateUrl: './status-detail.component.html',
})
export class StatusDetailComponent implements OnInit {
  status: IStatus | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ status }) => (this.status = status));
  }

  previousState(): void {
    window.history.back();
  }
}
