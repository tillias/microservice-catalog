import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MicrocatalogTestModule } from '../../../test.module';
import { ReleaseGroupComponent } from 'app/entities/release-group/release-group.component';
import { ReleaseGroupService } from 'app/entities/release-group/release-group.service';
import { ReleaseGroup } from 'app/shared/model/release-group.model';

describe('Component Tests', () => {
  describe('ReleaseGroup Management Component', () => {
    let comp: ReleaseGroupComponent;
    let fixture: ComponentFixture<ReleaseGroupComponent>;
    let service: ReleaseGroupService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [ReleaseGroupComponent],
      })
        .overrideTemplate(ReleaseGroupComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReleaseGroupComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ReleaseGroupService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new ReleaseGroup(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.releaseGroups && comp.releaseGroups[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
