import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { MicroserviceService } from 'app/entities/microservice/microservice.service';
import { HttpResponse } from '@angular/common/http';
import { IMicroservice } from 'app/shared/model/microservice.model';

@Component({
  selector: 'jhi-microservice-search',
  templateUrl: './microservice-search.component.html',
  styleUrls: ['./microservice-search.component.scss'],
})
export class MicroserviceSearchComponent implements OnInit {
  constructor(protected microserviceService: MicroserviceService) {}

  search = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),

      switchMap(searchText => (searchText.length < 2 ? [] : this.microserviceService.query()))
    );

  microserviceFormatter = (result: HttpResponse<IMicroservice>) => result?.body?.name;
  microserviceInputFormatter = (result: HttpResponse<IMicroservice>) => result?.body?.name;

  ngOnInit(): void {}
}
