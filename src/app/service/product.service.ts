import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Product } from '../responses/user/product';
import { TokenService } from '../service/token.service';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'  // Dịch vụ này có sẵn trên toàn ứng dụng
})
export class ProductService {
  [x: string]: any;
  // URL API để lấy danh sách sản phẩm

  private cartItems: any[] = [];  // Mảng chứa các sản phẩm trong giỏ hàng
  private cartItemCountSubject = new BehaviorSubject<number>(0);
  private apiGetProducts = 'http://localhost:8080/api/v1/products';
 
  private apiUploadImage = 'http://localhost:8080/api/v1/products/uploads';

  constructor(private http: HttpClient, private tokenService: TokenService) {}

  // Phương thức lấy danh sách sản phẩm với phân trang
  getProducts(categoryId: number, keyword: string, page: number, limit: number): Observable<Product[]> {
    const params = new HttpParams()
      .set('category_id', categoryId.toString())
      .set('keyword', keyword)
      .set('page', page.toString())  // Tham số trang
      .set('limit', limit.toString());  // Số sản phẩm mỗi trang

    return this.http.get<Product[]>(this.apiGetProducts, { params });
  }

  // Lấy chi tiết sản phẩm theo ID
  getDetailProduct(productId: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiGetProducts}/${productId}`);
  }
  getProductById(productId: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiGetProducts}/${productId}`);
  }

  // Lấy sản phẩm theo danh sách IDs
  getProductsByIds(productIds: number[]): Observable<Product[]> {
    const params = new HttpParams().set('ids', productIds.join(',')); // Chuyển danh sách ID thành chuỗi, ngăn cách bởi dấu phẩy
    return this.http.get<Product[]>(`${this.apiGetProducts}/by-ids`, { params });
  }

  // Thêm sản phẩm không có ảnh
  addProduct(product: Product): Observable<Product> {
    debugger
    const token = this.tokenService.getToken();
    const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : undefined;

    return this.http.post<Product>(this.apiGetProducts, product, { headers }).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error adding product:', error.message);
        return throwError(error);
      })
    );
  }

  // Phương thức upload ảnh cho sản phẩm đã có ID
  uploadProductImage(productId: number, formData: FormData): Observable<any> {
    const token = this.tokenService.getToken();
    const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : undefined;
    console.log('Token being sent:', token); // Kiểm tra token đang được gửi
  
    return this.http.post(`${this.apiUploadImage}/${productId}`, formData, { headers }).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error uploading image:', error.message);
        return throwError(error);
      })
    );
  }
  deleteProduct(productId: number): Observable<any> {
    const token = this.tokenService.getToken(); // Lấy token từ TokenService
    const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : undefined;
    const deleteUrl = `${this.apiGetProducts}/${productId}`;
  
    return this.http.delete(deleteUrl, { headers, responseType: 'text' }).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error deleting product:', error.message);
        return throwError(error);
      })
    );
  }
  
  // Phương thức cập nhật sản phẩm
  updateProduct(productId: number, product: Product): Observable<Product> {
    const token = this.tokenService.getToken();
    const headers = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : undefined;

    const updateUrl = `${this.apiGetProducts}/${productId}`;  // Đảm bảo URL API đúng
    
    console.log('Sending product update request:', product);

    return this.http.put<Product>(updateUrl, product, { headers }).pipe(
      catchError((error: HttpErrorResponse) => {
        console.error('Error updating product:', error.message);
        console.error('Server error details:', error.error);  // Xem chi tiết lỗi từ server
        return throwError(error);
      })
    );
  }

  
  
 

  
  
  
  


  
}
