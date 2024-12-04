import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
// Dịch vụ gọi API
import { CommonModule } from '@angular/common';
import { OrderResponse } from '../../responses/user/order.responses';
import { OrderService } from '../../service/order.service';
import { OrderDTO } from '../../responses/user/orderDTO';
import { HeaderComponent } from '../../header/header.component';

@Component({
  selector: 'app-detail',
  standalone: true,
  imports: [CommonModule,HeaderComponent],
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {
  orders: OrderResponse[] = []; // Danh sách đơn hàng
   filteredOrders: OrderResponse[] = [];
  loading: boolean = true; // Trạng thái loading
  error: string | null = null; // Trạng thái lỗi
  private baseUrl: string = 'http://localhost:8080/api/v1/products/images/';
  statuses: string[] = ['pending', 'processing', 'shipped', 'delivered']; // Các trạng thái đơn hàng
  selectedStatus: string = 'pending'; // Trạng thái mặc định

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    const userId = 6; // Thay bằng user_id thực tế
    this.fetchOrders(userId);
    this.getOrdersByStatus(this.selectedStatus);
  }
  

  getProductImageUrl(thumbnail: string): string {
    return `${this.baseUrl}${thumbnail}`;
  }
  // Lấy danh sách đơn hàng từ API
  fetchOrders(userId: number): void {
    this.loading = true;
    this.orderService.getOrdersByUserID(userId).subscribe({
      next: (data: OrderResponse[]) => {
        this.orders = data; // Lưu dữ liệu vào biến `orders`
        console.log('Orders:', JSON.stringify(this.orders, null, 2));
        this.filteredOrders = [...this.orders]; 
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error fetching orders:', err);
        this.error = 'Failed to load orders.';
        this.loading = false;
      },
    });
  }
  getOrdersByStatus(status: string): void {
    this.orderService.getOrdersByStatus(status).subscribe((data: OrderResponse[]) => {
      this.orders = data;
    });
  }

  // Xử lý khi người dùng click vào trạng thái
  onStatusClick(status: string): void {
    this.selectedStatus = status;
    this.getOrdersByStatus(status); // Gọi API để lấy các đơn hàng theo trạng thái
  }
}