import { Component, OnInit,Input } from '@angular/core';
import { Category } from '../../../responses/user/category';

import { CommonModule } from '@angular/common';
import { AddCategoryAdminComponent } from '../addCategory/add.category.admin.component';
import { CategoryService } from '../../../service/category.service';
import { FormsModule } from '@angular/forms';
import { UpdateCategoryAdminComponent } from '../updateCategory/update.category.admin.component';

@Component({
  selector: 'app-category-admin',
  standalone: true,
  templateUrl: './category.admin.component.html',
  styleUrls: ['./category.admin.component.scss'],
  imports: [CommonModule, AddCategoryAdminComponent,FormsModule,UpdateCategoryAdminComponent],
})
export class CategoryAdminComponent implements OnInit {
  @Input() categoryId: string | null = null;
  categories: Category[] = []; // Danh sách categories
  currentPage: number = 1; // Trang hiện tại
  limit: number = 5; // Số lượng categories mỗi trang
  error: string | null = null; // Lưu lỗi (nếu có)
  isLoading: boolean = false; // Trạng thái tải dữ liệu
  isAddModalOpen: boolean = false; // Kiểm soát trạng thái của modal thêm
  searchKeyword: string = '';
  isUpdateModalOpen: boolean = false;
  selectedCategory: Category | null = null;
  constructor(private categoryService: CategoryService) {}

  ngOnInit(): void {
    this.loadCategories(this.currentPage, this.limit);
  }

  // Tải danh sách danh mục
  loadCategories(page: number, limit: number): void {
    this.isLoading = true; // Bắt đầu tải dữ liệu
    this.categoryService.getCategories(page, limit).subscribe({
      next: (data: Category[]) => {
        this.categories = data;
        this.isLoading = false; // Hoàn tất tải dữ liệu
      },
      error: (err: { message: any }) => {
        this.error = `Error: ${err.message}`; // Xử lý lỗi
        this.isLoading = false; // Hoàn tất tải dữ liệu
      },
    });
  }

  // Mở modal thêm danh mục
  openAddModal(): void {
    this.isAddModalOpen = true;
  }

  // Đóng modal thêm danh mục
  closeAddModal(): void {
    this.isAddModalOpen = false;
  }

  // Xử lý khi một sản phẩm được thêm
  onCategoryAdded(): void {
    this.loadCategories(this.currentPage, this.limit); // Reload danh sách danh mục
    this.closeAddModal(); // Đóng modal
  }
  deleteCategory(id: number): void {
    const confirmed = confirm('Bạn có chắc chắn muốn xoá');
    if (confirmed) {
      this.categoryService.deleteCategory(id).subscribe({
        next: (response: any) => {
          alert(response.message); // Hiển thị thông báo từ phản hồi API
          this.loadCategories(this.currentPage, this.limit); // Tải lại danh sách danh mục
        },
        error: (err: { error: { message: any; }; }) => {
          console.error('Error deleting category:', err);
          alert(err?.error?.message || 'Failed to delete category.');
        },
      });
    }
  }
  searchCategories(): void {
    if (this.searchKeyword.trim() === '') {
      alert('Please enter a search keyword.'); // Cảnh báo nếu không nhập từ khóa
      return;
    }
    this.isLoading = true;
    this.categoryService.searchCategoriesByName(this.searchKeyword).subscribe({
      next: (response: { data: Category[]; }) => {
        console.log('Search results:', response.data); // Log kết quả
        this.categories = response.data; // Cập nhật danh sách hiển thị
        this.isLoading = false;
      },
      error: (err: { error: { message: any; }; }) => {
        console.error('Error searching categories:', err);
        alert(err?.error?.message || 'Failed to search categories.');
        this.isLoading = false;
      },
    });
  }
  openUpdateModal(category: Category): void {
    this.selectedCategory = category;
    this.isUpdateModalOpen = true;
  }

  closeUpdateModal(): void {
    this.isUpdateModalOpen = false;
    this.selectedCategory = null;
  }

  onCategoryUpdated(): void {
    this.loadCategories(this.currentPage, this.limit);
   
    this.closeUpdateModal();
  }
}
