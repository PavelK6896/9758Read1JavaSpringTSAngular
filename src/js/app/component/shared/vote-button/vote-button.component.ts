import {Component, Input, OnInit} from '@angular/core';
import {ToastrService} from "ngx-toastr";
import {throwError} from "rxjs";
import {faArrowDown, faArrowUp} from '@fortawesome/free-solid-svg-icons';
import {PostModel, VotePayload, VoteType} from "../../../utill/class1";
import {VoteService} from "../../../service/vote.service";
import {AuthService} from "../../../service/auth.service";
import {PostService} from "../../../service/post.service";
import {logUtil} from "../../../utill/log1";


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
        logUtil("VoteButtonComponent!")
        this.votePayload = {
            voteType: undefined,
            postId: undefined
        }
    }

    ngOnInit(): void {
        this.authService.loggedInEmitter
            .subscribe((data: boolean) => {
                logUtil("loggedInEmitter+ ", data)
                this.isLoggedIn = data
            }, error => {
                logUtil("loggedInEmitter- ", error)
                throwError(error);
            });
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
            .subscribe((data) => {
                logUtil("vote+ ", data)
                this.updateVoteDetails();
            }, error => {
                logUtil("vote- ", error)
                this.toastrService.error(error.error.message);
                throwError(error);
            });
    }

    private updateVoteDetails() {
        this.postService.getPostById(this.post.id)
            .subscribe(data => {
                logUtil("getPostById+ ", data)
                this.post = data;
            }, error => {
                logUtil("getPostById- ", error)
                this.toastrService.error(error.error.message);
                throwError(error);
            });
    }
}
