import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'microservice',
        loadChildren: () => import('./microservice/microservice.module').then(m => m.MicrocatalogMicroserviceModule),
      },
      {
        path: 'team',
        loadChildren: () => import('./team/team.module').then(m => m.MicrocatalogTeamModule),
      },
      {
        path: 'status',
        loadChildren: () => import('./status/status.module').then(m => m.MicrocatalogStatusModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class MicrocatalogEntityModule {}
