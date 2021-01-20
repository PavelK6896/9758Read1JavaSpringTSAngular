import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UserViewPostComponent} from './user-view-post.component';
import {PostService} from "../../../service/post.service";
import {ActivatedRoute, ActivatedRouteSnapshot} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {of} from "rxjs";

class ActivatedRouteStub {
    snapshot: ActivatedRouteSnapshot;

    constructor() {
        this.snapshot = new ActivatedRouteSnapshot()
        this.snapshot.params = of({id: 123})
    }
}


describe('UserViewPostComponent 101', () => {
    let component: UserViewPostComponent;
    let fixture: ComponentFixture<UserViewPostComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [UserViewPostComponent],
            providers: [PostService,
                {provide: ActivatedRoute, useClass: ActivatedRouteStub},],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserViewPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

    it('1 should create', () => {
        expect(component).toBeTruthy();
    });
});
