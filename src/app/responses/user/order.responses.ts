import { OrderDetail } from "./order.detail";
import { OrderDetailDTO } from "./orderDetailDTO";

export interface OrderResponse {
    id: number;
    user_id: number;
    fullname: string;
    phone_number: string;
    email: string;
    address: string;
    note: string;
    order_date: Date;
    status: string;
    total_money: number;
    shipping_method: string;
    shipping_address: string;
    shippingDate: Date;
    payment_method: string;
    
    active:string;
    coupon_code: string; 
    cart_items: {              
      product_id: number;      
      quantity: number; 
             
    }[];
    orderDetails: OrderDetailDTO[];// Mảng chi tiết đơn hàng
  }