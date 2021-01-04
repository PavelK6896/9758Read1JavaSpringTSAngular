import {EventEmitter, Injectable, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable, throwError} from "rxjs";
import {SignupRequestPayload} from "../signup/singup-request.payload";
import {LoginRequestPayload} from "../login/login-request.payload";
import {LoginResponse} from "../login/login-response.payload";
import {map, tap} from "rxjs/operators";
import {environment} from "../../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    @Output() loggedInEmitter: EventEmitter<boolean> = new EventEmitter();
    @Output() usernameEmitter: EventEmitter<string> = new EventEmitter();

    refreshTokenPayload = {
        refreshToken: this.getRefreshToken(),
        username: this.getUserName()
    }

    constructor(private httpClient: HttpClient) {
    }

    signUp(signUpRequestPayload: SignupRequestPayload): Observable<any> {
        return this.httpClient
            .post(environment.URL +'/api/auth/signUp', signUpRequestPayload, {responseType: 'text'});
    }

    login(loginRequestPayload: LoginRequestPayload): Observable<boolean> {
        return this.httpClient.post<LoginResponse>(environment.URL +'/api/auth/login', loginRequestPayload)
            .pipe(map(data => {
                localStorage.setItem('authenticationToken', data.authenticationToken);
                localStorage.setItem('username', data.username);
                localStorage.setItem('refreshToken', data.refreshToken);
                localStorage.setItem('expiresAt', data.expiresAt.toString());
                this.loggedInEmitter.emit(true);
                this.usernameEmitter.emit(data.username);
                return true;
            }));
    }


    refreshToken(): Observable<any> {
        return this.httpClient.post<LoginResponse>(environment.URL +'/api/auth/refresh/token', this.refreshTokenPayload)
            .pipe(tap(response => {
                    localStorage.removeItem('authenticationToken');
                    localStorage.removeItem('expiresAt');
                    localStorage.setItem('authenticationToken', response.authenticationToken);
                    localStorage.setItem('expiresAt', response.expiresAt.toString());
                }
            ));
    }

    logout() {
        this.httpClient.post(environment.URL +'/api/auth/logout', this.refreshTokenPayload, {responseType: 'text'})
            .subscribe(data => {

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
