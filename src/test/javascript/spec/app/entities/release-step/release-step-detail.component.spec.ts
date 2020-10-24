import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleaseStepDetailComponent } from 'app/entities/release-step/release-step-detail.component';
import { ReleaseStep } from 'app/shared/model/release-step.model';

describe('Component Tests', () => {
  describe('ReleaseStep Management Detail Component', () => {
    let comp: ReleaseStepDetailComponent;
    let fixture: ComponentFixture<ReleaseStepDetailComponent>;
    const route = ({ data: of({ releaseStep: new ReleaseStep(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleaseStepDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ReleaseStepDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReleaseStepDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load releaseStep on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.releaseStep).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
