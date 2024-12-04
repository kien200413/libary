import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ProductService } from '../../../service/product.service';
import { Product } from '../../../responses/user/product';
import { Category } from '../../../responses/user/category';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-update-product',
  standalone: true,
  templateUrl: './update.product.component.html',
  styleUrls: ['./update.product.component.scss'],
  imports: [ReactiveFormsModule, CommonModule],
  providers: [ProductService]
})
export class UpdateProductComponent implements OnInit {
  @Input() productId: number | null = null;
  @Input() product: Product | null = null;
  @Input() categories: Category[] = [];
  @Output() closeModal = new EventEmitter<void>();
  @Output() productUpdated = new EventEmitter<void>();

  updateProductForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService
  ) {
    // Tạo form
    this.updateProductForm = this.fb.group({
      id: [null],
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(200)]],
      price: ['', [Validators.required, Validators.min(0), Validators.max(10000000)]],
      author: [''],
      thumbnail: [''],
      description: [''],
      category_id: [null, [Validators.required]]
    });
  }

  ngOnInit() {
    console.log('ProductId received:', this.productId);  // Kiểm tra id khi nhận được
    if (this.productId) {
      this.getProductDetails(this.productId);
    } else if (this.product) {
      // Kiểm tra và điền id vào form
      this.updateProductForm.patchValue({
        ...this.product,
        categoryId: this.product.category_id, // Điền categoryId vào form
        id: this.product.id  // Đảm bảo điền id vào form
      });
    }
  }
  

  // Lấy chi tiết sản phẩm theo ID và điền vào form
  getProductDetails(productId: number) {
    this.productService.getDetailProduct(productId).subscribe({
      next: (product: Product | null) => {
        this.product = product;
        if (this.product) {
          console.log('Product details:', this.product); // Kiểm tra dữ liệu sản phẩm
          this.updateProductForm.patchValue({
            ...this.product,
            category_id: this.product.category_id, // Điền categoryId vào form
            id: this.product.id  // Đảm bảo điền id vào form
          });
          console.log('Updated form value with id:', this.updateProductForm.value); // Kiểm tra form đã có id
        }
      },
      error: (error) => {
        console.error('Error fetching product details:', error);
      }
    });
  }
  

  // Xử lý cập nhật sản phẩm
  onUpdateProduct() {
    console.log('Product ID before sending request:', this.productId);  // Kiểm tra productId
  
    if (this.updateProductForm.valid && this.productId !== null && this.productId !== undefined) {
      // Lấy dữ liệu từ form và chuẩn bị object để gửi đi
      const updatedProduct: Product = {
        ...this.updateProductForm.value,
        // Không cần gán lại id vào đây, vì nó đã có trong URL
      };
  
      console.log('Updated Product:', updatedProduct);  // Kiểm tra dữ liệu sản phẩm
  
      // Gửi yêu cầu PUT đến API với URL chứa productId
      this.productService.updateProduct(this.productId, updatedProduct).subscribe({
        next: () => {
          console.log('Product updated successfully');
          this.productUpdated.emit();  // Thông báo đã cập nhật xong
          this.closeModal.emit();  // Đóng modal
        },
        error: (error: any) => {
          console.error('Error updating product:', error);  // Xử lý lỗi nếu có
        }
      });
    } else {
      console.log('Form is invalid or productId is missing');
      console.log('Form values:', this.updateProductForm.value);  // In ra các giá trị trong form nếu có lỗi
    }
  }
  

  
  
  
  
} 