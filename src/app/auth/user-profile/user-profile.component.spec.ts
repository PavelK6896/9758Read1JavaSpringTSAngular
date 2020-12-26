import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UserProfileComponent} from './user-profile.component';
import {ActivatedRoute, ActivatedRouteSnapshot, Params} from "@angular/router";
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {of, Subject} from "rxjs";
import {CommentService} from "../../comment/comment.service";
import {PostService} from "../../shared/post.service";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {HttpClientTestingModule} from "@angular/common/http/testing";

class ActivatedRouteStub {
    private subject = new Subject<Params>();
    snapshot: ActivatedRouteSnapshot;

    constructor() {
        this.snapshot = new ActivatedRouteSnapshot()
        this.snapshot.params = of({name: 'user13'})
    }

    push(params: Params) {
        this.subject.next(params)
    }

    get queryParams() {
        return this.subject.asObservable()
    }

}


describe('UserProfileComponent 13', () => {
    let component: UserProfileComponent;
    let fixture: ComponentFixture<UserProfileComponent>;

    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [ UserProfileComponent ],
          providers: [CommentService, PostService,
              {provide: ActivatedRoute, useClass: ActivatedRouteStub},
          ],
          imports: [HttpClientTestingModule ],
          schemas: [NO_ERRORS_SCHEMA]
      })
      .compileComponents();
    });

    beforeEach(() => {
      fixture = TestBed.createComponent(UserProfileComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    //вызывает метод инит ??
    it('1 init', () => {
      expect(component).toBeDefined();
    });

    it('2 ', () => {

        // let httpClient = TestBed.inject(HttpClient);
        // let spyHttp = spyOn(httpClient, 'get').and.returnValues()
        let commentService = TestBed.inject(CommentService)
        let postService = TestBed.inject(PostService)

        let spyPostService = spyOn(postService, 'getAllPostsByUser').and.returnValue(of([]))
        let spyCommentService = spyOn(commentService, 'getAllCommentsByUser').and.returnValue(of([]))

        component.ngOnInit()
        expect(spyPostService).toHaveBeenCalled()
        expect(spyCommentService).toHaveBeenCalled()

       expect(fixture.debugElement.nativeElement.querySelector('app-post-tile')).not.toBe(null);
    });


});
