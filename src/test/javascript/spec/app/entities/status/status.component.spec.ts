import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MicrocatalogTestModule } from '../../../test.module';
import { StatusComponent } from 'app/entities/status/status.component';
import { StatusService } from 'app/entities/status/status.service';
import { Status } from 'app/shared/model/status.model';

describe('Component Tests', () => {
  describe('Status Management Component', () => {
    let comp: StatusComponent;
    let fixture: ComponentFixture<StatusComponent>;
    let service: StatusService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [StatusComponent],
      })
        .overrideTemplate(StatusComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(StatusComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(StatusService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Status(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.statuses && comp.statuses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
