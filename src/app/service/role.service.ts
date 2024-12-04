import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Role } from '../responses/user/role.responses';


@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private apiGetRoles = 'http://localhost:8080/api/v1/roles'; // Đường dẫn API để lấy danh sách roles

  constructor(private http: HttpClient) { }

  getRoles(): Observable<any> {  // Sử dụng kiểu dữ liệu rõ ràng cho các vai trò
    return this.http.get<any[]>(this.apiGetRoles);  // Gọi API và trả về observable
  }
}
