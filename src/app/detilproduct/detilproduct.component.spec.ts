import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetilproductComponent } from './detilproduct.component';

describe('DetilproductComponent', () => {
  let component: DetilproductComponent;
  let fixture: ComponentFixture<DetilproductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetilproductComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetilproductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
