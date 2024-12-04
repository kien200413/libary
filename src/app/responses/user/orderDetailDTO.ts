import { Product } from "./product";

export class OrderDetailDTO {
  id: number;
  product: Product;
  price: number;
  numberOfProducts: number;
  totalMoney: number | null;
  color: string | null;

  constructor(data: any) {
    this.id = data.id;
    this.product = data.product || {}; // Sử dụng đối tượng product từ API
    this.price = data.price;
    this.numberOfProducts = data.numberOfProducts;
    this.totalMoney = data.totalMoney || null;
    this.color = data.color || null;
  }
}