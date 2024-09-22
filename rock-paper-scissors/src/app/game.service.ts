import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GameService {

  private apiUrl = 'http://localhost:8080/game';  // Backend URL

  constructor(private http: HttpClient) {}

  startGame(): Observable<any> {
    return this.http.post(this.apiUrl + '/start', {}, { responseType: 'json', withCredentials: true });
  }

  makeMove(move: string): Observable<any> {
    const body = { move };
    return this.http.post(this.apiUrl + '/move', body, { responseType: 'json', withCredentials: true });
  }

  terminateGame(): Observable<any> {
    return this.http.post(this.apiUrl + '/terminate', {}, { responseType: 'json', withCredentials: true });
  }
}
