import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root', // Đảm bảo rằng TokenService được cung cấp ở cấp root
})
export class TokenService {
  private readonly TOKEN_KEY = 'access_token'; // Key để lưu trữ token
  private readonly USER_KEY = 'user'; // Key để lưu trữ thông tin người dùng
  private jwtHelper = new JwtHelperService(); // Khởi tạo JwtHelperService

  constructor() {}

  // Kiểm tra sự tồn tại của localStorage trước khi sử dụng
  private isLocalStorageAvailable(): boolean {
    return typeof localStorage !== 'undefined';
  }

  // Getter cho token
  getToken(): string | null {
    if (this.isLocalStorageAvailable()) {
      const token = localStorage.getItem(this.TOKEN_KEY);
      return token ? token : null; // Trả về null nếu token rỗng
    }
    console.warn('localStorage is not available');
    return null;
  }

  // Setter cho token
  setToken(token: string): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem(this.TOKEN_KEY, token); // Lưu token vào localStorage
    } else {
      console.warn('localStorage is not available');
    }
  }

  // Xóa token và thông tin user khỏi localStorage
  removeToken(): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem(this.TOKEN_KEY); // Xóa token
      localStorage.removeItem(this.USER_KEY); // Xóa thông tin người dùng
    } else {
      console.warn('localStorage is not available');
    }
  }

  // Lưu thông tin người dùng
  setUser(user: any): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    } else {
      console.warn('localStorage is not available');
    }
  }

  // Lấy userId từ localStorage
  getUserId(): number {
    if (this.isLocalStorageAvailable()) {
      const user = localStorage.getItem(this.USER_KEY);
      if (!user) {
        console.warn('Không tìm thấy thông tin người dùng');
        return 0;
      }
      try {
        const userObject = JSON.parse(user); // Giải mã JSON thành object
        return userObject?.id ?? 0; // Trả về userId nếu tồn tại, không thì trả về 0
      } catch (error) {
        console.error('Lỗi khi giải mã thông tin người dùng:', error);
        return 0;
      }
    }
    console.warn('localStorage is not available');
    return 0;
  }

  // Kiểm tra token có hết hạn hay không
  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) {
      console.warn('Token không tồn tại hoặc rỗng');
      return true; // Nếu không có token, coi như đã hết hạn
    }
    try {
      return this.jwtHelper.isTokenExpired(token); // Kiểm tra hết hạn
    } catch (error) {
      console.error('Lỗi khi kiểm tra token hết hạn:', error);
      return true; // Trả về true nếu lỗi
    }
  }

  // Kiểm tra người dùng có đang đăng nhập không
  isLoggedIn(): boolean {
    const token = this.getToken();
    const userId = this.getUserId();
    if (token && !this.isTokenExpired() && userId > 0) {
      return true;
    }
    return false;
  }
}
