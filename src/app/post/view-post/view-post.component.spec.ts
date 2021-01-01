import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ViewPostComponent} from './view-post.component';
import {PostService} from "../../shared/post.service";
import {ActivatedRoute, ActivatedRouteSnapshot, Router} from "@angular/router";
import {CommentService} from "../../comment/comment.service";
import {of} from "rxjs";
import {PostModel} from "../../shared/post-model";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {By} from "@angular/platform-browser";
import {HttpClientTestingModule} from "@angular/common/http/testing";

class RouterStub {
    navigateByUrl(url: string) {
    }
}

class ActivatedRouteStub {
    snapshot: ActivatedRouteSnapshot;

    constructor() {
        this.snapshot = new ActivatedRouteSnapshot()
        this.snapshot.params = of({id: 123})
    }
}


describe('ViewPostComponent 11', () => {
    let component: ViewPostComponent;
    let fixture: ComponentFixture<ViewPostComponent>;

    let postModel1: PostModel = {
        id: 1,
        postName: "name",
        url: "url",
        description: "d",
        voteCount: 1,
        userName: "user",
        subReadName: "subReadName1",
        commentCount: 1,
        duration: "dur",
        upVote: false,
        downVote: false
    }

    beforeEach(async () => {

        await TestBed.configureTestingModule({
            declarations: [ViewPostComponent],
            providers: [PostService, CommentService,
                {provide: Router, useClass: RouterStub},
                {provide: ActivatedRoute, useClass: ActivatedRouteStub},
            ],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(ViewPostComponent);
        component = fixture.componentInstance;
        component.post = postModel1
        fixture.detectChanges();
    });


    it('1 init ', () => {
        expect(component).toBeTruthy();
        expect(component).toBeDefined();
    });

    it('2 content ', () => {

        component.loadingPost = true
        component.loadingComment = true
        fixture.detectChanges();

        //проверка вставленого слова
        let postUrl = fixture.debugElement.query(By.css('.post-url')).nativeElement.innerText
        expect(postUrl).toBe(postModel1.subReadName)

        //проверка вставленого слова
        let username = fixture.debugElement.query(By.css('.username')).nativeElement.innerText
        expect(username).toBe(postModel1.userName)

        //проверям наличие компонентов
        const compiled = fixture.debugElement.nativeElement;
        expect(compiled.querySelector('app-vote-button')).not.toBe(null);
        expect(compiled.querySelector('app-side-bar')).not.toBe(null);
        expect(compiled.querySelector('app-subreddit-side-bar')).not.toBe(null);
    });
});
