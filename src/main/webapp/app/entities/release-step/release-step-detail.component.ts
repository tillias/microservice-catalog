import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReleaseStep } from 'app/shared/model/release-step.model';

@Component({
  selector: 'jhi-release-step-detail',
  templateUrl: './release-step-detail.component.html',
})
export class ReleaseStepDetailComponent implements OnInit {
  releaseStep: IReleaseStep | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ releaseStep }) => (this.releaseStep = releaseStep));
  }

  previousState(): void {
    window.history.back();
  }
}
