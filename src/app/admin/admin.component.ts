import { Component, OnInit } from '@angular/core';
import { TokenService } from '../service/token.service';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';
import { UserResponse } from '../responses/user/user.reponse';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { OrderAdminComponent } from './order.admin/order.admin.component';
import { ProductAdminComponent } from './managementProduct/product/product.admin.component';
import { CategoryAdminComponent } from './cate/category/category.admin.component';
import { UserListAdminComponent } from './User/userList/user-list.admin.component';


@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [RouterModule, CommonModule, OrderAdminComponent, ProductAdminComponent,CategoryAdminComponent,UserListAdminComponent],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.scss'
})
export class AdminComponent implements OnInit {
  userResponse?: UserResponse | null;
  adminComponent: string = 'order';
  selectedNav: string = 'order';

  constructor(
    private userService: UserService,
    private router: Router,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
  }

  logout() {
    this.userService.removeUserFromLocalStorage();
    this.tokenService.removeToken();
    this.userResponse = this.userService.getUserResponseFromLocalStorage();
  }

  showAdminComponent(selectedComponent: string) {
    this.adminComponent = selectedComponent;
    this.selectedNav = selectedComponent;

    // Điều hướng dựa trên component được chọn
    
    // Thêm các điều hướng khác nếu có
  }
}
