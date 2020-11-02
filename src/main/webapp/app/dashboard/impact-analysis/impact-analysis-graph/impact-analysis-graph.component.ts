import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IResult } from '../../../shared/model/impact/analysis/result.model';

@Component({
  selector: 'jhi-impact-analysis-graph',
  templateUrl: './impact-analysis-graph.component.html',
  styleUrls: ['./impact-analysis-graph.component.scss'],
})
export class ImpactAnalysisGraphComponent implements OnInit {
  analysisResult?: IResult;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analysisResult }) => {
      this.analysisResult = analysisResult;
    });
  }
}
