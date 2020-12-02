import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { ReleasePathDashboardComponent } from './release-path-dashboard.component';
import { Injectable } from '@angular/core';
import { EMPTY, Observable, of } from 'rxjs';
import { catchError, flatMap } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { IReleasePath, ReleasePath } from 'app/shared/model/release-path.model';
import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ReleasePathCustomService } from 'app/services/release-path-custom.service';

@Injectable({ providedIn: 'root' })
export class ReleasePathResolve implements Resolve<IReleasePath> {
  constructor(private service: ReleasePathCustomService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReleasePath> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((releasePath: HttpResponse<ReleasePath>) => {
          if (releasePath.body) {
            return of(releasePath.body);
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
    return of(new ReleasePath());
  }
}

export const releasePathDashboardRoute: Routes = [
  {
    path: ':id',
    component: ReleasePathDashboardComponent,
    resolve: {
      releasePath: ReleasePathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.microservice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
