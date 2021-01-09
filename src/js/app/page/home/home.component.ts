import {Component, OnDestroy, OnInit} from '@angular/core';

import {Subscription, throwError} from "rxjs";
import {PostModel} from "../../utill/class1";
import {PostService} from "../../service/post.service";
import {logUtil} from "../../utill/log1";


@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

    posts: Array<PostModel> = [];
    getAllPostsSubscription: Subscription

    constructor(private postService: PostService) {
        logUtil("HomeComponent!")
    }

    ngOnInit(): void {
        this.getAllPostsSubscription = this.postService.getAllPosts()
            .subscribe(data => {
                logUtil("getAllPosts+ ", data)
                this.posts = data;
            }, error => {
                logUtil("getAllPosts- ", error)
                throwError(error);
            });
    }

    ngOnDestroy(): void {
        if (this.getAllPostsSubscription) {
            this.getAllPostsSubscription.unsubscribe()
        }
    }

}
