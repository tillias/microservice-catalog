import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { MicroserviceUpdateComponent } from 'app/entities/microservice/microservice-update.component';
import { MicroserviceService } from 'app/entities/microservice/microservice.service';
import { Microservice } from 'app/shared/model/microservice.model';

describe('Component Tests', () => {
  describe('Microservice Management Update Component', () => {
    let comp: MicroserviceUpdateComponent;
    let fixture: ComponentFixture<MicroserviceUpdateComponent>;
    let service: MicroserviceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [MicroserviceUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(MicroserviceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MicroserviceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MicroserviceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Microservice(123);
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
        const entity = new Microservice();
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
