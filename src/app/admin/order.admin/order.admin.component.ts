import { Component, OnInit } from '@angular/core';
import { OrderResponse } from '../../responses/user/order.responses';
import { OrderService } from '../../service/order.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-order-admin',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './order.admin.component.html',
  styleUrls: ['./order.admin.component.scss'] // Sửa thành styleUrls với "s" ở cuối
})
export class OrderAdminComponent implements OnInit {
  orders: OrderResponse[] = [];
  currentPage: number = 1;
  itemsPerPage: number = 10;
  pages: number[] = [];
  totalPages: number = 0;
  keyword: string = "";
  visiblePages: number[] = [];

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.getAllOrders(this.keyword, this.currentPage, this.itemsPerPage);
  }

  getAllOrders(keyword: string, page: number, limit: number): void {
    this.orderService.getAllOrders(keyword, page - 1, limit).subscribe({
      next: (response: any) => {
        this.orders = response.orders;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      error: (error: any) => {
        console.error('Error fetching products:', error);
      }
    });
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.getAllOrders(this.keyword, this.currentPage, this.itemsPerPage);
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
  deleteOrder(orderId: number) {
    this.orderService.deleteOrder(orderId).subscribe({
      next: (response: { message: any; }) => {
        alert(response.message || 'Đã xoá thành công');
        
        // Cập nhật trạng thái activeStatus của đơn hàng trong danh sách hiện tại
        const order = this.orders.find(o => o.id === orderId);
        if (order) {
          order.active = "Đã dừng"; // Thay đổi trạng thái hiển thị trên bảng
        }
      },
      error: (error: { status: number; }) => {
        console.error('Error deactivating order:', error);
  
        if (error.status === 403) {
          alert('Bạn không có quyền thực hiện thao tác này.');
        } else if (error.status === 404) {
          alert('Đơn hàng không tồn tại.');
        } else {
          alert('Không thể cập nhật trạng thái đơn hàng.');
        }
      }
    });
  }
  
  
  
}
