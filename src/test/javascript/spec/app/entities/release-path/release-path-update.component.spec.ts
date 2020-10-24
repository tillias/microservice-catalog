import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleasePathUpdateComponent } from 'app/entities/release-path/release-path-update.component';
import { ReleasePathService } from 'app/entities/release-path/release-path.service';
import { ReleasePath } from 'app/shared/model/release-path.model';

describe('Component Tests', () => {
  describe('ReleasePath Management Update Component', () => {
    let comp: ReleasePathUpdateComponent;
    let fixture: ComponentFixture<ReleasePathUpdateComponent>;
    let service: ReleasePathService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleasePathUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ReleasePathUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReleasePathUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReleasePathService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ReleasePath(123);
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
        const entity = new ReleasePath();
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
