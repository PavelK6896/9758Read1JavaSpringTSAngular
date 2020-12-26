import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {AuthService} from "../auth/shared/auth.service";
import {Router} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {By} from "@angular/platform-browser";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {HttpClientTestingModule} from "@angular/common/http/testing";


class RouterStub {
    navigate(path: string[]) {
    }

    navigateByUrl(url: string) {
    }
}

describe('HeaderComponent 2', () => {
    let component: HeaderComponent;
    let fixture: ComponentFixture<HeaderComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [HeaderComponent],
            providers: [AuthService,
                {provide: Router, useClass: RouterStub}
            ],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(HeaderComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });


    it(' 1 init', () => {
        expect(component).toBeTruthy();
    });


    //проверка изменения имени
    it(' 2 username', () => {

        //изменения в шаблоне
        component.isLoggedIn = true
        component.username = 'user1'
        fixture.detectChanges()
        let debugElement = fixture.debugElement.query(By.css('#dropdownBasic1'));
        expect(debugElement.nativeElement.textContent).toContain('user1')
    });


    //провека кнопки профиля
    it(' 3 goToUserProfile right', () => {

        let userName = 'user1'
        component.isLoggedIn = true
        component.username = userName
        fixture.detectChanges()

        let goToUserProfile = fixture.debugElement.query(By.css('#goToUserProfile'))

        let router = fixture.debugElement.injector.get(Router)
        let spy = spyOn(router, 'navigateByUrl')

        //прожимем кнопку
        goToUserProfile.triggerEventHandler('click', null)
        //сробатывает роутинг
        expect(spy).toHaveBeenCalledWith('/user-profile/' + userName)

    });

    //проверка кнопок меню
    it(' 4 logout', () => {

        let userName = 'user1'
        component.isLoggedIn = true
        component.username = userName
        fixture.detectChanges()

        let logout = fixture.debugElement.query(By.css('#logout'))

        let router = fixture.debugElement.injector.get(Router)
        let spy = spyOn(router, 'navigateByUrl')

        //получаем сервис
        let authService = fixture.debugElement.injector.get(AuthService)
        //мокаем метод сервиса
        let authServiceSpy = spyOn(authService, 'logout')

        //прожимем кнопку
        logout.triggerEventHandler('click', null)
        //сробатывает роутинг
        expect(spy).toHaveBeenCalledWith('')
        expect(component.isLoggedIn).toBeFalse()

        expect(authServiceSpy).toHaveBeenCalled();
        expect(authServiceSpy).toHaveBeenCalledWith();
        //вызван 1 раз
        expect(authServiceSpy.calls.count()).toBe(1)
    });

});
