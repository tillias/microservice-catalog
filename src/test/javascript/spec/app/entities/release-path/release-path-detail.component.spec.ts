import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleasePathDetailComponent } from 'app/entities/release-path/release-path-detail.component';
import { ReleasePath } from 'app/shared/model/release-path.model';

describe('Component Tests', () => {
  describe('ReleasePath Management Detail Component', () => {
    let comp: ReleasePathDetailComponent;
    let fixture: ComponentFixture<ReleasePathDetailComponent>;
    const route = ({ data: of({ releasePath: new ReleasePath(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleasePathDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ReleasePathDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReleasePathDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load releasePath on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.releasePath).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
