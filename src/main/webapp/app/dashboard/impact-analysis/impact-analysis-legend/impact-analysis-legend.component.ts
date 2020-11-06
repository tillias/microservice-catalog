import { Component, OnInit } from '@angular/core';
import { IResult } from 'app/shared/model/impact/analysis/result.model';
import { ActivatedRoute } from '@angular/router';
import { IMicroservice } from 'app/shared/model/microservice.model';

@Component({
  selector: 'jhi-impact-analysis-legend',
  templateUrl: './impact-analysis-legend.component.html',
})
export class ImpactAnalysisLegendComponent implements OnInit {
  analysisResult?: IResult;
  microservices: IMicroservice[] = [];
  page = 1;
  pageSize = 15;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analysisResult }) => {
      this.analysisResult = analysisResult;

      this.initDependencies();
    });
  }

  initDependencies(): void {
    const targetId = this.analysisResult?.target?.id;
    this.microservices = [];
    this.analysisResult?.groups?.forEach(v =>
      v.items?.forEach(i => {
        if (i.target.id !== targetId) {
          this.microservices.push(i.target);
        }
      })
    );
  }
}
