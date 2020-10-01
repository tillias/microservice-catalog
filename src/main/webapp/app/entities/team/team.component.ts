import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from './team.service';
import { TeamDeleteDialogComponent } from './team-delete-dialog.component';

@Component({
  selector: 'jhi-team',
  templateUrl: './team.component.html',
})
export class TeamComponent implements OnInit, OnDestroy {
  teams?: ITeam[];
  eventSubscriber?: Subscription;

  constructor(protected teamService: TeamService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.teamService.query().subscribe((res: HttpResponse<ITeam[]>) => (this.teams = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInTeams();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITeam): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInTeams(): void {
    this.eventSubscriber = this.eventManager.subscribe('teamListModification', () => this.loadAll());
  }

  delete(team: ITeam): void {
    const modalRef = this.modalService.open(TeamDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.team = team;
  }
}
