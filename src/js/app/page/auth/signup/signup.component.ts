import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ToastrService} from "ngx-toastr";

import {Subscription} from "rxjs";
import {SignupRequestPayload} from "../../../utill/interface1";
import {AuthService} from "../../../service/auth.service";
import {logUtil} from "../../../utill/log1";

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit, OnDestroy {

    signUpRequestPayload: SignupRequestPayload;
    signUpForm: FormGroup;
    signUpSubscription: Subscription

    constructor(private authService: AuthService,
                private router: Router,
                private toastrService: ToastrService) {
        logUtil("SignupComponent!")
        this.signUpRequestPayload = {
            username: '',
            email: '',
            password: ''
        };
    }

    ngOnInit() {
        this.signUpForm = new FormGroup({
            username: new FormControl('', Validators.required),
            email: new FormControl('', [Validators.required, Validators.email]),
            password: new FormControl('', Validators.required),
        });
    }

    ngOnDestroy(): void {
        if (this.signUpSubscription) {
            this.signUpSubscription.unsubscribe()
        }
    }

    signUp() {

        this.signUpRequestPayload.email = this.signUpForm.get('email').value;
        this.signUpRequestPayload.username = this.signUpForm.get('username').value;
        this.signUpRequestPayload.password = this.signUpForm.get('password').value;

        this.signUpSubscription = this.authService.signUp(this.signUpRequestPayload)
            .subscribe(data => {
                logUtil("signUp+ ", data)
                this.toastrService.success('success');
                this.router.navigate(['/login'], {queryParams: {registered: 'true'}});
            }, error => {
                logUtil("signUp- ", error)
                this.toastrService.error('Registration Failed! Please try again');
            });
    }

}
