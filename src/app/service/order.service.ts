import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { OrderDTO } from '../responses/user/orderDTO';
import { OrderResponse } from '../responses/user/order.responses';
import { TokenService } from './token.service';
import { OrderDetail } from '../responses/user/order.detail';
import { Product } from '../responses/user/product';
import { OrderDetailDTO } from '../responses/user/orderDetailDTO';
import { map } from 'rxjs/operators';
import { Order } from '../responses/user/order';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8080/api/v1/orders'; // URL của API cho các thao tác liên quan đến đơn hàng
  private apiGetAllOrders = 'http://localhost:8080/api/v1/orders/get-orders-by-keyword';
  private apiUrlDetail = 'http://localhost:8080/api/v1/orders_details';
  private productUrl = 'http://localhost:8080/api/v1/products';
  private a='http://localhost:8080/api/v1/coupons';

  constructor(private http: HttpClient, private tokenService: TokenService) {}

  // Phương thức đặt hàng
  placeOrder(orderData: OrderDTO): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}`, orderData);
  }
  validateCoupon(couponCode: string, orderTotal: number): Observable<any> {
    // Lấy token từ TokenService
    const token = this.tokenService.getToken();
  
    // Kiểm tra token có tồn tại không
    if (!token) {
      console.error('Token không tồn tại. Vui lòng đăng nhập lại.');
      alert('Vui lòng đăng nhập để sử dụng mã giảm giá.');
      // Sử dụng throwError để trả về Observable lỗi
      return throwError(() => new Error('Token không tồn tại'));
    }
  
    // Tạo payload dữ liệu gửi đi
    const payload = { couponCode, orderTotal };
  
    // Cấu hình headers với token
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  
    // Gửi request POST tới endpoint validate coupon
    return this.http.post(`${this.a}/validate`, payload, { headers });
  }
  
  
  



  getOrdersByStatus(status: string): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(`${this.apiUrl}/orders/search?status=${status}`);
  }
  // Phương thức lấy thông tin đơn hàng
  getOrderById(orderId: number): Observable<OrderDTO> {
    return this.http.get<OrderDTO>(`${this.apiUrl}/${orderId}`);
  }
 
  getOrderDetails(orderId: number): Observable<OrderDTO> {
    const url = `${this.apiUrl}/${orderId}`; // Tạo URL truy vấn theo orderId
    return this.http.get<OrderDTO>(url); // Trả về Observable chứa dữ liệu OrderDTO
  }
  // Lấy chi tiết đơn hàng theo orderId
 // OrderService
 getOrdersByUserID(userId: number): Observable<OrderResponse[]> {
  return this.http.get<OrderResponse[]>(`${this.apiUrl}/user/${userId}`);
}

  getOrderDetailById(orderDetailId: number): Observable<OrderDetailDTO> {
    return this.http.get<OrderDetailDTO>(`${this.apiUrlDetail}/detail/${orderDetailId}`);
  }

  // Lấy thông tin sản phẩm theo productId
  getProductById(productId: number): Observable<Product> {
    return this.http.get<Product>(`${this.productUrl}/${productId}`);
  }

  
  // Lấy danh sách đơn hàng theo userId
  getOrderDetailsByUserId(userId: number): Observable<OrderDetailDTO[]> {
    return this.http.get<OrderDetailDTO[]>(`${this.apiUrlDetail}/order_user/${userId}`);
  }
  getOrdersByUserId(userId: number): Observable<OrderDTO[]> {
    const url = `${this.apiUrl}/user/${userId}`;
    return this.http.get<OrderDTO[]>(url);
  }

  // Phương thức hủy đơn hàng
  deleteOrder(orderId: number): Observable<any> {
    const token = this.tokenService.getToken();
    if (!token) {
      console.error('Token không tồn tại. Vui lòng đăng nhập lại.');
      return throwError(() => new Error('Token không tồn tại'));
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.delete(`${this.apiUrl}/${orderId}`, { headers, responseType: 'text' as 'json' });
  }

  // Lấy tất cả các đơn hàng dựa trên từ khóa, trang và giới hạn
  getAllOrders(keyword: string, page: number, limit: number): Observable<OrderResponse[]> {
    const params = new HttpParams()
      .set('keyword', keyword)
      .set('page', page.toString())
      .set('limit', limit.toString());

    return this.http.get<OrderResponse[]>(this.apiGetAllOrders, { params });
  }
}
