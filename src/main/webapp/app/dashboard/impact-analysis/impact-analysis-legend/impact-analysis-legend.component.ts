import { Component, OnInit } from '@angular/core';
import { IResult } from '../../../shared/model/impact/analysis/result.model';
import { ActivatedRoute } from '@angular/router';
import { IMicroservice } from '../../../shared/model/microservice.model';

@Component({
  selector: 'jhi-impact-analysis-legend',
  templateUrl: './impact-analysis-legend.component.html',
  styleUrls: ['./impact-analysis-legend.component.scss'],
})
export class ImpactAnalysisLegendComponent implements OnInit {
  analysisResult?: IResult;
  dependencies: IMicroservice[] = [];
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
    this.dependencies = [];
    this.analysisResult?.groups?.forEach(v => v.items?.forEach(i => this.dependencies.push(i.target)));
  }
}
