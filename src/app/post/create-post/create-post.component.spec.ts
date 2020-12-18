import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CreatePostComponent} from './create-post.component';
import {Router} from "@angular/router";
import {SubredditService} from "../../subreddit/subreddit.service";
import {HttpClientModule} from "@angular/common/http";
import {By} from "@angular/platform-browser";
import {PostService} from "../../shared/post.service";
import {CreatePostPayload} from "./create-post.payload";
import {of} from "rxjs";
import {DebugElement} from "@angular/core";

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
            imports: [HttpClientModule]
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
        //получаем кнопку
        let createPost: DebugElement = fixture.debugElement.query(By.css('#createPost'))
        let form: DebugElement = fixture.debugElement.query(By.css('form'));

        //получаем роутер по интерфейсу
        let router = fixture.debugElement.injector.get(Router)
        //мокае роутер
        let spyOnRouter = spyOn(router, 'navigateByUrl')
        //получаем сервис
        let postService = fixture.debugElement.injector.get(PostService)
        //мокаем метод сервиса

        let postPayload1: CreatePostPayload = {
            postName: "name",
            subredditName: "sub",
            url: "url",
            description: "d"
        }

        let spyOnPostService = spyOn(postService, 'createPost')
        spyOnPostService.and.returnValue(of(postPayload1))

        //--
        //click button
        createPost.triggerEventHandler('click', null)
        // form.triggerEventHandler('submit', null)
        // fixture.detectChanges();
        // component.createPost()

        //--
        //чекаем роутер
        expect(spyOnRouter).toHaveBeenCalledWith('')
        //чек сервис вызван
        expect(spyOnPostService).toHaveBeenCalled();
        //вызван с пораметрами
        expect(spyOnPostService).toHaveBeenCalledWith(component.postPayload);
        //вызван 1 раз
        expect(spyOnPostService.calls.count()).toBe(1)

    });


});
