import { ProductImage } from "./product.image";

export interface Product {
    id?: number;
    name: string;
    price: number;
    thumbnail: string;
    description: string;
    category_id: number;
    keyword:string;
    author:string
    url: string;  // Thêm thuộc tính url (tùy chọn)
    product_images: any[];
  
  }
  
  