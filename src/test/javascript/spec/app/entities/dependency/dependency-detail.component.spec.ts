import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { MicrocatalogTestModule } from '../../../test.module';
import { DependencyDetailComponent } from 'app/entities/dependency/dependency-detail.component';
import { Dependency } from 'app/shared/model/dependency.model';

describe('Component Tests', () => {
  describe('Dependency Management Detail Component', () => {
    let comp: DependencyDetailComponent;
    let fixture: ComponentFixture<DependencyDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ dependency: new Dependency(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [DependencyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DependencyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DependencyDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load dependency on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.dependency).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
