import { Component, OnInit,ApplicationRef } from '@angular/core';
import { CartService } from '../../service/card.service';
import { ProductService } from '../../service/product.service';
import { CommonModule } from '@angular/common';
import { Product } from '../../responses/user/product';
import { HttpClientModule } from '@angular/common/http';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { OrderService } from '../../service/order.service';
import { OrderDTO } from '../../responses/user/orderDTO';
import { RouterModule } from '@angular/router';
import { Router } from '@angular/router';
import { TokenService } from '../../service/token.service';
import { HeaderComponent } from '../../header/header.component';
import { FooterComponent } from '../../footer/footer.component';
import { FormsModule } from '@angular/forms';







@Component({
  selector: 'app-order',
  standalone: true,
  imports: [CommonModule, HttpClientModule,ReactiveFormsModule,RouterModule,HeaderComponent,FooterComponent,FormsModule],
  templateUrl: './oder.component.html',
  styleUrls: ['./oder.component.scss'] // Chỉnh sửa thành styleUrls
})
export class OderComponent implements OnInit {
  orderForm! :FormGroup;
  cartItems: { product: Product; quantity: number }[] = []; // Thông tin giỏ hàng
  // couponCode:'';
  user:any;
  totalAmount: number = 0; // Tổng tiền
  discountAmount: number = 0; // Số tiền giảm giá
  finalAmount: number = 0; // Tổng tiền sau giảm giá
  couponCode: string = ''; // Mã giảm giá người dùng nhập
  orderData: OrderDTO = {
    id:0,
    user_id: 0, // Thay bằng user_id thích hợp
    fullname: '',
    email: '',
    phone_number: '',
    address: '',
    note: '',
    total_money: 0,
    payment_method: 'cod', // Mặc định là thanh toán khi nhận hàng (COD)
    shipping_method: 'express', // Mặc định là vận chuyển nhanh
    coupon_code: '', // Mã giảm giá
    order_date: new Date(),
    active:'',
    cart_items: [],// Mảng chứa sản phẩm trong giỏ hàng
    orderDetail: [], 
  };

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private orderService: OrderService,
    private fb:FormBuilder,
    private appRef: ApplicationRef,
    private router:Router,
    private tokenService:TokenService

  ) {
   
  }

  ngOnInit(): void {
     this.appRef.isStable.subscribe((isStable: any) => {
        if (isStable) {
            console.log('Ứng dụng đã ổn định');
        }
    });
    this.orderForm = this.fb.group({
      fullname: ['', Validators.required],  // Bắt buộc nhập họ tên
      email: ['', [Validators.required, Validators.email]],  // Bắt buộc nhập email đúng định dạng
      phone_number: ['', [Validators.required, Validators.minLength(6)]],  // Bắt buộc nhập và ít nhất 6 ký tự
      address: ['', [Validators.required, Validators.minLength(5)]],  // Bắt buộc nhập và ít nhất 5 ký tự
      note: [''],  // Không bắt buộc
      shipping_method: ['express', Validators.required],  // Bắt buộc chọn phương thức vận chuyển
      payment_method: ['cod', Validators.required],  // Bắt buộc chọn phương thức thanh toán
      
    });
    debugger;
    //this.cartService.clearCart();
    this.orderData.user_id=this.tokenService.getUserId();
    debugger 
    const cart = this.cartService.getCart();
    console.log('Cart data:', cart);
    const productIds = Array.from(cart.keys()); // Chuyển danh sách ID từ Map giỏ hàng

    
      // Gọi service để lấy thông tin sản phẩm dựa trên danh sách ID
if(productIds.length===0){
  return;
}
      this.productService.getProductsByIds(productIds).subscribe({
        next: (products: Product[]) => {
          console.log('Fetched products:', products);
          // Lấy thông tin sản phẩm và số lượng từ danh sách sản phẩm và giỏ hàng
          this.cartItems = productIds.map((productId) => {
            const product = products.find((p) => p.id === productId);
            if (product) {
              product.thumbnail = `http://localhost:8080/api/v1/products/images/${product.thumbnail}`;
            }
            return {
              product: product!,
              quantity: cart.get(productId)!
            };
          });

          // Tính tổng tiền sau khi đã cập nhật cartItems
          this.calculateTotal();
        },
        error: (error: any) => {
          console.error('Error fetching detail:', error);
        }
      });
  
  }
  placeOrder(): void {
    debugger; // Kiểm tra trạng thái ban đầu
    if (this.orderForm.valid) {
        // Lấy userId từ thông tin người dùng đã lấy từ token
        const token = this.tokenService.getToken();
        console.log('Token from localStorage:', token); // Kiểm tra token
      
        const userId = this.tokenService.getUserId();
        console.log('User ID from token:', userId);
      
        if (userId == null || userId <= 0) {
          alert('Không thể xác định người dùng. Vui lòng đăng nhập lại.');
          this.router.navigate(['/login']);
          return;
      }
      

        // Chuẩn bị dữ liệu orderData
        this.orderData = {
            ...this.orderData,
            ...this.orderForm.value,
            user_id: userId,  // Gán userId vào orderData
            total_money: this.finalAmount
        };
        
        // Kiểm tra xem giỏ hàng có chứa sản phẩm không
        if (!this.cartItems || this.cartItems.length === 0) {
            alert('Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng trước khi đặt hàng.');
            return;
        }

        // Map lại dữ liệu cart_items
        this.orderData.cart_items = this.cartItems.map(cartItem => ({
            product_id: cartItem.product.id!, // Kiểm tra xem cartItem.product.id có tồn tại không
            quantity: cartItem.quantity
        }));

        // Gán giá trị total_money
        this.orderData.total_money = this.totalAmount;
        
        // Log dữ liệu để kiểm tra trước khi gửi
        console.log('Dữ liệu trước khi gửi:', this.orderData);

        // Gửi yêu cầu đặt hàng
        this.orderService.placeOrder(this.orderData).subscribe({
            next: (response: any) => {
                debugger;
                console.log('Đặt hàng thành công', response);
                alert('Đặt hàng thành công');
                this.cartService.clearCart();
                this.router.navigate(['/']);
            },
            complete: () => {
                console.log('Yêu cầu hoàn tất');
                this.calculateTotal();
            },
            error: (error: any) => {
                debugger;
                console.error('Chi tiết lỗi từ API:', error);
                alert('Lỗi khi đặt hàng. Vui lòng thử lại.');
            }
        });
    } else {
        alert('Dữ liệu không hợp lệ. Vui lòng kiểm tra lại');
    }
}





applyCoupon(): void {
  if (!this.couponCode) {
    alert('Vui lòng nhập mã giảm giá.');
    return;
  }

  this.orderService.validateCoupon(this.couponCode, this.totalAmount).subscribe({
    next: (response: any) => {
      this.discountAmount = this.calculateDiscount(response);
      this.finalAmount = this.totalAmount - this.discountAmount;

      this.orderData.coupon_code = this.couponCode;
      this.orderData.total_money = this.finalAmount; // Lưu giá trị sau khi giảm giá
      
    },
    error: (error: any) => {
      console.error('Coupon validation error:', error);
      alert('Mã giảm giá không hợp lệ hoặc đã hết hạn.');
    },
  });
}

calculateDiscount(coupon: any): number {
  if (coupon.discountType === 'percentage') {
    let discount = (this.totalAmount * coupon.discountValue) / 100;
    if (coupon.maxDiscountValue && discount > coupon.maxDiscountValue) {
      discount = coupon.maxDiscountValue;
    }
    return discount;
  } else if (coupon.discountType === 'fixed') {
    return coupon.discountValue;
  }
  return 0;
}

  
  calculateTotal(): void {
   
    this.totalAmount = this.cartItems.reduce(
      (total, item) => total + (item.product.price * item.quantity), 0
    );
    console.log('Total amount:', this.totalAmount); // Debugging
  }
  removeItem(productId: number): void {
    this.cartService.removeFromCart(productId); // Gọi phương thức xóa sản phẩm
    this.cartItems = this.cartItems.filter(item => item.product.id !== productId); // Cập nhật lại giỏ hàng sau khi xóa sản phẩm
    this.calculateTotal(); // Tính lại tổng tiền sau khi xóa sản phẩm
    if (this.finalAmount < this.discountAmount) {
      this.discountAmount = 0; // Reset giảm giá
      this.orderData.coupon_code = ''; // Xóa mã giảm giá
      alert('Mã giảm giá không còn hợp lệ do giỏ hàng không đáp ứng điều kiện.');
    }
  }
  increaseQuantity(productId: number): void {
    const item = this.cartItems.find(cartItem => cartItem.product.id === productId);
    if (item) {
      item.quantity += 1; // Tăng số lượng
      this.cartService.updateCart(productId, item.quantity); // Cập nhật giỏ hàng
      this.calculateTotal(); // Tính lại tổng tiền
    }
  }

  decreaseQuantity(productId: number): void {
    const item = this.cartItems.find(cartItem => cartItem.product.id === productId);
    if (item) {
      if (item.quantity > 1) {
        item.quantity -= 1; // Giảm số lượng nếu lớn hơn 1
        this.cartService.updateCart(productId, item.quantity); // Cập nhật giỏ hàng
      } else {
        this.removeItem(productId); // Xóa sản phẩm nếu số lượng bằng 1
      }
      this.calculateTotal(); // Tính lại tổng tiền
    }
  }

  updateQuantity(productId: number, event: any): void {
    const value = +event.target.value; // Chuyển giá trị input sang số
    if (!isNaN(value) && value > 0) {
      const item = this.cartItems.find(cartItem => cartItem.product.id === productId);
      if (item) {
        item.quantity = value; // Cập nhật số lượng
        this.cartService.updateCart(productId, item.quantity); // Cập nhật giỏ hàng
        this.calculateTotal(); // Tính lại tổng tiền
        if (this.finalAmount < this.discountAmount) {
          this.discountAmount = 0; // Reset giảm giá
          this.orderData.coupon_code = ''; // Xóa mã giảm giá
          alert('Mã giảm giá không còn hợp lệ do giỏ hàng không đáp ứng điều kiện.');
        }
      }
    }
  }
 

}
