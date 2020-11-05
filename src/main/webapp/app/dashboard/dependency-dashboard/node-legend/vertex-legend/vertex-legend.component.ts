import { Component, Input, OnInit } from '@angular/core';
import { ISelectPayload } from 'app/shared/vis/events/VisEvents';
import { IMicroservice } from 'app/shared/model/microservice.model';
import { MicroserviceService } from '../../../../entities/microservice/microservice.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'jhi-vertex-legend',
  templateUrl: './vertex-legend.component.html',
  styleUrls: ['./vertex-legend.component.scss'],
})
export class VertexLegendComponent implements OnInit {
  selection?: ISelectPayload;
  microservice?: IMicroservice;

  constructor(protected service: MicroserviceService) {}

  ngOnInit(): void {}

  @Input()
  set nodeSelection(selection: ISelectPayload) {
    this.selection = selection;

    if (selection && selection.hasNodes()) {
      this.service
        .find(selection.firstNode())
        .pipe(map(r => r.body || undefined))
        .subscribe(m => (this.microservice = m));
    }
  }
}
