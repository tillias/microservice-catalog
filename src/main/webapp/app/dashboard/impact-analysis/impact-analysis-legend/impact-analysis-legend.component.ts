import { Component, OnInit } from '@angular/core';
import { IResult } from '../../../shared/model/impact/analysis/result.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-impact-analysis-legend',
  templateUrl: './impact-analysis-legend.component.html',
  styleUrls: ['./impact-analysis-legend.component.scss'],
})
export class ImpactAnalysisLegendComponent implements OnInit {
  analysisResult?: IResult;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analysisResult }) => {
      this.analysisResult = analysisResult;
    });
  }
}
