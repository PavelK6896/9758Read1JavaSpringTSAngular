import {Component, Input, OnInit} from '@angular/core';
import {throwError} from "rxjs";
import {faArrowDown, faArrowUp} from '@fortawesome/free-solid-svg-icons';
import {VoteService} from "../../../service/vote.service";
import {logUtil} from "../../../utill/log1";
import {PostResponseDto, VoteDto, VoteType} from "../../../utill/interface1";

@Component({
    selector: 'app-vote-button',
    templateUrl: './vote-button.component.html',
    styleUrls: ['./vote-button.component.css']
})
export class VoteButtonComponent implements OnInit {

    @Input() post: PostResponseDto;
    voteDto: VoteDto;
    faArrowUp = faArrowUp;
    faArrowDown = faArrowDown;
    up = 'UP_VOTE'
    down = 'DOWN_VOTE'
    isLoggedIn: boolean;

    constructor(private voteService: VoteService) {
        logUtil("VoteButtonComponent!")
        this.voteDto = {
            voteType: undefined,
            postId: undefined
        }
    }

    ngOnInit(): void {
    }

    upVotePost() {
        this.voteDto.voteType = VoteType.UP_VOTE;
        this.post.vote = this.up
        this.vote();
    }

    downVotePost() {
        this.voteDto.voteType = VoteType.DOWN_VOTE;
        this.post.vote = this.down
        this.vote();
    }

    private vote() {
        this.voteDto.postId = this.post.id;
        this.voteService.vote(this.voteDto)
            .subscribe((data) => {
                logUtil("vote+ ", data)
                this.post.voteCount = data
            }, error => {
                logUtil("vote- ", error)
                throwError(error);
            });
    }
}
