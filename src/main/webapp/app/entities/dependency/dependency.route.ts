import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDependency, Dependency } from 'app/shared/model/dependency.model';
import { DependencyService } from './dependency.service';
import { DependencyComponent } from './dependency.component';
import { DependencyDetailComponent } from './dependency-detail.component';
import { DependencyUpdateComponent } from './dependency-update.component';

@Injectable({ providedIn: 'root' })
export class DependencyResolve implements Resolve<IDependency> {
  constructor(private service: DependencyService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDependency> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((dependency: HttpResponse<Dependency>) => {
          if (dependency.body) {
            return of(dependency.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dependency());
  }
}

export const dependencyRoute: Routes = [
  {
    path: '',
    component: DependencyComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.dependency.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DependencyDetailComponent,
    resolve: {
      dependency: DependencyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.dependency.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DependencyUpdateComponent,
    resolve: {
      dependency: DependencyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.dependency.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DependencyUpdateComponent,
    resolve: {
      dependency: DependencyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.dependency.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
