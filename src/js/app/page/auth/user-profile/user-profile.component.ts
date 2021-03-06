import {Component, OnDestroy, OnInit} from '@angular/core';

import {Subscription, throwError} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {CommentPayload} from "../../../utill/class1";
import {PostService} from "../../../service/post.service";
import {CommentService} from "../../../service/comment.service";
import {logUtil} from "../../../utill/log1";
import {PostResponseDto} from "../../../utill/interface1";


@Component({
    selector: 'app-user-profile',
    templateUrl: './user-profile.component.html',
    styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit, OnDestroy {

    name: string;
    loadingPost: boolean = false
    loadingComment: boolean = false
    posts: PostResponseDto[];
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
        this.loadingPost = false
        this.postsSub = this.postService.getAllPostsByUser(this.name)
            .subscribe(data => {
                logUtil("getAllPostsByUser+ ", data)
                this.posts = data;
                this.postLength = data.length;
                this.loadingPost = true
            }, error => {
                logUtil("getAllPostsByUser- ", error)
                throwError(error);
            });
        this.loadingComment = false
        this.commentsSub = this.commentService.getAllCommentsByUser(this.name)
            .subscribe(data => {
                logUtil("getAllCommentsByUser+ ", data)
                this.comments = data;
                this.commentLength = data.length;
                this.loadingComment = true
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
