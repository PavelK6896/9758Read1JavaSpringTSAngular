import {ComponentFixture, TestBed} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {AuthService} from "../shared/auth.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {ToastrService} from "ngx-toastr";
import {of, Subject} from "rxjs";
import {By} from "@angular/platform-browser";
import {FormControl, FormGroup, Validators} from "@angular/forms";

class RouterStub {
    navigateByUrl(url: string) {
    }
}

class ActivatedRouteStub {
    private subject = new Subject<Params>();

    push(params: Params) {
        this.subject.next(params)
    }

    get queryParams() {
        return this.subject.asObservable()
    }
}

class ToastrServiceSub {
    success(message?: string) {
    }
}

describe('LoginComponent 10 ', () => {
    let component: LoginComponent;
    let fixture: ComponentFixture<LoginComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [LoginComponent],
            providers: [AuthService,
                {provide: Router, useClass: RouterStub},
                {provide: ActivatedRoute, useClass: ActivatedRouteStub},
                {provide: ToastrService, useClass: ToastrServiceSub},
            ],
            imports: [HttpClientModule]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(LoginComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('1 init', () => {
        expect(component).toBeTruthy();
    });

    it('2 activatedRoute queryParams registered', () => {
        let toast: ToastrServiceSub = TestBed.get(ToastrService)
        let route: ActivatedRouteStub = TestBed.get(ActivatedRoute)
        let spyToast = spyOn(toast, 'success')
        route.push({registered: 'true'})
        expect(spyToast).toHaveBeenCalledWith('Sign up Successful')
    });

    it('3 login', () => {
        component.loginForm = new FormGroup({
            username: new FormControl('dd', Validators.required),
            password: new FormControl('dd', Validators.required)
        });
        fixture.detectChanges();

        let auth = TestBed.inject(AuthService)
        let spyAuth = spyOn(auth, 'login').and.returnValue(of(true))
        let loginButton = fixture.debugElement.query(By.css('.loginButton'))
        let router = fixture.debugElement.injector.get(Router)
        let spyRouter = spyOn(router, 'navigateByUrl')

        fixture.detectChanges();
        loginButton.triggerEventHandler('click', null)
        expect(spyAuth).toHaveBeenCalledWith({username: 'dd', password: 'dd'})
        expect(spyRouter).toHaveBeenCalledWith('')
    });

});

