import {Component, OnInit, ViewEncapsulation} from '@angular/core';

import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {throwError} from "rxjs";
import {CommentPayload} from "../../../utill/class1";
import {PostService} from "../../../service/post.service";
import {CommentService} from "../../../service/comment.service";
import {logUtil} from "../../../utill/log1";
import {PostResponseDto} from "../../../utill/interface1";

@Component({
    selector: 'app-view-post',
    templateUrl: './view-post.component.html',
    styleUrls: ['./view-post.component.css'],
    encapsulation: ViewEncapsulation.None,
})
export class ViewPostComponent implements OnInit {

    loadingPost: boolean = false;
    loadingComment: boolean = false;
    postId: number;
    post: PostResponseDto;
    commentForm: FormGroup;
    commentPayload: CommentPayload;
    comments: CommentPayload[];

    constructor(private postService: PostService, private activateRoute: ActivatedRoute,
                private commentService: CommentService, private router: Router) {
        logUtil("ViewPostComponent!")
        this.postId = this.activateRoute.snapshot.params.id;
        this.commentForm = new FormGroup({
            text: new FormControl('',
                [Validators.required, Validators.minLength(1), Validators.maxLength(250)])
        });
        this.commentPayload = {
            text: '',
            postId: this.postId
        };
    }

    ngOnInit(): void {
        this.getPostById();
        this.getCommentsForPost();
    }

    postComment() {
        this.commentPayload.text = this.commentForm.get('text').value;
        if (this.commentPayload.text.trim().length == 0) {
            return
        }
        this.commentService.postComment(this.commentPayload)
            .subscribe(data => {
                logUtil("postComment+ ", data)
                this.commentForm.get('text').setValue('');
                this.getCommentsForPost();
            }, error => {
                logUtil("postComment- ", error)
                throwError(error);
            })
    }

    private getPostById() {
        this.loadingPost = false;
        this.postService.getPostById(this.postId)
            .subscribe(data => {
                logUtil("getPostById+ ", data)
                this.post = data;
                this.loadingPost = true;
            }, error => {
                logUtil("getPostById- ", error)
                throwError(error);
            });
    }

    private getCommentsForPost() {
        this.loadingComment = false
        this.commentService.getAllCommentsForPost(this.postId)
            .subscribe(data => {
                logUtil("getAllCommentsForPost+ ", data)
                this.comments = data;
                this.loadingComment = true
            }, error => {
                logUtil("getAllCommentsForPost- ", error)
                throwError(error);
            });
    }
}
