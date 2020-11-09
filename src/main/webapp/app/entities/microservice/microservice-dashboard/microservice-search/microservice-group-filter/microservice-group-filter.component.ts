import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { IMicroserviceGroupFilter, MicroserviceGroupFilter } from 'app/shared/model/util/microservice-group-filter';
import { TeamService } from 'app/entities/team/team.service';
import { StatusService } from 'app/entities/status/status.service';
import { ITeam } from 'app/shared/model/team.model';
import { IStatus } from 'app/shared/model/status.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-microservice-group-filter',
  templateUrl: './microservice-group-filter.component.html',
})
export class MicroserviceGroupFilterComponent implements OnInit {
  groupFilter: IMicroserviceGroupFilter;
  teams: ITeam[];
  statuses: IStatus[];

  @Output() filterChanged = new EventEmitter<IMicroserviceGroupFilter>();

  constructor(private teamService: TeamService, private statusService: StatusService) {
    this.groupFilter = new MicroserviceGroupFilter();
    this.teams = [];
    this.statuses = [];
  }

  ngOnInit(): void {
    this.loadItems();
  }

  loadItems(): void {
    this.teamService.query().subscribe((res: HttpResponse<ITeam[]>) => (this.teams = res.body || []));
    this.statusService.query().subscribe((res: HttpResponse<IStatus[]>) => (this.statuses = res.body || []));
  }

  onSearch(): void {
    this.filterChanged.emit(this.groupFilter);
  }

  onClearFilter(): void {
    this.groupFilter = new MicroserviceGroupFilter();
    this.onSearch();
  }
}
