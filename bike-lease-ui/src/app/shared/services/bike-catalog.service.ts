import { Injectable } from '@angular/core';
import { BikeCatalog } from '../models/bike-catalog.model';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { PageResponse } from '../models/page-response';

@Injectable({
  providedIn: 'root',
})
export class BikeCatalogService {
  
  private readonly apiUrl =
    `${environment.bikeCatalogApiUrl}/bikeCatalog`;

  constructor(
    private http: HttpClient
  ) {}

  getAllBikes(pageIndex: number, pageSize: number): Observable<PageResponse<BikeCatalog>> {
    const params = new HttpParams()
    .set('page', pageIndex.toString())
    .set('size', pageSize.toString());

    console.log('Fetching bikes with params:', params.toString());

  return this.http.get<PageResponse<BikeCatalog>>(
    this.apiUrl,
    { params }
  );
  }

  getBike(
    id: number
  ): Observable<BikeCatalog> {

    return this.http.get<BikeCatalog>(
      `${this.apiUrl}/${id}`
    );
  }
}
