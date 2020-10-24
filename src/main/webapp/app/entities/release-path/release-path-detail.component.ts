import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReleasePath } from 'app/shared/model/release-path.model';

@Component({
  selector: 'jhi-release-path-detail',
  templateUrl: './release-path-detail.component.html',
})
export class ReleasePathDetailComponent implements OnInit {
  releasePath: IReleasePath | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releasePath }) => (this.releasePath = releasePath));
  }

  previousState(): void {
    window.history.back();
  }
}
