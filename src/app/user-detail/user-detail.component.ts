import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';
import { CommonModule } from '@angular/common';
import { TokenService } from '../service/token.service';
declare var bootstrap: any; 
import { RouterModule, Router } from '@angular/router';
import { UserResponse } from '../responses/user/user.reponse';


@Component({
  selector: 'app-user-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-detail.component.html',
  styleUrl: './user-detail.component.scss'
})
export class UserDetailComponent implements OnInit{
  user: any = null; // Lưu thông tin người dùng
  error: string | null = null;
  userResponse?: UserResponse | null;
  isPopoverOpen: boolean = false;
  constructor(private userService: UserService, 
   
  
    private router: Router,
    private tokenService: TokenService) {}

  ngOnInit(): void {}
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
  openUserDetail(): void {
    this.getUserDetails();

    // Hiển thị modal khi nhấn vào "Tài khoản của tôi"
    const modalElement = document.getElementById('userDetailModal');
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
  }

  getUserDetails(): void {
    const token = localStorage.getItem('access_token'); // Lấy token từ localStorage
    if (!token) {
      this.error = 'Token không tồn tại!';
      return;
    }

    // Gọi API để lấy thông tin người dùng
    this.userService.getUserDetail(token).subscribe({
      next: (data: any) => {
        this.user = data;
      },
      error: (err: any) => {
        this.error = 'Không thể lấy thông tin người dùng.';
        console.error(err);
      }
    });
  }
}
