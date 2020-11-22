import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {AuthService} from './auth/shared/auth.service';
import {catchError, filter, switchMap, take} from 'rxjs/operators';
import {LoginResponse} from './auth/login/login-response.payload';

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptor implements HttpInterceptor {//Перехватчик

  isTokenRefreshing = false;
  //Поведение
  refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject(null);

  constructor(public authService: AuthService) {
  }


  //перехват
  intercept(req: HttpRequest<any>, next: HttpHandler):
    Observable<HttpEvent<any>> {

    //
    if (req.url.indexOf('refresh') !== -1 || req.url.indexOf('login') !== -1) {
      return next.handle(req);
    }
    const jwtToken = this.authService.getJwtToken(); // получаем токен из стора блаузера

    if (jwtToken) {//если токен есть
      //вызыв метода добавляет токен в хедер

      return next.handle(this.addToken(req, jwtToken))
        .pipe(
          //если токен невалиден
          catchError(error => {
              if (error instanceof HttpErrorResponse && error.status === 403) {
                //пробует обнавить токен
                return this.handleAuthErrors(req, next);
              } else {
                return throwError(error);
              }
            }
          ));
    }

    //вызывает метод
    return next.handle(req);

  }


  //справляться
  private handleAuthErrors(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    //если  рефрешь токен
    if (!this.isTokenRefreshing) {
      this.isTokenRefreshing = true;
      this.refreshTokenSubject.next(null);

      //переплучить токен
      return this.authService.refreshToken().pipe(
        switchMap((refreshTokenResponse: LoginResponse) => {
          this.isTokenRefreshing = false;
          this.refreshTokenSubject.next(refreshTokenResponse.authenticationToken);

          return next.handle(this.addToken(req, refreshTokenResponse.authenticationToken));
        })
      )
    } else {
      return this.refreshTokenSubject.pipe(
        filter(result => result !== null),
        //брать
        take(1),
        //переключатель
        switchMap((res) => {
            return next.handle(this.addToken(req, this.authService.getJwtToken()))
          }
        )
      );
    }
  }


  //добавить токен в заголовки
  addToken(req: HttpRequest<any>, jwtToken: any) {
    return req.clone({
      headers: req.headers.set('Authorization', 'Bearer ' + jwtToken)
    });
  }

}
