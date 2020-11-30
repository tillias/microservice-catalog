import { Injectable } from '@angular/core';
import { SERVER_API_URL } from '../app.constants';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class IntegrationTestsService {
  public resourceUrl = SERVER_API_URL + 'api/integration-tests';

  constructor(protected http: HttpClient) {}

  trigger(microserviceId: number): Observable<any> {
    return this.http.post(`${this.resourceUrl}/microservice/${microserviceId}`, { observe: 'response' });
  }
}
