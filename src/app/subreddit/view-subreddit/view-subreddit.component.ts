import {Component, OnDestroy, OnInit} from '@angular/core';
import {PostModel} from "../../shared/post-model";
import {Subscription} from "rxjs";
import {PostService} from "../../shared/post.service";
import {ActivatedRoute} from "@angular/router";
import {SubredditService} from "../subreddit.service";
import {SubredditModel} from "../subreddit-response";

@Component({
    selector: 'app-view-subreddit',
    templateUrl: './view-subreddit.component.html',
    styleUrls: ['./view-subreddit.component.css']
})
export class ViewSubredditComponent implements OnInit, OnDestroy {

    posts: PostModel[]
    sub: SubredditModel
    postLength: number;
    subId: number;
    postsSubscription: Subscription
    subSubscription: Subscription

    loadingPost: boolean = false
    loadingSub: boolean = false

    constructor(private postService: PostService,
                private subredditService: SubredditService,
                private activatedRoute: ActivatedRoute) {
    }

    ngOnInit(): void {
        this.subId = this.activatedRoute.snapshot.params.id;

        this.postsSubscription = this.postService.getAllPostsBySub(this.subId)
            .subscribe(data => {
                this.posts = data;
                this.postLength = data.length;
                this.loadingPost = true
            });

        this.subSubscription = this.subredditService.getSubredditsId(this.subId)
            .subscribe(data => {
                    this.sub = data
                    this.loadingSub = true
                }
            )
    }

    ngOnDestroy(): void {
        if (this.subSubscription) {
            this.subSubscription.unsubscribe()
        }
        if (this.postsSubscription) {
            this.postsSubscription.unsubscribe()
        }
    }

}
