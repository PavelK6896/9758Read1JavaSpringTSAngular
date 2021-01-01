import {Component, OnDestroy, OnInit} from '@angular/core';
import {SubredditModel} from "../../subreddit/subreddit-response";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {CreatePostPayload} from "./create-post.payload";
import {Router} from "@angular/router";
import {PostService} from "../../shared/post.service";
import {SubredditService} from "../../subreddit/subreddit.service";
import {Subscription, throwError} from "rxjs";

@Component({
    selector: 'app-create-post',
    templateUrl: './create-post.component.html',
    styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit, OnDestroy {

    createPostForm: FormGroup;
    postPayload: CreatePostPayload;
    subRead: Array<SubredditModel>;

    getAllSubReadSub: Subscription
    createPostSub: Subscription

    constructor(private router: Router,
                private postService: PostService,
                private subredditService: SubredditService) {
    }

    ngOnInit() {
        this.postPayload = {
            postName: '',
            url: '',
            description: '',
            subReadName: ''
        }

        this.createPostForm = new FormGroup({
            postName: new FormControl('', Validators.required),
            subReadName: new FormControl('', Validators.required),
            url: new FormControl('', Validators.required),
            description: new FormControl('', Validators.required),
        });

        this.getAllSubReadSub = this.subredditService.getAllSubreddits()
            .subscribe((data) => {
                this.subRead = data;
            }, error => {
                throwError(error);
            });

    }

    ngOnDestroy(): void {
        if (this.getAllSubReadSub) {
            this.getAllSubReadSub.unsubscribe()
        }
        if (this.createPostSub) {
            this.createPostSub.unsubscribe()
        }
    }

    createPost() {
        this.postPayload.postName = this.createPostForm.get('postName').value;
        this.postPayload.subReadName = this.createPostForm.get('subReadName').value;
        this.postPayload.url = this.createPostForm.get('url').value;
        this.postPayload.description = this.createPostForm.get('description').value;

        this.createPostSub = this.postService.createPost(this.postPayload)
            .subscribe((data) => {
                this.router.navigateByUrl('');
            }, error => {
                throwError(error);
            })
    }

    discardPost() {
        this.router.navigateByUrl('');
    }


}
