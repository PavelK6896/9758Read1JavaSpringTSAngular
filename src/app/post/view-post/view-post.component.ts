import {Component, OnInit} from '@angular/core';
import {PostModel} from "../../shared/post-model";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {PostService} from "../../shared/post.service";
import {ActivatedRoute, Router} from "@angular/router";
import {throwError} from "rxjs";
import {CommentPayload} from "../../comment/comment.payload";
import {CommentService} from "../../comment/comment.service";

@Component({
    selector: 'app-view-post',
    templateUrl: './view-post.component.html',
    styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent implements OnInit {

    loadingPost: boolean = false;
    loadingComment: boolean = false;
    postId: number;
    post: PostModel;
    commentForm: FormGroup;
    commentPayload: CommentPayload;
    comments: CommentPayload[];

    constructor(private postService: PostService, private activateRoute: ActivatedRoute,
                private commentService: CommentService, private router: Router) {
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
        this.commentService.postComment(this.commentPayload).subscribe(data => {
            this.commentForm.get('text').setValue('');
            this.getCommentsForPost();
        }, error => {
            throwError(error);
        })
    }

    private getPostById() {
        this.loadingPost = false;
        this.postService.getPostById(this.postId).subscribe(data => {
            this.post = data;
            this.loadingPost = true;
        }, error => {
            throwError(error);
        });


    }

    private getCommentsForPost() {
        this.loadingComment = false
        this.commentService.getAllCommentsForPost(this.postId).subscribe(data => {
            this.comments = data;
            this.loadingComment = true
        }, error => {
            throwError(error);
        });
    }
}
