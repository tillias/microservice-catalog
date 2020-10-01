import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITeam } from 'app/shared/model/team.model';

@Component({
  selector: 'jhi-team-detail',
  templateUrl: './team-detail.component.html',
})
export class TeamDetailComponent implements OnInit {
  team: ITeam | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ team }) => (this.team = team));
  }

  previousState(): void {
    window.history.back();
  }
}
