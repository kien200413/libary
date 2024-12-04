import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../responses/user/category'; // Đường dẫn tới file Category model
import { CategoryResponse } from '../responses/user/category.response';
import { TokenService } from './token.service'; // Service quản lý token

@Injectable({
  providedIn: 'root', // Dịch vụ này có sẵn trên toàn ứng dụng
})
export class CategoryService {
  private apiGetCategories = 'http://localhost:8080/api/v1/categories'; // URL API để quản lý categories

  constructor(private http: HttpClient, private tokenService: TokenService) {}

  // Lấy danh sách categories với phân trang
  getCategories(page: number, limit: number): Observable<Category[]> {
    const params = new HttpParams()
      .set('page', page.toString()) // Tham số trang
      .set('limit', limit.toString()); // Số lượng categories mỗi trang

    return this.http.get<Category[]>(this.apiGetCategories, { params });
  }

  // Lấy danh sách categories với phản hồi chi tiết
  getCategoriesWithResponse(page: number, limit: number): Observable<CategoryResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('limit', limit.toString());

    return this.http.get<CategoryResponse>(this.apiGetCategories, { params });
  }

  // Thêm danh mục mới
  addCategory(category: any): Observable<any> {
    const headers = this.createAuthHeaders(); // Tạo headers có chứa token
    return this.http.post(this.apiGetCategories, category, { headers });
  }

  // Cập nhật danh mục
  updateCategory(id: number, category: { name: string; description?: string }): Observable<any> {
    const headers = this.createAuthHeaders(); // Tạo headers có chứa token
    return this.http.put(`${this.apiGetCategories}/${id}`, category, { headers });
  }

  // Xóa danh mục
  deleteCategory(id: number): Observable<any> {
    const headers = this.createAuthHeaders(); // Tạo headers có chứa token
    return this.http.delete(`${this.apiGetCategories}/${id}`, { headers });
  }

  // Hàm tạo headers với token
  private createAuthHeaders(): HttpHeaders | undefined {
    const token = this.tokenService.getToken();
    return token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : undefined;
  }
  searchCategoriesByName(name: string): Observable<any> {
    const sanitizedInput = name.trim().replace(/^,+|,+$/g, ''); // Loại bỏ dấu phẩy và khoảng trắng
    return this.http.get(`${this.apiGetCategories}/search`, { params: { name: sanitizedInput } });
  }
  
}
