import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { DependencyUpdateComponent } from 'app/entities/dependency/dependency-update.component';
import { DependencyService } from 'app/entities/dependency/dependency.service';
import { Dependency } from 'app/shared/model/dependency.model';

describe('Component Tests', () => {
  describe('Dependency Management Update Component', () => {
    let comp: DependencyUpdateComponent;
    let fixture: ComponentFixture<DependencyUpdateComponent>;
    let service: DependencyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [DependencyUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DependencyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DependencyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DependencyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Dependency(123);
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
        const entity = new Dependency();
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
