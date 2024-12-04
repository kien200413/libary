import { Component, OnInit } from '@angular/core';
import { CartService } from '../../service/card.service';
import { ProductService } from '../../service/product.service';
import { CommonModule } from '@angular/common';
import { Product } from '../../responses/user/product';
import { HttpClientModule } from '@angular/common/http';
import { HeaderComponent } from '../../header/header.component';

@Component({
  selector: 'app-order-confirm',
  standalone: true,
  imports: [CommonModule, HttpClientModule,HeaderComponent],
  templateUrl: './oder-confirm.component.html',
  styleUrls: ['./oder-confirm.component.scss'] // Đảm bảo rằng styleUrls là đúng tên và đường dẫn
})
export class OrderConfirmComponent implements OnInit {
  cartItems: { product: Product, quantity: number }[] = []; // Khai báo biến lưu trữ thông tin giỏ hàng
  totalAmount: number = 0;
  constructor(
    private cartService: CartService, // Inject CartService
    private productService: ProductService // Inject ProductService
  ) { }

  ngOnInit(): void {
    const cart = this.cartService.getCart();
    console.log('Cart data:', cart);
    const productIds = Array.from(cart.keys()); // Chuyển danh sách ID từ Map giỏ hàng

    if (productIds.length > 0) {
      debugger;
      // Gọi service để lấy thông tin sản phẩm dựa trên danh sách ID
      this.productService.getProductsByIds(productIds).subscribe({

        next: (products: Product[]) => {
          debugger;
          console.log('Fetched products:', products);
          // Lấy thông tin sản phẩm và số lượng từ danh sách sản phẩm và giỏ hàng
          this.cartItems = productIds.map((productId) => {
            const product = products.find((p) => p.id === productId);
            if (product) {
              product.thumbnail = `http://localhost:8080/api/v1/products/images/${product.thumbnail}`;
            }
            return {
              product: product!,
              quantity: cart.get(productId)!,
            };
          });
        },
        complete: () => {
          this.calculateTotal();
        },
        error: (error: any) => {
          console.error('Error fetching detail:', error);
        },
      });
    } else {
      console.warn('No products found in cart.');
    }
  }
  calculateTotal(): void {

    console.log('Calculating total for cartItems:', this.cartItems); // Debugging
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + (item.product.price * item.quantity), 0
    );
    console.log('Total amount:', this.totalAmount); // Debugging
  }

}
