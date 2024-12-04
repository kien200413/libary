import { Component, OnInit, Output, EventEmitter, Inject, PLATFORM_ID } from '@angular/core';
import { ProductService } from '../../../service/product.service';
import { FormsModule } from '@angular/forms';
import { Category } from '../../../responses/user/category';
import { CategoryService } from '../../../service/category.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-product-admin',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add.product.admin.component.html',
  styleUrls: ['./add.product.admin.component.scss']
})
export class AddProductAdminComponent implements OnInit {
  @Output() productAdded = new EventEmitter<void>();
  
  product = {
    name: '',
    price: 0,
    thumbnail: '',
    description: '',
    category_id: 1,
    keyword: '',
    author: '',
    url: '',
    product_images: []
  };
  showForm: boolean = true;
  categories: Category[] = [];
  selectedFiles: File[] = [];

  constructor(
    private productService: ProductService,
    private categoryService: CategoryService,
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.getCategories();
    }
  }

  getCategories() {
    this.categoryService.getCategories(1, 100).subscribe({
      next: (categories: Category[]) => {
        this.categories = categories;
      },
      error: (error: any) => {
        console.error('Error fetching categories:', error);
      }
    });
  }

  onFilesSelected(event: Event) {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files) {
      this.selectedFiles = Array.from(fileInput.files);
    }
  }

  onAddProduct() {
    this.productService.addProduct(this.product).subscribe(
      (response: any) => {
        if (this.selectedFiles.length > 0 && response.id) {
          const formData = new FormData();
          this.selectedFiles.forEach((file) => {
            formData.append('files', file, file.name);
          });

          this.productService.uploadProductImage(response.id, formData).subscribe(
            () => {
              alert('Product and images added successfully!');
              this.productAdded.emit();
              this.resetForm();
              this.closeModal();
            },
            (error: any) => {
              console.error('Error uploading images:', error);
            }
          );
        } else {
          alert('Product added successfully!');
          this.productAdded.emit();
          this.resetForm();
          this.closeModal();
        }
      },
      (error: any) => {
        console.error('Error adding product:', error);
      }
    );
  }

  private resetForm() {
    this.product = {
      name: '',
      price: 0,
      thumbnail: '',
      description: '',
      category_id: 1,
      keyword: '',
      author: '',
      url: '',
      product_images: []
    };
    this.selectedFiles = [];
  }

  private closeModal() {
    if (isPlatformBrowser(this.platformId)) {
      import('bootstrap').then((bootstrap) => {
        const modalElement = document.getElementById('addProductModal');
        if (modalElement) {
          const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
          modalInstance.hide();
        }

        // Sử dụng setTimeout để loại bỏ lớp và backdrop sau khi modal đóng
        setTimeout(() => {
          document.body.classList.remove('modal-open');
          const modalBackdrop = document.querySelector('.modal-backdrop');
          if (modalBackdrop) {
            modalBackdrop.remove();
          }
        }, 300); // Đảm bảo thời gian phù hợp để hiệu ứng đóng modal hoàn tất
      });
    }
  }
}
