import {ComponentFixture, TestBed} from '@angular/core/testing';

import {HeaderComponent} from './header.component';
import {AuthService} from "../auth/shared/auth.service";
import {Router} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";


class RouterStub {
    navigate(path: string[]) {
    }
}

describe('HeaderComponent', () => {
    let component: HeaderComponent;
    let fixture: ComponentFixture<HeaderComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [HeaderComponent],
            providers: [AuthService,
                {provide: Router, useClass: RouterStub}
            ],
            imports: [HttpClientModule]
        })
            .compileComponents();
    });

    beforeEach(() => {
        fixture = TestBed.createComponent(HeaderComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    // it('HeaderComponent 1', () => {
    //     expect(component).toBeTruthy();
    // });
});
