import { Component, OnInit } from '@angular/core';
import { Product } from '../../../responses/user/product';
import { ProductService } from '../../../service/product.service';
import { CommonModule } from '@angular/common';
import { CategoryService } from '../../../service/category.service';
import { Category } from '../../../responses/user/category';
import { FormsModule } from '@angular/forms'; 
import { RouterModule } from '@angular/router';

import { UpdateProductComponent } from '../updateProduct/update.product.component';
import { AddProductAdminComponent } from '../addProduct/add.product.admin.component';


@Component({
  selector: 'app-product-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, AddProductAdminComponent, UpdateProductComponent],
  templateUrl: './product.admin.component.html',
  styleUrls: ['./product.admin.component.scss']
})
export class ProductAdminComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = [];
  selectedCategoryId: number = 0;
  currentPage: number = 1;
  itemsPerPage: number = 16;
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = ""; 
  isAddModalOpen: boolean = false;
  isUpdateModalOpen: boolean = false;
  selectedProduct: Product | null = null;

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService
    
  ) {}

  ngOnInit() {
    this.getCategoriesAndProducts();
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
  }


  searchProducts() {
    this.currentPage = 1;
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
  }

 
  getCategoriesAndProducts() {
    this.categoryService.getCategories(1, 100).subscribe({
      next: (categories: Category[]) => {
        this.categories = categories;
        this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
      },
      error: (error: any) => {
        console.error('Error fetching categories:', error);
      }
    });
  }
  getCategoryName(categoryId: number): string {
    const category = this.categories.find(cat => cat.id === categoryId);
    return category ? category.name : 'Unknown Category';
  }
  
  
  
  
  getProducts(keyword: string, selectedCategoryId: number, page: number, limit: number): void {
    this.productService.getProducts(selectedCategoryId, keyword, page - 1, limit).subscribe({
      next: (response: any) => {
        if (response.products) {
          response.products.forEach((product: Product) => {
            product.url = `http://localhost:8080/api/v1/products/images/${product.thumbnail}`;
           
          });
          this.products = response.products;
          this.totalPages = response.totalPages;
          this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
        } else {
          console.warn('No products found in the response.');
        }
      },
      error: (error: any) => {
        console.error('Error fetching products:', error);
      }
    });
  }
  
  
  

  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);
    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }

    if (totalPages < maxVisiblePages) {
      startPage = 1;
      endPage = totalPages;
    }

    return new Array(endPage - startPage + 1).fill(0).map((_, index) => startPage + index);
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
  }

  onDeleteProduct(productId: number) {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(productId).subscribe({
        next: () => {
          console.log('Product deleted successfully');
          this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
        },
        error: (error: { status: any; message: any; }) => {
          console.error('Error deleting product:', error);
          alert(`Failed to delete product. Error: ${error.status} - ${error.message}`);
        }
      });
    }
  }

  openAddModal(): void {
    this.isAddModalOpen = true;
  }

  closeAddModal(): void {
    this.isAddModalOpen = false;
  }

  onProductAdded(): void {
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
    this.closeAddModal();
  }

  openUpdateModal(product: Product): void {
    this.selectedProduct = product;
    this.isUpdateModalOpen = true;
  }

  closeUpdateModal(): void {
    this.isUpdateModalOpen = false;
    this.selectedProduct = null;
  }

  onProductUpdated(): void {
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
    this.closeUpdateModal();
  }
}
