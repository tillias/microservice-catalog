import { Component, OnInit } from '@angular/core';
import { IntegrationTestsService } from 'app/services/integration-tests.service';
import { ActivatedRoute } from '@angular/router';
import { IResult } from '../../shared/model/impact/analysis/result.model';

@Component({
  selector: 'jhi-impact-analysis-dashboard',
  templateUrl: './impact-analysis-dashboard.component.html',
})
export class ImpactAnalysisDashboardComponent implements OnInit {
  analysisResult?: IResult;

  constructor(protected activatedRoute: ActivatedRoute, protected service: IntegrationTestsService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ analysisResult }) => {
      this.analysisResult = analysisResult;
    });
  }

  startIntegrationTests(): void {
    if (this.analysisResult && this.analysisResult.target) {
      this.service.trigger(this.analysisResult.target.id!).subscribe();
    }
  }
}
