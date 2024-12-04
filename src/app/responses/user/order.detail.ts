import { Product } from './product';

export interface OrderDetail {
  id: number;
  order_id: number;
  product_id: number;
  price: number;
  number_of_products: number;
  total_money: number | null;
  color: string | null;
  thumbnail?: string; // Tùy chọn, sẽ được thêm nếu bạn lấy hình ảnh từ một service khác
}

