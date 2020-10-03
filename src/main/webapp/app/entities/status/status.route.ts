import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IStatus, Status } from 'app/shared/model/status.model';
import { StatusService } from './status.service';
import { StatusComponent } from './status.component';
import { StatusDetailComponent } from './status-detail.component';
import { StatusUpdateComponent } from './status-update.component';

@Injectable({ providedIn: 'root' })
export class StatusResolve implements Resolve<IStatus> {
  constructor(private service: StatusService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStatus> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((status: HttpResponse<Status>) => {
          if (status.body) {
            return of(status.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Status());
  }
}

export const statusRoute: Routes = [
  {
    path: '',
    component: StatusComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.status.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StatusDetailComponent,
    resolve: {
      status: StatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.status.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StatusUpdateComponent,
    resolve: {
      status: StatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.status.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StatusUpdateComponent,
    resolve: {
      status: StatusResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.status.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
