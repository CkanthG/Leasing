import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LeaseRequest } from '../models/lease-request.model';
import { Observable } from 'rxjs';
import { LeaseResponse } from '../models/lease-response.model';
import { environment } from '../../../environments/environment';
import { PageResponse } from '../models/page-response';

@Injectable({
  providedIn: 'root',
})
export class LeaseService {
  private leaseUrl = `${environment.bikeCatalogApiUrl}/lease`;
  
  constructor(
    private http: HttpClient
  ) {}

  applyLease(request: LeaseRequest): Observable<LeaseResponse> {
    return this.http.post<LeaseResponse>(
      this.leaseUrl,
      request
    );
  }

  getAllLeases(pageIndex: number, pageSize: number): Observable<PageResponse<LeaseResponse>> {
    const params = new HttpParams()
    .set('page', pageIndex.toString())
    .set('size', pageSize.toString());
    
    return this.http.get<PageResponse<LeaseResponse>>(
      this.leaseUrl,
    { params }
    )
  }
}