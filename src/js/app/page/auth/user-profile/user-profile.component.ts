import {Component, OnDestroy, OnInit} from '@angular/core';

import {Subscription, throwError} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {CommentPayload, PostModel} from "../../../utill/class1";
import {PostService} from "../../../service/post.service";
import {CommentService} from "../../../service/comment.service";
import {logUtil} from "../../../utill/log1";


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
        logUtil("UserProfileComponent!")
    }

    ngOnInit(): void {
        this.name = this.activatedRoute.snapshot.params.name;

        this.postsSub = this.postService.getAllPostsByUser(this.name)
            .subscribe(data => {
                logUtil("getAllPostsByUser+ ", data)
                this.posts = data;
                this.postLength = data.length;
            }, error => {
                logUtil("getAllPostsByUser- ", error)
                throwError(error);
            });
        this.commentsSub = this.commentService.getAllCommentsByUser(this.name)
            .subscribe(data => {
                logUtil("getAllCommentsByUser+ ", data)
                this.comments = data;
                this.commentLength = data.length;
            }, error => {
                logUtil("getAllCommentsByUser- ", error)
                throwError(error);
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