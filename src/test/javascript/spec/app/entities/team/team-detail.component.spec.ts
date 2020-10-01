import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { TeamDetailComponent } from 'app/entities/team/team-detail.component';
import { Team } from 'app/shared/model/team.model';

describe('Component Tests', () => {
  describe('Team Management Detail Component', () => {
    let comp: TeamDetailComponent;
    let fixture: ComponentFixture<TeamDetailComponent>;
    const route = ({ data: of({ team: new Team(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [TeamDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TeamDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TeamDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load team on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.team).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
