  import { Component, OnInit } from '@angular/core';

  import { Product } from '../../responses/user/product';
  import { ProductService } from '../../service/product.service';
  import { CommonModule } from '@angular/common'; // Import CommonModule
  import { Category } from '../../responses/user/category';
  import { CategoryService } from '../../service/category.service';
  import { FormsModule } from '@angular/forms'; 
  import { Router } from '@angular/router';
 
  import { RouterModule } from '@angular/router';
import { OderComponent } from '../oder/oder.component';

import { HeaderComponent } from '../../header/header.component';
import { AuthGuardFn } from '../../service/Guard/auth.guard';
import { FooterComponent } from '../../footer/footer.component';


  @Component({
    selector: 'app-home',
    standalone: true,
    imports: [CommonModule,FormsModule,RouterModule,HeaderComponent,FooterComponent], // Đảm bảo thêm CommonModule vào đây
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
  })
  export class HomeComponent implements OnInit {
    products: Product[] = [];
    categories: Category[] = [];
    selectedCategoryId: number = 0;
    currentPage: number = 1;
    itemsPerPage: number = 9;
    totalPages: number = 0;
    visiblePages: number[] = [];
    keyword: string = ""; // Đảm bảo là kiểu string

    constructor(private productService: ProductService, private categoryService: CategoryService,private router: Router) {
     
    }

    ngOnInit() {
      this.getCategories(1, 100); // Lấy danh sách categories khi khởi động
      this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage); // Lấy sản phẩm
    }

    searchProducts() {
      this.currentPage = 1; // Đặt lại trang hiện tại về 1
      this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage); // Gọi lại phương thức lấy sản phẩm
    }

    getCategories(page: number, limit: number) {
      this.categoryService.getCategories(page, limit).subscribe({
        next: (categories: Category[]) => {
          this.categories = categories; // Gán danh sách categories
        },
        error: (error: any) => {
          console.error('Error fetching categories:', error); // Xử lý lỗi
        }
      });
    }

    getProducts(keyword: string, selectedCategoryId: number, page: number, limit: number): void {
      this.productService.getProducts(selectedCategoryId, keyword, page - 1, limit).subscribe({ // Đảm bảo thứ tự các tham số đúng
        next: (response: any) => {
          debugger
          if (response.products) {
            response.products.forEach((product: Product) => {
              product.url = `http://localhost:8080/api/v1/products/images/${product.thumbnail}`; // Gán URL cho sản phẩm
              console.log(product.thumbnail);

            });
            this.products = response.products; // Cập nhật danh sách sản phẩm
            this.totalPages = response.totalPages; // Cập nhật tổng số trang
            this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages); // Cập nhật các trang hiển thị
          } else {
            console.warn('No products found in the response.');
          }
        },
        error: (error: any) => {
          console.error('Error fetching products:', error); // Xử lý lỗi
        }
      });
    }

    generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
      const maxVisiblePages = 5;
      const halfVisiblePages = Math.floor(maxVisiblePages / 2);
      let startPage = Math.max(currentPage - halfVisiblePages, 1);
      let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

      // Điều chỉnh lại startPage nếu endPage chưa đủ số lượng trang cần hiển thị
      if (endPage - startPage + 1 < maxVisiblePages) {
        startPage = Math.max(endPage - maxVisiblePages + 1, 1);
      }

      // Nếu tổng số trang ít hơn maxVisiblePages, điều chỉnh để hiển thị tất cả các trang
      if (totalPages < maxVisiblePages) {
        startPage = 1;
        endPage = totalPages;
      }

      // Tạo mảng các trang hiển thị từ startPage đến endPage
      return new Array(endPage - startPage + 1).fill(0).map((_, index) => startPage + index);
    }

    onPageChange(page: number): void {
      this.currentPage = page; // Cập nhật trang hiện tại
      this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage); // Gọi lại phương thức để lấy sản phẩm cho trang mới
    }
    onProductClick(productId: number) {
      debugger; // Dừng ở đây để kiểm tra giá trị nếu cần
    
      // Điều hướng đến trang detail-product với productId là tham số
      this.router.navigate(['/products', productId]);
    }

    setupRoutes() {
      this.router.resetConfig([
       
        {
          path: 'order',
          component: OderComponent, // Route cho giỏ hàng
          canActivate: [AuthGuardFn] // Bảo vệ route giỏ hàng
        },
        // Các route khác...
      ]);
    }
    filterByCategory(categoryId: number): void {
      this.selectedCategoryId = categoryId; // Cập nhật danh mục được chọn
      this.currentPage = 1; // Reset về trang đầu tiên
      this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage); // Lấy lại sản phẩm dựa trên danh mục mới
    }
    
    
  }
