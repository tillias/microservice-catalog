import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap } from 'rxjs/operators';
import { MicroserviceService } from 'app/entities/microservice/microservice.service';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { IMicroserviceGroupFilter } from 'app/shared/model/util/microservice-group-filter';

@Component({
  selector: 'jhi-microservice-search',
  templateUrl: './microservice-search.component.html',
})
export class MicroserviceSearchComponent implements OnInit {
  model: any;

  /**
   * Enables advanced search capabilities. Default: false
   */
  @Input() advanced = false;
  @Output() itemSelected = new EventEmitter<IMicroservice>();
  @Output() groupFilterChanged = new EventEmitter<IMicroserviceGroupFilter>();

  constructor(protected microserviceService: MicroserviceService) {}

  ngOnInit(): void {}

  @Input()
  set selectedValue(value: IMicroservice) {
    this.model = value;
  }

  search = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(500),
      distinctUntilChanged(),

      switchMap(searchText => (searchText.length < 2 ? [] : this.loadData(searchText)))
    );

  loadData(searchText: string): Observable<IMicroservice[]> {
    return this.microserviceService
      .query()
      .pipe(map(response => response.body!.filter(m => m.name!.toLowerCase().includes(searchText.toLowerCase())) || [{}]));
  }

  formatter = (result: IMicroservice) => result.name || '';

  inputFormatter = (result: IMicroservice) => result.name || '';

  onItemSelected(item: IMicroservice): any {
    this.itemSelected.emit(item);
  }

  clear(): any {
    this.model = '';
    this.itemSelected.emit();
  }

  onModelChanged(): any {
    if (!this.model) {
      this.itemSelected.emit();
    }
  }

  onFilterChanged(groupFilter: IMicroserviceGroupFilter): any {
    this.groupFilterChanged.emit(groupFilter);
  }
}
