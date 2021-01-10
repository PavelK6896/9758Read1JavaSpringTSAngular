import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HomeComponent} from './home.component';
import {of} from "rxjs";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {PostService} from "../../service/post.service";
// import {PostModel} from "../../utill/class1";

describe('HomeComponent 9', () => {
    let component: HomeComponent;
    let fixture: ComponentFixture<HomeComponent>;
    let postService: PostService;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [HomeComponent],
            providers: [PostService],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(HomeComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it(' 1 init ', () => {
        expect(component).toBeTruthy();
    });

    it(' 2 getAllPosts ', () => {

        // let postModel1: PostModel = {
        //     id: 1,
        //     postName: "name",
        //     url: "url",
        //     description: "d",
        //     voteCount: 1,
        //     userName: "user",
        //     subReadName: "sub",
        //     commentCount: 1,
        //     duration: "dur",
        //     upVote: false,
        //     downVote: false
        // }
        //
        // let postModel2: PostModel = {
        //     id: 2,
        //     postName: "name2",
        //     url: "url2",
        //     description: "d2",
        //     voteCount: 1,
        //     userName: "user",
        //     subReadName: "sub",
        //     commentCount: 1,
        //     duration: "dur",
        //     upVote: false,
        //     downVote: false
        // }

        // const postsTest = [postModel1, postModel2];
        // postService = fixture.debugElement.injector.get(PostService)
        // spyOn(postService, 'getAllPosts').and.returnValue(of(postsTest))
        // component.ngOnInit()
        // expect(component.posts).toEqual(postsTest)
        //
        // const compiled = fixture.debugElement.nativeElement;
        // expect(compiled.querySelector('app-post-tile')).not.toBe(null);
        // expect(compiled.querySelector('app-side-bar')).not.toBe(null);
        // expect(compiled.querySelector('app-subreddit-side-bar')).not.toBe(null);
    });

});
