import { Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { OderComponent } from './component/oder/oder.component';
import { DetailComponent } from './component/detail/detail.component';
import { OrderConfirmComponent } from './component/oder-confirm/oder-confirm.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { ProductDetailComponent } from './detilproduct/detilproduct.component';

import { AdminComponent } from './admin/admin.component';
import { AdminGuardFn } from './service/Guard/admin.guard';
import { AuthGuardFn } from './service/Guard/auth.guard';
import { OrderAdminComponent } from './admin/order.admin/order.admin.component';
import { AddProductAdminComponent } from './admin/managementProduct/addProduct/add.product.admin.component';
import { ProductAdminComponent } from './admin/managementProduct/product/product.admin.component';
import { UpdateProductComponent } from './admin/managementProduct/updateProduct/update.product.component';
import { CategoryAdminComponent } from './admin/cate/category/category.admin.component';
import { AddCategoryAdminComponent } from './admin/cate/addCategory/add.category.admin.component';
import { UpdateCategoryAdminComponent } from './admin/cate/updateCategory/update.category.admin.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { UserListAdminComponent } from './admin/User/userList/user-list.admin.component';


export const appRoutes: Routes = [
  { path: '', component: HomeComponent },               // Trang chính (HomeComponent)
  { path: 'login', component: LoginComponent}, 
     
  { path: 'admin', component: AdminComponent,canActivate: [AdminGuardFn]   },
  { path: 'admin/products', component: ProductAdminComponent,canActivate: [AdminGuardFn]   },

  { path: 'admin/order', component: OrderAdminComponent,canActivate: [AdminGuardFn]   },       // Trang login
  { path: 'order', component: OderComponent,canActivate:[AuthGuardFn] },          // Trang order không có tham số
      // Trang order với tham số id
      
  { path: 'order-confirm', component: OrderConfirmComponent }, // Trang xác nhận đơn hàng
  { path: 'register', component: RegisterComponent },   // Trang đăng ký
  { path: 'products/:id', component: ProductDetailComponent }, // Trang chi tiết sản phẩm
  { path: 'detail/:id', component: DetailComponent },       // Trang chi tiết sản phẩm (ví dụ)
   // Điều hướng về HomeComponent nếu không tìm thấy route
  { path: 'admin/app-add-product-admin', component: AddProductAdminComponent ,canActivate: [AdminGuardFn]},
  { path: 'update-product/:id', component: UpdateProductComponent ,canActivate: [AdminGuardFn]},
  { path: 'app-category-admin', component: CategoryAdminComponent ,canActivate: [AdminGuardFn]},
  { path: 'app-add-category-admin', component: AddCategoryAdminComponent ,canActivate: [AdminGuardFn]},
  { path: 'app-user-list-admin', component: UserListAdminComponent ,canActivate: [AdminGuardFn]},
  { path: 'app-update.category.admin/:id', component: UpdateCategoryAdminComponent ,canActivate: [AdminGuardFn]},
  { path: 'profile', component: UserDetailComponent, canActivate: [AuthGuardFn] },
  { path: '**', redirectTo: '' } 
];
