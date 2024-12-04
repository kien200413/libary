import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'; // Import HttpClient
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/user/register.dto';
import { LoginDTO } from '../dtos/user/login.dto';
import { HttpUtilService } from './http.util.service'; // Đảm bảo đường dẫn đúng
import { UserResponse } from '../responses/user/user.reponse';
import { TokenService } from './token.service';

@Injectable({
  providedIn: 'root' // Đảm bảo rằng UserService được cung cấp ở cấp root
})
export class UserService {
  private apiRegister = 'http://localhost:8080/api/v1/users/register';
  private apiLogin = 'http://localhost:8080/api/v1/users/login';
  private apiUserDetail = 'http://localhost:8080/api/v1/users/details';
  private apiPhone = 'http://localhost:8080/api/v1/users';

  constructor(private http: HttpClient, private httpUtilService: HttpUtilService, private tokenService: TokenService) { }

  importUsers(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);

    const token = this.tokenService.getToken(); // Lấy token từ tokenService
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.post(`${this.apiPhone}/import-users`, formData, { headers });
  }
  getUsers(): Observable<any> {
    const token = this.tokenService.getToken(); // Lấy token từ TokenService

    // Đặt header Authorization với token
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    // Gửi request GET với headers
    return this.http.get(`${this.apiPhone}`, { headers });
  }
  // Phương thức đăng ký
  register(registerDTO: RegisterDTO): Observable<any> {
    const apiConfig = {
      headers: this.httpUtilService.createHeaders() // Tạo header sử dụng HttpUtilService
    };
    return this.http.post(this.apiRegister, registerDTO, apiConfig); // Gửi yêu cầu POST
  }

  // Phương thức đăng nhập
  login(loginDTO: LoginDTO): Observable<any> {
    const apiConfig = {
      headers: this.httpUtilService.createHeaders() // Tạo header sử dụng HttpUtilService
    };
    return this.http.post(this.apiLogin, loginDTO, apiConfig); // Gửi yêu cầu POST
  }

  // Phương thức lấy thông tin người dùng
  getUserDetail(token: string): Observable<any> {
    return this.http.post(this.apiUserDetail, {}, { // Gửi yêu cầu POST với body rỗng
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` // Gửi token trong header Authorization
      })
    });
  }

  saveUserResponseToLocalStorage(userResponse?: UserResponse) {
    try {
      debugger;
      if(userResponse==null || !userResponse){
        return;
      }
      // Chuyển đổi đối tượng userResponse thành chuỗi JSON
      const userResponseJSON = JSON.stringify(userResponse);
      console.log('Lưu thông tin người dùng vào localStorage:', userResponse);
      // Lưu chuỗi JSON vào local storage với một key (ví dụ: "userResponse")
      localStorage.setItem('user', userResponseJSON);
  
      console.log('User response saved to local storage.');
    } catch (error) {
      console.error('Error saving user response to local storage:', error);
    }
  }
  
  getUserResponseFromLocalStorage() {
    try {
      // Kiểm tra nếu localStorage có sẵn
      if (typeof localStorage === 'undefined') {
        console.warn('localStorage is not available.');
        return null;
      }
  
      // Lấy chuỗi JSON từ local storage bằng key 'user'
      const userResponseJSON = localStorage.getItem('user');
  
      // Kiểm tra xem chuỗi JSON có null hoặc undefined không
      if (userResponseJSON === null || userResponseJSON === undefined) {
        return null; // Trả về null nếu không có dữ liệu
      }
  
      // Chuyển đổi chuỗi JSON thành đối tượng
      const userResponse = JSON.parse(userResponseJSON);
      console.log('User response retrieved from local storage.');
  
      return userResponse; // Trả về đối tượng userResponse
    } catch (error) {
      console.error('Error retrieving user response from local storage:', error);
      return null; // Trả về null hoặc xử lý lỗi theo cách cần thiết
    }
  }
  checkPhoneExists(phone: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiPhone}/check-phone`, { params: { phone } });
  }
  removeUserFromLocalStorage(): void {
    try {
      // Kiểm tra nếu localStorage có sẵn
      if (typeof localStorage === 'undefined') {
        console.warn('localStorage is not available.');
        return;
      }
  
      // Xóa dữ liệu người dùng khỏi local storage với key 'user'
      localStorage.removeItem('user');
      console.log('User data removed from local storage.');
  
      // Chuyển hướng về trang đăng nhập hoặc trang chủ sau khi đăng xuất
      
    } catch (error) {
      console.error('Error removing user data from local storage:', error);
      // Xử lý lỗi nếu cần
    }
  }
  
  
  
}
