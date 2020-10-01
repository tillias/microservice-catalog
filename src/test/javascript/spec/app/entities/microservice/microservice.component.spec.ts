import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MicrocatalogTestModule } from '../../../test.module';
import { MicroserviceComponent } from 'app/entities/microservice/microservice.component';
import { MicroserviceService } from 'app/entities/microservice/microservice.service';
import { Microservice } from 'app/shared/model/microservice.model';

describe('Component Tests', () => {
  describe('Microservice Management Component', () => {
    let comp: MicroserviceComponent;
    let fixture: ComponentFixture<MicroserviceComponent>;
    let service: MicroserviceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [MicroserviceComponent],
      })
        .overrideTemplate(MicroserviceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MicroserviceComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MicroserviceService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Microservice(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.microservices && comp.microservices[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
