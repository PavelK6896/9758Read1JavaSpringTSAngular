import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {SignupRequestPayload} from "./singup-request.payload";
import {AuthService} from "../shared/auth.service";
import {ToastrService} from "ngx-toastr";

@Component({
    selector: 'app-signup',
    templateUrl: './signup.component.html',
    styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

    signUpRequestPayload: SignupRequestPayload;
    signUpForm: FormGroup; // форма для регистрации

    constructor(private authService: AuthService, private router: Router, private toastrService: ToastrService) {
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

    signUp() {

        this.signUpRequestPayload.email = this.signUpForm.get('email').value;
        this.signUpRequestPayload.username = this.signUpForm.get('username').value;
        this.signUpRequestPayload.password = this.signUpForm.get('password').value;

        this.authService.signUp(this.signUpRequestPayload)
            .subscribe(data => {
                this.toastrService.success('success');
                this.router.navigate(['/login'], {queryParams: {registered: 'true'}});
            }, error => {
                this.toastrService.error('Registration Failed! Please try again');
            });
    }

}
