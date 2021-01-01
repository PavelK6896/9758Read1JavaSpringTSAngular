import {Component, Input, OnInit} from '@angular/core';
import {PostModel} from "../post-model";
import {VotePayload} from "./vote-payload";
import {AuthService} from "../../auth/shared/auth.service";
import {PostService} from "../post.service";
import {ToastrService} from "ngx-toastr";
import {VoteType} from "./vote-type";
import {throwError} from "rxjs";
import {faArrowDown, faArrowUp} from '@fortawesome/free-solid-svg-icons';
import {VoteService} from "../vote.service";

@Component({
    selector: 'app-vote-button',
    templateUrl: './vote-button.component.html',
    styleUrls: ['./vote-button.component.css']
})
export class VoteButtonComponent implements OnInit {

    @Input() post: PostModel;
    votePayload: VotePayload;
    faArrowUp = faArrowUp;
    faArrowDown = faArrowDown;

    upVoteColor: string;
    downVoteColor: string;
    isLoggedIn: boolean;

    constructor(private voteService: VoteService,
                private authService: AuthService,
                private postService: PostService,
                private toastrService: ToastrService
    ) {
        this.votePayload = {
            voteType: undefined,
            postId: undefined
        }
    }

    ngOnInit(): void {
        this.authService.loggedInEmitter
            .subscribe((data: boolean) => this.isLoggedIn = data);
        this.updateVoteDetails();
    }

    upVotePost() {
        this.votePayload.voteType = VoteType.UP_VOTE;
        this.vote();
        this.downVoteColor = '';
    }

    downVotePost() {
        this.votePayload.voteType = VoteType.DOWN_VOTE;
        this.vote();
        this.upVoteColor = '';
    }

    private vote() {
        this.votePayload.postId = this.post.id;
        this.voteService.vote(this.votePayload)
            .subscribe(() => {
                this.updateVoteDetails();
            }, error => {
                this.toastrService.error(error.error.message);
                throwError(error);
            });
    }

    private updateVoteDetails() {
        this.postService.getPostById(this.post.id)
            .subscribe(post => {
                this.post = post;
            });
    }
}
