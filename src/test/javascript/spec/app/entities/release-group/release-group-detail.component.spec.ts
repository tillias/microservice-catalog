import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleaseGroupDetailComponent } from 'app/entities/release-group/release-group-detail.component';
import { ReleaseGroup } from 'app/shared/model/release-group.model';

describe('Component Tests', () => {
  describe('ReleaseGroup Management Detail Component', () => {
    let comp: ReleaseGroupDetailComponent;
    let fixture: ComponentFixture<ReleaseGroupDetailComponent>;
    const route = ({ data: of({ releaseGroup: new ReleaseGroup(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleaseGroupDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ReleaseGroupDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReleaseGroupDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load releaseGroup on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.releaseGroup).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
