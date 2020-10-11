import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMicroservice, Microservice } from 'app/shared/model/microservice.model';
import { MicroserviceService } from './microservice.service';
import { MicroserviceComponent } from './microservice.component';
import { MicroserviceDetailComponent } from './microservice-detail.component';
import { MicroserviceUpdateComponent } from './microservice-update.component';
import { MicroserviceCustomDetailsComponent } from './microservice-details/microservice-custom-details.component';

@Injectable({ providedIn: 'root' })
export class MicroserviceResolve implements Resolve<IMicroservice> {
  constructor(private service: MicroserviceService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMicroservice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((microservice: HttpResponse<Microservice>) => {
          if (microservice.body) {
            return of(microservice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Microservice());
  }
}

export const microserviceRoute: Routes = [
  {
    path: '',
    component: MicroserviceComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.microservice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MicroserviceDetailComponent,
    resolve: {
      microservice: MicroserviceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.microservice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MicroserviceUpdateComponent,
    resolve: {
      microservice: MicroserviceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.microservice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MicroserviceUpdateComponent,
    resolve: {
      microservice: MicroserviceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.microservice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/details/custom',
    component: MicroserviceCustomDetailsComponent,
    resolve: {
      microservice: MicroserviceResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.microservice.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
