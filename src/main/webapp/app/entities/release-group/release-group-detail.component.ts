import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReleaseGroup } from 'app/shared/model/release-group.model';

@Component({
  selector: 'jhi-release-group-detail',
  templateUrl: './release-group-detail.component.html',
})
export class ReleaseGroupDetailComponent implements OnInit {
  releaseGroup: IReleaseGroup | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releaseGroup }) => (this.releaseGroup = releaseGroup));
  }

  previousState(): void {
    window.history.back();
  }
}
