import { Component, Input, OnInit } from '@angular/core';
import { IMicroservice } from 'app/shared/model/microservice.model';

@Component({
  selector: 'jhi-microservice-card',
  templateUrl: './microservice-card.component.html',
  styleUrls: ['./microservice-card.component.scss'],
})
export class MicroserviceCardComponent implements OnInit {
  @Input() microservice!: IMicroservice;

  constructor() {}

  ngOnInit(): void {}
}
