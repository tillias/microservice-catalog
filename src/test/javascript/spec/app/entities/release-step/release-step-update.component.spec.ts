import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleaseStepUpdateComponent } from 'app/entities/release-step/release-step-update.component';
import { ReleaseStepService } from 'app/entities/release-step/release-step.service';
import { ReleaseStep } from 'app/shared/model/release-step.model';

describe('Component Tests', () => {
  describe('ReleaseStep Management Update Component', () => {
    let comp: ReleaseStepUpdateComponent;
    let fixture: ComponentFixture<ReleaseStepUpdateComponent>;
    let service: ReleaseStepService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleaseStepUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ReleaseStepUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReleaseStepUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReleaseStepService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ReleaseStep(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ReleaseStep();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
