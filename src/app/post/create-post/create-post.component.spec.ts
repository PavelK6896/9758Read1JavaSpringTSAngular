import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CreatePostComponent} from './create-post.component';
import {Router} from "@angular/router";
import {SubredditService} from "../../subreddit/subreddit.service";
import {HttpClientModule} from "@angular/common/http";
import {By} from "@angular/platform-browser";
import {PostService} from "../../shared/post.service";
import {CreatePostPayload} from "./create-post.payload";
import {of} from "rxjs";
import {DebugElement, NO_ERRORS_SCHEMA} from "@angular/core";
import {HttpClientTestingModule} from "@angular/common/http/testing";

class RouterStub {
    navigate(path: string[]) {
    }

    navigateByUrl(url: string) {
    }
}


describe('CreatePostComponent 4', () => {
    let component: CreatePostComponent;
    let fixture: ComponentFixture<CreatePostComponent>;
    let postService: PostService

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [CreatePostComponent],
            providers: [PostService, SubredditService,
                {provide: Router, useClass: RouterStub}],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(CreatePostComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it(' 1 init ', () => {
        expect(component).toBeTruthy();
        expect(component).toBeDefined();
    });

    //тест кнопки
    it(' 2 discardPost right', () => {
        //получаем кнопку
        let discardPost = fixture.debugElement.query(By.css('#discardPost'))
        //получаем роутер по интерфейсу
        let router = fixture.debugElement.injector.get(Router)
        //мокае роутер
        let spyRouter = spyOn(router, 'navigateByUrl')

        //--
        //прожимем кнопку
        discardPost.triggerEventHandler('click', null)
        //чекаем роутер
        expect(spyRouter).toHaveBeenCalledWith('')
    });

    it(' 3 createPost right', () => {
        let postPayload1: CreatePostPayload = {
            postName: "name",
            subReadName: "sub",
            url: "url",
            description: "d"
        }
        let router = TestBed.inject(Router)
        let postService = TestBed.inject(PostService)
        let createPost = fixture.debugElement.query(By.css('#createPost'))


        let spyPostService = spyOn(postService, 'createPost')
        spyPostService.and.returnValue(of(postPayload1))
        let spyRouter = spyOn(router, 'navigateByUrl')

        createPost.triggerEventHandler('click', null)

        expect(spyRouter).toHaveBeenCalledWith('')
        expect(spyPostService).toHaveBeenCalled();
        expect(spyPostService).toHaveBeenCalledWith(component.postPayload);
        expect(spyPostService.calls.count()).toBe(1)

    });


});
