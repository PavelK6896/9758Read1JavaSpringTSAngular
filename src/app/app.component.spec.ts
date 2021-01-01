import {AppComponent} from './app.component';
import {TestBed} from "@angular/core/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {NO_ERRORS_SCHEMA} from "@angular/core";

describe('AppComponent 1', () => {
    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [
                RouterTestingModule
            ],
            declarations: [
                AppComponent
            ],
            schemas: [NO_ERRORS_SCHEMA]
        }).compileComponents();
    });

    it('1 init', () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.componentInstance;
        expect(app).toBeTruthy();
    });


    it(`2 should have as title 'Read 1'`, () => {
        const fixture = TestBed.createComponent(AppComponent);
        const app = fixture.componentInstance;
        expect(app.title).toEqual('Read 1');
    });

    it('3', () => {
        const fixture = TestBed.createComponent(AppComponent);
        fixture.detectChanges();
        const compiled = fixture.nativeElement;
        expect(compiled.querySelector('app-header')).not.toBe(null);
        expect(compiled.querySelector('router-outlet')).not.toBe(null);
    });
});
