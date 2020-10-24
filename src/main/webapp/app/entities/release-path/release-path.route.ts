import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReleasePath, ReleasePath } from 'app/shared/model/release-path.model';
import { ReleasePathService } from './release-path.service';
import { ReleasePathComponent } from './release-path.component';
import { ReleasePathDetailComponent } from './release-path-detail.component';
import { ReleasePathUpdateComponent } from './release-path-update.component';

@Injectable({ providedIn: 'root' })
export class ReleasePathResolve implements Resolve<IReleasePath> {
  constructor(private service: ReleasePathService, private router: Router) {}

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
        })
      );
    }
    return of(new ReleasePath());
  }
}

export const releasePathRoute: Routes = [
  {
    path: '',
    component: ReleasePathComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releasePath.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReleasePathDetailComponent,
    resolve: {
      releasePath: ReleasePathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releasePath.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReleasePathUpdateComponent,
    resolve: {
      releasePath: ReleasePathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releasePath.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReleasePathUpdateComponent,
    resolve: {
      releasePath: ReleasePathResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releasePath.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
