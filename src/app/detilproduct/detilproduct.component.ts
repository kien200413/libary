import { Component, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Product } from '../responses/user/product';
import { ProductImage } from '../responses/user/product.image';
import { CommonModule } from '@angular/common';
import { CartService } from '../service/card.service';
import { TokenService } from '../service/token.service';
import { Router,ActivatedRoute } from '@angular/router';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';




@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule,FooterComponent,HeaderComponent],
  templateUrl: './detilproduct.component.html',
  styleUrls: ['./detilproduct.component.scss']
})

export class ProductDetailComponent implements OnInit {
  product?: Product;
  productId: number = 0;
  currentImageIndex: number = 0;
  quantity: number = 1;

  constructor(
    private productService: ProductService,
    private cartService: CartService,
  private tokenService :TokenService,
  private router:Router,
  private activateRoute: ActivatedRoute,

  ) {}

  ngOnInit() {
    const idParam = this.activateRoute.snapshot.paramMap.get('id');
    
    if (idParam !== null) {
      this.productId = +idParam;
      console.log("Product ID:", this.productId);
    } else {
      console.error('Invalid productId:', idParam);
      return;
    }

    if (!isNaN(this.productId)) {
      this.productService.getDetailProduct(this.productId).subscribe({
        next: (response: any) => {
          console.log("API Response:", response); // Kiểm tra toàn bộ dữ liệu trả về từ API
        this.product = response;
        console.log("Product ID (from API):", this.product?.id); // Kiểm tra `id`
      
          if (this.product?.product_images) {
            this.product.product_images.forEach((product_image: ProductImage) => {
              // Kiểm tra từng image_url
              console.log("Original image_url:", product_image.imageUrl); // Log URL gốc từ API
              if (product_image.imageUrl && !product_image.imageUrl.startsWith("http://localhost:8080/api/v1/products/images/")) {
                product_image.imageUrl = `http://localhost:8080/api/v1/products/images/${product_image.imageUrl}`;
              } else {
                console.warn(`Missing or invalid image_url for product image with ID: ${product_image.id}`);
              }
            });
          }
      
          this.showImage(0); // Hiển thị ảnh đầu tiên
        },
        error: (error: any) => {
          console.error('Error fetching product details:', error);
        }
      });
      

    } else {
      console.error('Invalid productId:', this.productId);
    }
  }

  showImage(index: number): void {
    if (this.product && this.product.product_images && this.product.product_images.length > 0) {
      if (index < 0) {
        index = 0;
      } else if (index >= this.product.product_images.length) {
        index = this.product.product_images.length - 1;
      }
      this.currentImageIndex = index;
    }
  }

  addToCart(): void {
    if (this.tokenService.isTokenExpired()) {
      // Nếu token đã hết hạn hoặc không có token
      console.warn('Token has expired or does not exist. Redirecting to login...');
      this.router.navigate(['/login']);
      return; // Ngăn không cho tiếp tục thực hiện thêm sản phẩm vào giỏ hàng
    }
  
    if (this.product && this.product.id) {
      console.log('Adding to cart with productId:', this.product.id, 'Quantity:', this.quantity);
      this.cartService.addToCart(this.product.id, this.quantity);
      this.router.navigate(['/order']);
    } else {
      console.error('Product ID is undefined, cannot add to cart');
    }
  }
  
  

  thumbnailClick(index: number): void {
    this.currentImageIndex = index;
    this.showImage(index);
  }

  nextImage(): void {
    this.showImage(this.currentImageIndex + 1);
  }

  previousImage(): void {
    this.showImage(this.currentImageIndex - 1);
  }

  increaseQuantity(): void {
    this.quantity++;
  }

  decreaseQuantity(): void {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  updateQuantity(event: any): void {
    const value = +event.target.value;
    if (!isNaN(value) && value > 0) {
      this.quantity = value;
    } else {
      this.quantity = 1;
    }
  }

  buyNow(): void {
    console.log(`Mua ngay sản phẩm: ${this.product?.name}, số lượng: ${this.quantity}`);
  }
  
}
