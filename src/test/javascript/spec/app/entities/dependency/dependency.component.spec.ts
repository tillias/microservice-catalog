import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MicrocatalogTestModule } from '../../../test.module';
import { DependencyComponent } from 'app/entities/dependency/dependency.component';
import { DependencyService } from 'app/entities/dependency/dependency.service';
import { Dependency } from 'app/shared/model/dependency.model';

describe('Component Tests', () => {
  describe('Dependency Management Component', () => {
    let comp: DependencyComponent;
    let fixture: ComponentFixture<DependencyComponent>;
    let service: DependencyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MicrocatalogTestModule],
        declarations: [DependencyComponent],
      })
        .overrideTemplate(DependencyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DependencyComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DependencyService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Dependency(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.dependencies && comp.dependencies[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
