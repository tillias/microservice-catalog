import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleaseStepComponent } from 'app/entities/release-step/release-step.component';
import { ReleaseStepService } from 'app/entities/release-step/release-step.service';
import { ReleaseStep } from 'app/shared/model/release-step.model';

describe('Component Tests', () => {
  describe('ReleaseStep Management Component', () => {
    let comp: ReleaseStepComponent;
    let fixture: ComponentFixture<ReleaseStepComponent>;
    let service: ReleaseStepService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleaseStepComponent],
      })
        .overrideTemplate(ReleaseStepComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReleaseStepComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReleaseStepService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ReleaseStep(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.releaseSteps && comp.releaseSteps[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
