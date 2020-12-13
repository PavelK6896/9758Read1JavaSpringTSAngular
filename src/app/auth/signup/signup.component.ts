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

    signupRequestPayload: SignupRequestPayload;
    signupForm: FormGroup; // форма для регистрации

    constructor(private authService: AuthService, private router: Router, private toastr: ToastrService) {
        this.signupRequestPayload = {
            username: '',
            email: '',
            password: ''
        };
    }

    ngOnInit() {
        this.signupForm = new FormGroup({ // форма со встроиной валидацией
            username: new FormControl('', Validators.required),
            email: new FormControl('', [Validators.required, Validators.email]),
            password: new FormControl('', Validators.required),
        });
    }

    signup() {

        this.signupRequestPayload.email = this.signupForm.get('email').value;
        this.signupRequestPayload.username = this.signupForm.get('username').value;
        this.signupRequestPayload.password = this.signupForm.get('password').value;

        this.authService.signup(this.signupRequestPayload)
            .subscribe(data => {
                console.log(data)
                this.router.navigate(['/login'], //редирект на логин
                    {queryParams: {registered: 'true'}});
            }, error => {
                console.log(error);
                this.toastr.error('Registration Failed! Please try again'); //анимация
                //Регистрация Не Удалась! Пожалуйста, попробуйте еще раз
            });
    }

}
