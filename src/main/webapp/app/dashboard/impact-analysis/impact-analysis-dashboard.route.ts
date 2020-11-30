import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { ImpactAnalysisDashboardComponent } from './impact-analysis-dashboard.component';
import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Injectable } from '@angular/core';
import { EMPTY, Observable, of } from 'rxjs';
import { catchError, flatMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { IResult, Result } from 'app/shared/model/impact/analysis/result.model';
import { ImpactAnalysisCustomService } from 'app/services/impact-analysis-custom.service';

@Injectable({ providedIn: 'root' })
export class ImpactAnalysisResultResolve implements Resolve<IResult> {
  constructor(private service: ImpactAnalysisCustomService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResult> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((analysisResult: HttpResponse<IResult>) => {
          if (analysisResult.body) {
            return of(analysisResult.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        }),
        catchError(() => {
          return EMPTY;
        })
      );
    }
    return of(new Result());
  }
}

export const impactAnalysisDashboardRoute: Routes = [
  {
    path: ':id',
    component: ImpactAnalysisDashboardComponent,
    resolve: {
      analysisResult: ImpactAnalysisResultResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.microservice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
