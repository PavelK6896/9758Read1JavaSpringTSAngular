import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {LoginRequestPayload} from "./login-request.payload";
import {AuthService} from "../shared/auth.service";
import {ActivatedRoute, Router} from "@angular/router";
import {throwError} from "rxjs";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginRequestPayload: LoginRequestPayload;
  registerSuccessMessage: string;
  isError: boolean;

  constructor(private authService: AuthService, private activatedRoute: ActivatedRoute,
              private router: Router, private toastr: ToastrService) { //Тостр ToastrService
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

    this.activatedRoute.queryParams
      .subscribe(params => {//инит
        if (params.registered !== undefined && params.registered === 'true') {
          this.toastr.success('Signup Successful');//Успешный

          this.registerSuccessMessage = 'Please Check your inbox for activation email '
            //Пожалуйста, проверьте свой почтовый ящик для активации электронной почты
            + 'activate your account before you Login!';
          //активируйте свой аккаунт перед входом в систему!
        }
      });
  }

  login() {//вход

    console.log("----------------------------------------------")
    this.loginRequestPayload.username = this.loginForm.get('username').value;
    this.loginRequestPayload.password = this.loginForm.get('password').value;

    console.log(this.loginRequestPayload)

    this.authService.login(this.loginRequestPayload).subscribe(data => {
      this.isError = false;
      this.router.navigateByUrl(''); //редирект
      this.toastr.success('Login Successful'); //анимация

    }, error => {
      this.isError = true;
      throwError(error);
    });
  }


}
