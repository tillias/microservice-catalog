import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MicroserviceGroupFilter, IMicroserviceGroupFilter } from 'app/shared/model/util/microservice-group-filter';

@Component({
  selector: 'jhi-microservice-group-filter',
  templateUrl: './microservice-group-filter.component.html',
  styleUrls: ['./microservice-group-filter.component.scss'],
})
export class MicroserviceGroupFilterComponent implements OnInit {
  groupFilter: IMicroserviceGroupFilter;

  @Output() filterChanged = new EventEmitter<IMicroserviceGroupFilter>();

  constructor() {
    this.groupFilter = new MicroserviceGroupFilter();
  }

  ngOnInit(): void {}

  onSearch(): void {
    this.filterChanged.emit(this.groupFilter);
  }
}
