import {ComponentFixture, TestBed} from '@angular/core/testing';

import {VoteButtonComponent} from './vote-button.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";
import {VoteService} from "../vote.service";
import {AuthService} from "../../auth/shared/auth.service";
import {PostService} from "../post.service";
import {ToastrService} from "ngx-toastr";
import {PostModel} from "../post-model";

class ToastrServiceSub {
    success(message?: string) {
    }

    error(message?: string) {
    }
}

describe('VoteButtonComponent 15', () => {
    let component: VoteButtonComponent;
    let fixture: ComponentFixture<VoteButtonComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [VoteButtonComponent],
            providers: [VoteService, AuthService, PostService,
                {provide: ToastrService, useClass: ToastrServiceSub},
            ],
            imports: [HttpClientTestingModule],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(VoteButtonComponent);
        component = fixture.componentInstance;
        component.post = new PostModel(1, 'n', 'u', 'd',
            21, 'e', 's', 3, 'r', true, false)
        fixture.detectChanges();
    });

    it('1 should create', () => {
        expect(component).toBeTruthy();
    });
});
