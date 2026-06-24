import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LeaseRequest } from '../models/lease-request.model';
import { Observable } from 'rxjs';
import { LeaseResponse } from '../models/lease-response.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class LeaseService {
  private leaseUrl = `${environment.bikeCatalogApiUrl}/lease`;
  
  constructor(
    private http: HttpClient
  ) {}

  applyLease(request: LeaseRequest): Observable<LeaseResponse> {
    console.log("lease request : ", request, " url : ", this.leaseUrl);
    return this.http.post<LeaseResponse>(
      this.leaseUrl,
      request
    );
  }
}