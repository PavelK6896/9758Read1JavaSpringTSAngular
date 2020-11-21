import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {SignupComponent} from "./auth/signup/signup.component";


const routes: Routes = [
  // { path: '', component: HomeComponent },
  // { path: 'view-post/:id', component: ViewPostComponent },
  // { path: 'user-profile/:name', component: UserProfileComponent, canActivate: [AuthGuard] },
  // { path: 'list-subreddits', component: ListSubredditsComponent },
  // { path: 'create-post', component: CreatePostComponent, canActivate: [AuthGuard] },
  // { path: 'create-subreddit', component: CreateSubredditComponent, canActivate: [AuthGuard] },
  { path: 'sign-up', component: SignupComponent },
  // { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
