import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Subscription, throwError} from "rxjs";
import {AuthService} from "../../service/auth.service";
import {logUtil} from "../../utill/log1";

@Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

    isLoggedIn: boolean;
    username: string;
    loggedInEmitterSubscription: Subscription
    usernameEmitterSubscription: Subscription

    constructor(private authService: AuthService, private router: Router) {
        logUtil("HeaderComponent!")
    }

    ngOnInit() {
        this.loggedInEmitterSubscription = this.authService.loggedInEmitter
            .subscribe((data: boolean) => {
                logUtil("loggedInEmitter+ ", data)
                this.isLoggedIn = data
            }, error => {
                logUtil("loggedInEmitter- ", error)
                throwError(error);
            });
        this.usernameEmitterSubscription = this.authService.usernameEmitter
            .subscribe((data: string) => {
                logUtil("usernameEmitter+ ", data)
                this.username = data
            }, error => {
                logUtil("usernameEmitter- ", error)
                throwError(error);
            });
        this.isLoggedIn = this.authService.isLoggedIn();
        this.username = this.authService.getUserName();
    }

    ngOnDestroy(): void {
        if (this.loggedInEmitterSubscription) {
            this.loggedInEmitterSubscription.unsubscribe()
        }
        if (this.usernameEmitterSubscription) {
            this.usernameEmitterSubscription.unsubscribe()
        }
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
