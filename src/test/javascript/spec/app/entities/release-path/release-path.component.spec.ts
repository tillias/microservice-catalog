import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleasePathComponent } from 'app/entities/release-path/release-path.component';
import { ReleasePathService } from 'app/entities/release-path/release-path.service';
import { ReleasePath } from 'app/shared/model/release-path.model';

describe('Component Tests', () => {
  describe('ReleasePath Management Component', () => {
    let comp: ReleasePathComponent;
    let fixture: ComponentFixture<ReleasePathComponent>;
    let service: ReleasePathService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleasePathComponent],
      })
        .overrideTemplate(ReleasePathComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReleasePathComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReleasePathService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ReleasePath(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.releasePaths && comp.releasePaths[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
