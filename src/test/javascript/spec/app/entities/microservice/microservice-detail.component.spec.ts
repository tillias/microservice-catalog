import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { MicrocatalogTestModule } from '../../../test.module';
import { MicroserviceDetailComponent } from 'app/entities/microservice/microservice-detail.component';
import { Microservice } from 'app/shared/model/microservice.model';

describe('Component Tests', () => {
  describe('Microservice Management Detail Component', () => {
    let comp: MicroserviceDetailComponent;
    let fixture: ComponentFixture<MicroserviceDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ microservice: new Microservice(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [MicroserviceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MicroserviceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MicroserviceDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load microservice on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.microservice).toEqual(jasmine.objectContaining({ id: 123 }));
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
