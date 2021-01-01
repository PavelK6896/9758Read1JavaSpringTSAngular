import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PostTileComponent} from './post-tile.component';
import {Router} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {By} from "@angular/platform-browser";
import {PostModel} from "../post-model";

class RouterStub {
    navigate(path: string[]) {
    }

    navigateByUrl(url: string) {
    }
}

describe('PostTileComponent 12', () => {
    let component: PostTileComponent;
    let fixture: ComponentFixture<PostTileComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [PostTileComponent],
            providers: [
                {provide: Router, useClass: RouterStub}
            ],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(PostTileComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('1 ', () => {
        expect(component).toBeTruthy();
    });

    it('2 ', () => {

        let newP: PostModel = {
            id: 55,
            postName: "string",
            url: "string",
            description: "string",
            voteCount: 1,
            userName: "string",
            subReadName: "string",
            commentCount: 2,
            duration: "string",
            upVote: false,
            downVote: false,
        }
        component.posts = [newP]
        fixture.detectChanges();

        let router = TestBed.inject(Router)
        let loginButton = fixture.debugElement.query(By.css('.loginButton'))

        let spyRouter = spyOn(router, 'navigateByUrl')
        loginButton.triggerEventHandler('click', null)


        expect(spyRouter).toHaveBeenCalledWith('/view-post/' + newP.id)
    });
});
