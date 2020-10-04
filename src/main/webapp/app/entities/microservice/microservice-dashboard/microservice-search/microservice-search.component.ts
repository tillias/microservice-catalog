import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap } from 'rxjs/operators';
import { MicroserviceService } from 'app/entities/microservice/microservice.service';
import { IMicroservice } from 'app/shared/model/microservice.model';

@Component({
  selector: 'jhi-microservice-search',
  templateUrl: './microservice-search.component.html',
  styleUrls: ['./microservice-search.component.scss'],
})
export class MicroserviceSearchComponent implements OnInit {
  model: any;

  @Output() itemSelected = new EventEmitter<IMicroservice>();

  constructor(protected microserviceService: MicroserviceService) {}

  ngOnInit(): void {}

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
}
