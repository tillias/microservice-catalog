import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITeam, Team } from 'app/shared/model/team.model';
import { TeamService } from './team.service';
import { TeamComponent } from './team.component';
import { TeamDetailComponent } from './team-detail.component';
import { TeamUpdateComponent } from './team-update.component';

@Injectable({ providedIn: 'root' })
export class TeamResolve implements Resolve<ITeam> {
  constructor(private service: TeamService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITeam> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((team: HttpResponse<Team>) => {
          if (team.body) {
            return of(team.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Team());
  }
}

export const teamRoute: Routes = [
  {
    path: '',
    component: TeamComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.team.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TeamDetailComponent,
    resolve: {
      team: TeamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.team.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TeamUpdateComponent,
    resolve: {
      team: TeamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.team.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TeamUpdateComponent,
    resolve: {
      team: TeamResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'microcatalogApp.team.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
