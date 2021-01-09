import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {Subscription, throwError} from "rxjs";
import {CreatePostPayload, SubredditModel} from "../../../utill/class1";
import {PostService} from "../../../service/post.service";
import {SubredditService} from "../../../service/subreddit.service";
import {logUtil} from "../../../utill/log1";

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
        logUtil("CreatePostComponent!")
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
            description: new FormControl(null, Validators.required),
        });

        this.getAllSubReadSub = this.subredditService.getAllSubreddits()
            .subscribe((data) => {
                logUtil("getAllSubreddits+ ", data)
                this.subRead = data;
            }, error => {
                logUtil("getAllSubreddits- ", error)
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
        if( this.postPayload.subReadName === "" && this.subRead){
            this.postPayload.subReadName = this.subRead[0].name
        }
        this.postPayload.url = this.createPostForm.get('url').value;
        this.postPayload.description = this.createPostForm.get('description').value;

        console.log("****************")
        console.log("****************")
        console.log(this.postPayload)

        this.createPostSub = this.postService.createPost(this.postPayload)
            .subscribe((data) => {
                logUtil("createPost+ ", data)
                this.router.navigateByUrl('');


            }, error => {
                logUtil("createPost- ", error)
                throwError(error);
            })
    }

    discardPost() {
        this.router.navigateByUrl('');
    }


}
