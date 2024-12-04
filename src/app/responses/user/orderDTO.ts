import { OrderDetailDTO } from "./orderDetailDTO";

export class OrderDTO {
  id: number;
  user_id: number;           
  fullname: string;          
  email: string;             
  phone_number: string;      
  address: string;           
  note: string;              
  total_money: number;       
  shipping_method: string;   
  payment_method: string;    
  coupon_code: string; 
  order_date: Date;      
  cart_items: {              
    product_id: number;      
    quantity: number;        
  }[];
  active: string;  // Thêm thuộc tính active
  orderDetail: OrderDetailDTO[] = [];
  constructor(data: any) {
    this.id=data.id;
    this.user_id = data.user_id;
    this.fullname = data.fullname;
    this.email = data.email;
    this.phone_number = data.phone_number;
    this.address = data.address;
    this.note = data.note;
    this.total_money = data.total_money;
    this.shipping_method = data.shipping_method;
    this.payment_method = data.payment_method;
    this.coupon_code = data.coupon_code || '';
    this.order_date=data.order_date;
    this.cart_items = data.cart_items || [];
    this.active = data.active || 'Đang hoạt động'; // Giá trị mặc định
    this.orderDetail = data.orderDetails || [];
  }
}
