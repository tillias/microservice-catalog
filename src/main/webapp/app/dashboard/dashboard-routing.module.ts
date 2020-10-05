import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'dependencies',
        loadChildren: () => import('./dependency-dashboard/dependency-dashboard.module').then(m => m.DependencyDashboardModule),
      },
    ]),
  ],
})
export class DashboardRoutingModule {}
