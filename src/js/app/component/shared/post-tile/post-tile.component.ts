import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {faComments} from '@fortawesome/free-solid-svg-icons';
import {logUtil} from "../../../utill/log1";
import {PostResponseDto} from "../../../utill/interface1";

@Component({
    selector: 'app-post-tile',
    templateUrl: './post-tile.component.html',
    styleUrls: ['./post-tile.component.css']
})
export class PostTileComponent implements OnInit {

    faComments = faComments;
    @Input() post: PostResponseDto;

    constructor(private router: Router) {
        logUtil("PostTileComponent!")
    }

    ngOnInit(): void {
    }

    goToPost(id: number): void {
        this.router.navigateByUrl('/view-post/' + id);
    }
}
