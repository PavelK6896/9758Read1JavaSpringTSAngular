import {Component, OnInit} from '@angular/core';
import {PostResponseDto} from "../../../utill/interface1";
import {Subscription, throwError} from "rxjs";
import {logUtil} from "../../../utill/log1";
import {PostService} from "../../../service/post.service";
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'app-user-view-post',
    templateUrl: './user-view-post.component.html',
    styleUrls: ['./user-view-post.component.css']
})
export class UserViewPostComponent implements OnInit {

    posts: PostResponseDto[] = [];
    postsSubscription: Subscription
    loadingPost: boolean = false
    name: string
    postLength: number


    constructor(private postService: PostService,
                private activatedRoute: ActivatedRoute) {
        logUtil("UserViewPostComponent!")
    }


    ngOnInit(): void {
        this.name = this.activatedRoute.snapshot.params.name;
        this.loadingPost = false
        this.postsSubscription = this.postService.getAllPostsByUser(this.name)
            .subscribe(data => {
                logUtil("getAllPostsBySub+ ", data)
                this.posts = data;
                this.postLength = data.length;
                this.loadingPost = true
            }, error => {
                logUtil("getAllPostsBySub- ", error)
                throwError(error);
            });
    }


}
