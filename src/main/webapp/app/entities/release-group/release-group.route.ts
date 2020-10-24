import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReleaseGroup, ReleaseGroup } from 'app/shared/model/release-group.model';
import { ReleaseGroupService } from './release-group.service';
import { ReleaseGroupComponent } from './release-group.component';
import { ReleaseGroupDetailComponent } from './release-group-detail.component';
import { ReleaseGroupUpdateComponent } from './release-group-update.component';

@Injectable({ providedIn: 'root' })
export class ReleaseGroupResolve implements Resolve<IReleaseGroup> {
  constructor(private service: ReleaseGroupService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReleaseGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((releaseGroup: HttpResponse<ReleaseGroup>) => {
          if (releaseGroup.body) {
            return of(releaseGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReleaseGroup());
  }
}

export const releaseGroupRoute: Routes = [
  {
    path: '',
    component: ReleaseGroupComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releaseGroup.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReleaseGroupDetailComponent,
    resolve: {
      releaseGroup: ReleaseGroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releaseGroup.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReleaseGroupUpdateComponent,
    resolve: {
      releaseGroup: ReleaseGroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releaseGroup.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReleaseGroupUpdateComponent,
    resolve: {
      releaseGroup: ReleaseGroupResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.releaseGroup.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
