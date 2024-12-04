import { Component, OnInit,ViewChild } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { UserResponse } from '../responses/user/user.reponse';
import { UserService } from '../service/user.service';
import { CommonModule } from '@angular/common';
import { NgbPopoverModule, NgbPopoverConfig } from '@ng-bootstrap/ng-bootstrap';
import { TokenService } from '../service/token.service';
import { UserDetailComponent } from '../user-detail/user-detail.component';
import { CartService } from '../service/card.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterModule, CommonModule, NgbPopoverModule,UserDetailComponent],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  @ViewChild(UserDetailComponent) userDetailComponent!: UserDetailComponent;
  userResponse?: UserResponse | null;
  isPopoverOpen: boolean = false;
  activeNavItem: number = -1; // Dùng để theo dõi tab hiện tại
  productId: number = 1; // Giá trị mặc định, có thể thay đổi tùy nhu cầu
  cartItemCount: number = 0;
  
  

  constructor(
    private userService: UserService,
    private popoverConfig: NgbPopoverConfig,
    private router: Router,
    private tokenService: TokenService,
    private cartService: CartService
  ) {
    // Configure default settings for popovers
    this.popoverConfig.placement = 'bottom';
    this.popoverConfig.triggers = 'click';
   
  }

  ngOnInit() {
    this.cartService.getCartObservable().subscribe(cart => {
      this.cartItemCount = cart.size; // Cập nhật số lượng sản phẩm trong giỏ hàng
    });
    this.loadUser();
    // Lấy thông tin người dùng từ UserService khi khởi tạo component
    this.updateCartItemCount();
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
  }

  togglePopover(event: Event): void {
    event.preventDefault();
    this.isPopoverOpen = !this.isPopoverOpen;
  }

  handleItemClick(index: number): void {
    // Xử lý khi click vào các mục trong popover
    if (index === 2) { // Nếu chọn "Đăng xuất"
      this.userService.removeUserFromLocalStorage();
      this.tokenService.removeToken();
      this.userResponse = null;
      this.router.navigate(['/login']); // Điều hướng tới trang đăng nhập sau khi đăng xuất
    }
    this.isPopoverOpen = false; // Đóng popover sau khi click vào item
  }

  setActiveNavItem(index: number): void {
    this.activeNavItem = index; // Cập nhật tab đang active
  }

  viewProductDetail(productId: number): void {
    this.router.navigate(['/detail', productId]); // Điều hướng đến trang chi tiết sản phẩm với `productId` động
  }

  loadUser(): void {
    if (typeof localStorage === 'undefined') {
      console.warn('localStorage is not available in this environment.');
      return;
    }
  
    const user = localStorage.getItem('user');
    this.userResponse = user ? JSON.parse(user) : null;
  }
  

  // Đổi trạng thái active menu
  

  // Mở modal hiển thị thông tin tài khoản
  openUserDetail(): void {
    this.userDetailComponent.openUserDetail();
  }
  updateCartItemCount(): void {
    this.cartItemCount = this.cartService.getCartOrderCount(); // Cập nhật số lượng loại sản phẩm trong giỏ hàng
  }

  navigateToDetail(id: number): void {
    this.router.navigate(['/detail', id]); // Điều hướng tới '/detail/:id'
  }
}
