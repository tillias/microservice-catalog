import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReleaseStep, ReleaseStep } from 'app/shared/model/release-step.model';
import { ReleaseStepService } from './release-step.service';
import { ReleaseStepComponent } from './release-step.component';
import { ReleaseStepDetailComponent } from './release-step-detail.component';
import { ReleaseStepUpdateComponent } from './release-step-update.component';

@Injectable({ providedIn: 'root' })
export class ReleaseStepResolve implements Resolve<IReleaseStep> {
  constructor(private service: ReleaseStepService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReleaseStep> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((releaseStep: HttpResponse<ReleaseStep>) => {
          if (releaseStep.body) {
            return of(releaseStep.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReleaseStep());
  }
}

export const releaseStepRoute: Routes = [
  {
    path: '',
    component: ReleaseStepComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releaseStep.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReleaseStepDetailComponent,
    resolve: {
      releaseStep: ReleaseStepResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releaseStep.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReleaseStepUpdateComponent,
    resolve: {
      releaseStep: ReleaseStepResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releaseStep.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReleaseStepUpdateComponent,
    resolve: {
      releaseStep: ReleaseStepResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releaseStep.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
