import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {ToastrModule} from "ngx-toastr";
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HeaderComponent} from "./component/header/header.component";
import {SignupComponent} from "./page/auth/signup/signup.component";
import {LoginComponent} from "./page/auth/login/login.component";
import {HomeComponent} from "./page/home/home.component";
import {PostTileComponent} from "./component/shared/post-tile/post-tile.component";
import {VoteButtonComponent} from "./component/shared/vote-button/vote-button.component";
import {SideBarComponent} from "./component/shared/side-bar/side-bar.component";
import {SubredditSideBarComponent} from "./component/shared/subreddit-side-bar/subreddit-side-bar.component";
import {CreateSubredditComponent} from "./page/subreddit/create-subreddit/create-subreddit.component";
import {CreatePostComponent} from "./page/post/create-post/create-post.component";
import {ListSubredditsComponent} from "./page/subreddit/list-subreddits/list-subreddits.component";
import {UserProfileComponent} from "./page/auth/user-profile/user-profile.component";
import {ViewPostComponent} from "./page/post/view-post/view-post.component";
import {ViewSubredditComponent} from "./page/subreddit/view-subreddit/view-subreddit.component";
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {TokenInterceptor} from "./security/token-interceptor";
import {QuillModule} from "ngx-quill";

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        SignupComponent,
        LoginComponent,
        HomeComponent,
        PostTileComponent,
        VoteButtonComponent,
        SideBarComponent,
        SubredditSideBarComponent,
        CreateSubredditComponent,
        CreatePostComponent,
        ListSubredditsComponent,
        ViewPostComponent,
        UserProfileComponent,
        ViewSubredditComponent,

    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        ReactiveFormsModule, // для формы ввода
        HttpClientModule,
        BrowserAnimationsModule, // для анимации
        ToastrModule.forRoot(),  //для анимации тостар
        FontAwesomeModule, //иконки
        // EditorModule, //tiny
        QuillModule.forRoot(),
        NgbModule
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: TokenInterceptor,
            multi: true
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}


