import { Injectable } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HttpUtilService {

  constructor() { }

  // Phương thức để tạo tiêu đề HTTP mặc định
  createHeaders(): HttpHeaders {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept-Language': 'vi'
      // Có thể thêm các tiêu đề khác nếu cần
    });
    return headers;
  }
}
