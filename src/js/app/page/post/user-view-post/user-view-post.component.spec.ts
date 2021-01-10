import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserViewPostComponent } from './user-view-post.component';

describe('UserViewPostComponent', () => {
  let component: UserViewPostComponent;
  let fixture: ComponentFixture<UserViewPostComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserViewPostComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserViewPostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
