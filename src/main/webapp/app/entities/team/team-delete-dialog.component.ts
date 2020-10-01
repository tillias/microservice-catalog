import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITeam } from 'app/shared/model/team.model';
import { TeamService } from './team.service';

@Component({
  templateUrl: './team-delete-dialog.component.html',
})
export class TeamDeleteDialogComponent {
  team?: ITeam;

  constructor(protected teamService: TeamService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.teamService.delete(id).subscribe(() => {
      this.eventManager.broadcast('teamListModification');
      this.activeModal.close();
    });
  }
}
