import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewSubredditComponent} from './view-subreddit.component';
import {PostService} from "../../shared/post.service";
import {ActivatedRoute, ActivatedRouteSnapshot} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {of} from "rxjs";
import {SubredditService} from "../subreddit.service";
import {By} from "@angular/platform-browser";
import {SubredditModel} from "../subreddit-response";
import {PostModel} from "../../shared/post-model";


const subId = 50

class ActivatedRouteStub {
    snapshot: ActivatedRouteSnapshot;

    constructor() {
        this.snapshot = new ActivatedRouteSnapshot()
        this.snapshot.params = of({id: subId})
        this.snapshot.params.id = 50
    }
}

describe('ViewSubredditComponent 21', () => {
    let component: ViewSubredditComponent;
    let fixture: ComponentFixture<ViewSubredditComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [ViewSubredditComponent],
            providers: [PostService, SubredditService,
                {provide: ActivatedRoute, useClass: ActivatedRouteStub},
            ],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(ViewSubredditComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('1', () => {
        expect(component).toBeTruthy();
    });

    it('2', () => {

        let postService = TestBed.inject(PostService)
        let subredditService = TestBed.inject(SubredditService)
        let spyPostService = spyOn(postService, 'getAllPostsBySub')

        let posts = [new PostModel(1, 'n', 'u', 'd',
            21, 'e', 's',
            3, 'r', true, false),
            new PostModel(2, 'n', 'u', 'd',
                21, 'e', 's',
                3, 'r', true, false)]
        spyPostService.and.returnValue(of(posts))
        let spySubredditService = spyOn(subredditService, 'getSubredditsId')

        let subredditModel = new SubredditModel(1, 'name', "d", 22);
        spySubredditService.and.returnValue(of(subredditModel))

        component.ngOnInit()
        component.loadingSub = true
        component.loadingPost = true
        fixture.detectChanges();

        expect(spyPostService).toHaveBeenCalledWith(subId)
        expect(spySubredditService).toHaveBeenCalledWith(subId)
        expect(component.subId).toBe(subId)

        let debugElement = fixture.debugElement.query(By.css('h5'));
        expect(debugElement).not.toBe(null)
        const compiled = fixture.debugElement.nativeElement;
        expect(compiled.querySelector('app-post-tile')).not.toBe(null);
    });
});
