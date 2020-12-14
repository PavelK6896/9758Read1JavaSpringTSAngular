import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../auth/shared/auth.service";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit { // верхнее меню

    isLoggedIn: boolean;
    username: string;

    constructor(private authService: AuthService, private router: Router) {
    }

    ngOnInit() {
        this.authService.loggedIn.subscribe((data: boolean) => this.isLoggedIn = data);
        this.authService.username.subscribe((data: string) => this.username = data);
        this.isLoggedIn = this.authService.isLoggedIn();
        this.username = this.authService.getUserName();
    }

    goToUserProfile() {
        this.router.navigateByUrl('/user-profile/' + this.username);
    }

    logout() {
        this.authService.logout();
        this.isLoggedIn = false;
        this.router.navigateByUrl('');
    }
}
