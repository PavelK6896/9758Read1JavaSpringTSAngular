import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {SignupRequestPayload} from "../signup/singup-request.payload";
import {LoginRequestPayload} from "../login/login-request.payload";
import {LoginResponse} from "../login/login-response.payload";
import {map, tap} from "rxjs/operators";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    //@Output () - функция декоратора, маркирующая свойство как путь для данных, чтобы отправиться от ребенка на родитель.
    @Output() loggedIn: EventEmitter<boolean> = new EventEmitter();
    @Output() username: EventEmitter<string> = new EventEmitter();

    refreshTokenPayload = {
        refreshToken: this.getRefreshToken(),
        username: this.getUserName()
    }

    constructor(private httpClient: HttpClient) {
    }

    signup(signupRequestPayload: SignupRequestPayload): Observable<any> {
        return this.httpClient.post('http://localhost:8080/api/auth/signUp',
            signupRequestPayload, {responseType: 'text'});
    }

    //сохроняем даные пользователя
    login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
        return this.httpClient.post<LoginResponse>('http://localhost:8080/api/auth/login',
            loginRequestPayload).pipe(map(data => {


            console.log("login", data)
            localStorage.setItem('authenticationToken', data.authenticationToken);
            localStorage.setItem('username', data.username);
            localStorage.setItem('refreshToken', data.refreshToken);
            localStorage.setItem('expiresAt', data.expiresAt.toString());

            this.loggedIn.emit(true);
            this.username.emit(data.username);
            return true;
        }));
    }


    refreshToken() {
        return this.httpClient.post<LoginResponse>('http://localhost:8080/api/auth/refresh/token',
            this.refreshTokenPayload)
            //труба//кран
            .pipe(tap(response => {
                    localStorage.removeItem('authenticationToken');
                    localStorage.removeItem('expiresAt');
                    //обновляем токен
                    localStorage.setItem('authenticationToken', response.authenticationToken);
                    localStorage.setItem('expiresAt', response.expiresAt.toString());
                }
            ));
    }

    logout() {
        this.httpClient.post('http://localhost:8080/api/auth/logout', this.refreshTokenPayload,
            {responseType: 'text'})
            .subscribe(data => {
                console.log(data);
            }, error => {
                throwError(error);
            })
        localStorage.removeItem('authenticationToken');
        localStorage.removeItem('username');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('expiresAt');
    }

    getUserName() {
        return localStorage.getItem('username');
    }

    getRefreshToken() {
        return localStorage.getItem('refreshToken');
    }

    getJwtToken() {
        return localStorage.getItem('authenticationToken');
    }

    isLoggedIn(): boolean {
        return this.getJwtToken() != null;
    }
}
