import {Component, OnDestroy, OnInit} from '@angular/core';
import {PostModel} from "../../shared/post-model";
import {CommentPayload} from "../../comment/comment.payload";
import {ActivatedRoute} from "@angular/router";
import {PostService} from "../../shared/post.service";
import {CommentService} from "../../comment/comment.service";
import {Subscription} from "rxjs";

@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit, OnDestroy {

    name: string;

    posts: PostModel[];
    postLength: number;

    comments: CommentPayload[];
    commentLength: number;

    postsSub: Subscription
    commentsSub: Subscription

    constructor(private activatedRoute: ActivatedRoute,
                private postService: PostService,
                private commentService: CommentService) {
    }

    ngOnInit(): void {
        this.name = this.activatedRoute.snapshot.params.name;

        this.postsSub = this.postService.getAllPostsByUser(this.name)
            .subscribe(data => {
                this.posts = data;
                this.postLength = data.length;
            });
        this.commentsSub = this.commentService.getAllCommentsByUser(this.name)
            .subscribe(data => {
                this.comments = data;
                this.commentLength = data.length;
            });
    }

    ngOnDestroy(): void {
        if (this.postsSub) {
            this.postsSub.unsubscribe()
        }
        if (this.commentsSub) {
            this.commentsSub.unsubscribe()
        }
    }
}
