import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateSubredditComponent} from './create-subreddit.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {SubredditService} from "../subreddit.service";
import {Router} from "@angular/router";

class RouterStub {
    navigateByUrl(url: string) {
    }
}

describe('CreateSubReadComponent 18', () => {
    let component: CreateSubredditComponent;
    let fixture: ComponentFixture<CreateSubredditComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [CreateSubredditComponent],
            providers: [SubredditService,
                {provide: Router, useClass: RouterStub}
            ],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(CreateSubredditComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('1 should create', () => {
        expect(component).toBeTruthy();
    });
});
