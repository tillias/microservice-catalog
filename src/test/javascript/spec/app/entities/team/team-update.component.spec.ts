import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { TeamUpdateComponent } from 'app/entities/team/team-update.component';
import { TeamService } from 'app/entities/team/team.service';
import { Team } from 'app/shared/model/team.model';

describe('Component Tests', () => {
  describe('Team Management Update Component', () => {
    let comp: TeamUpdateComponent;
    let fixture: ComponentFixture<TeamUpdateComponent>;
    let service: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [TeamUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TeamUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TeamService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Team(123);
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
        const entity = new Team();
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
