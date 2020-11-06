import { Component, Input, OnInit } from '@angular/core';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { CardSettings, ICardSettings } from 'app/entities/microservice/microservice-dashboard/microservice-card/card-settings.model';
import { EXPERIMENTAL_FEATURE } from 'app/app.constants';

@Component({
  selector: 'jhi-microservice-card',
  templateUrl: './microservice-card.component.html',
})
export class MicroserviceCardComponent implements OnInit {
  @Input() microservice!: IMicroservice;
  @Input() settings: ICardSettings = CardSettings.DEFAULT;

  experimentalFeatures = EXPERIMENTAL_FEATURE;

  constructor() {}

  ngOnInit(): void {
    this.assertInputsProvided();
  }

  private assertInputsProvided(): void {
    if (!this.microservice || !this.settings) {
      throw new Error('The required inputs [microservice] or [cardSettings] are not provided');
    }
  }
}
