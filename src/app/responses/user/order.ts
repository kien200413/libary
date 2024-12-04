export interface Order {
  id: number;
    user_id: number;           // ID người dùng
  fullname: string;          // Họ tên
  email: string;             // Địa chỉ email
  phone_number: string;      // Số điện thoại
  address: string;           // Địa chỉ giao hàng
  note: string;              // Ghi chú
  total_money: number;       // Tổng tiền
  shipping_method: string;   // Phương thức vận chuyển
  payment_method: string;    // Phương thức thanh toán
  coupon_code: string;       // Mã giảm giá (nếu có)
  active:string;
  order_date: Date;
  cart_items: {              // Danh sách sản phẩm trong giỏ hàng
    product_id: number;      // ID sản phẩm
    quantity: number;        // Số lượng
  }[];

}