import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IReleasePath } from 'app/shared/model/release-path.model';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

type EntityResponseType = HttpResponse<IReleasePath>;

@Injectable({
  providedIn: 'root',
})
export class ReleasePathCustomService {
  public resourceUrl = SERVER_API_URL + 'api/release-path';

  constructor(protected http: HttpClient) {}

  find(microserviceId: number): Observable<EntityResponseType> {
    return this.http
      .get<IReleasePath>(`${this.resourceUrl}/microservice/${microserviceId}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? moment(res.body.createdOn) : undefined;
    }
    return res;
  }
}
