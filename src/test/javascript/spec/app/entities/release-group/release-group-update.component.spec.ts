import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleaseGroupUpdateComponent } from 'app/entities/release-group/release-group-update.component';
import { ReleaseGroupService } from 'app/entities/release-group/release-group.service';
import { ReleaseGroup } from 'app/shared/model/release-group.model';

describe('Component Tests', () => {
  describe('ReleaseGroup Management Update Component', () => {
    let comp: ReleaseGroupUpdateComponent;
    let fixture: ComponentFixture<ReleaseGroupUpdateComponent>;
    let service: ReleaseGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleaseGroupUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ReleaseGroupUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReleaseGroupUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReleaseGroupService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ReleaseGroup(123);
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
        const entity = new ReleaseGroup();
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
