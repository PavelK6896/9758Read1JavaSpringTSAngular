import {AuthService} from './auth.service';
import {TestBed} from "@angular/core/testing";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {of} from "rxjs";

describe('AuthService', () => {
    let service: AuthService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientModule]
        });
        service = TestBed.inject(AuthService);
    });

    it('1 init', () => {
        expect(service).toBeTruthy();
    });

    it('2 login', () => {
        let data2 = {username: '1', authenticationToken: "ssssss", refreshToken: "sss", expiresAt: "ss"}
        let httpClient = TestBed.inject(HttpClient);
        let spy = spyOn(httpClient, 'post');
        spy.and.returnValues(of(data2))

        let result = null
        service.loggedInEmitter.subscribe(v => result = v)
        let result2 = null
        service.usernameEmitter.subscribe(v => result2 = v)

        service.login({
            username: "string",
            password: "string"
        }).subscribe(data => {
            expect(data).toBe(true)
        })

        expect(localStorage.getItem("username")).toBe(data2.username)
        expect(localStorage.getItem("authenticationToken")).toBe(data2.authenticationToken)
        expect(localStorage.getItem("refreshToken")).toBe(data2.refreshToken)
        expect(localStorage.getItem("expiresAt")).toBe(data2.expiresAt)
        expect(result).toBe(true)
        expect(result2).toBe('1')
    });


    it('3 signUp', () => {
        let data1 = {
            username: "string",
            password: "string",
            email: "string"
        }
        let data2 = "User Registration Successful"
        let httpClient = TestBed.inject(HttpClient);
        let spy = spyOn(httpClient, 'post');
        spy.and.returnValues(of(data2))

        service.signUp(data1).subscribe(data => {
            expect(data).toBe(data2)
        })

        expect(spy).toHaveBeenCalledWith('http://localhost:8080/api/auth/signUp',
            data1, {responseType: 'text'})
    });

    it('4 refreshToken', () => {
        let data2 = {username: '1', authenticationToken: "asdsadasda", refreshToken: "asdasdas", expiresAt: "adsasdas"}
        let httpClient = TestBed.inject(HttpClient);
        let spy = spyOn(httpClient, 'post');
        spy.and.returnValues(of(data2))

        service.refreshToken()

        expect(spy).toHaveBeenCalledWith('http://localhost:8080/api/auth/refresh/token',
            {refreshToken: '', username: ''})

        expect(localStorage.getItem("authenticationToken")).toBe(data2.authenticationToken)
        expect(localStorage.getItem("expiresAt")).toBe(data2.expiresAt)
    });

    it('5 logout', () => {
        let data2 = "Refresh Token Deleted Successfully!"
        let httpClient = TestBed.inject(HttpClient);
        let spy = spyOn(httpClient, 'post');
        spy.and.returnValues(of(data2))

        service.logout()

        expect(spy).toHaveBeenCalledWith('http://localhost:8080/api/auth/logout',
            {refreshToken: '', username: ''}, {responseType: 'text'})
    });

});
