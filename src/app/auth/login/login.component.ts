import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginRequestPayload} from "./login-request.payload";
import {AuthService} from "../shared/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Subscription, throwError} from "rxjs";
import {ToastrService} from "ngx-toastr";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

    loginForm: FormGroup;
    loginRequestPayload: LoginRequestPayload;
    registerSuccessMessage: string;
    isError: boolean;
    queryParamsSubscription: Subscription
    loginSubscription: Subscription

    constructor(private authService: AuthService, private activatedRoute: ActivatedRoute,
                private router: Router, private toastrService: ToastrService) {

        this.loginRequestPayload = {
            username: '',
            password: ''
        };
    }

    ngOnInit(): void {

        this.loginForm = new FormGroup({
            username: new FormControl('', Validators.required),
            password: new FormControl('', Validators.required)
        });

        this.queryParamsSubscription = this.activatedRoute.queryParams
            .subscribe(params => {
                if (params.registered !== undefined && params.registered === 'true') {
                    this.toastrService.success('Sign up Successful');
                    this.registerSuccessMessage = 'Please Check your inbox for activation email '
                        + 'activate your account before you Login!';
                }
            });
    }

    ngOnDestroy(): void {
        if (this.queryParamsSubscription) {
            this.queryParamsSubscription.unsubscribe()
        }
        if (this.loginSubscription) {
            this.loginSubscription.unsubscribe()
        }
    }

    login() {

        this.loginRequestPayload.username = this.loginForm.get('username').value;
        this.loginRequestPayload.password = this.loginForm.get('password').value;

        this.loginSubscription = this.authService.login(this.loginRequestPayload).subscribe(data => {
            this.isError = false;
            this.router.navigateByUrl('');
            this.toastrService.success('Login Successful', 'Info', {
                timeOut: 500,
            });
        }, error => {
            this.isError = true;

            this.toastrService.error('Login Error', 'Error', {
                timeOut: 1000,
            });
            throwError(error);
        });
    }
}
