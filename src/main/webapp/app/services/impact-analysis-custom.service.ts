import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';
import { IResult } from 'app/shared/model/impact/analysis/result.model';

type EntityResponseType = HttpResponse<IResult>;

@Injectable({
  providedIn: 'root',
})
export class ImpactAnalysisCustomService {
  public resourceUrl = SERVER_API_URL + 'api/impact-analysis';

  constructor(protected http: HttpClient) {}

  find(microserviceId: number): Observable<EntityResponseType> {
    return this.http
      .get<IResult>(`${this.resourceUrl}/microservice/${microserviceId}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? moment(res.body.createdOn) : undefined;
    }
    return res;
  }
}
