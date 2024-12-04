import { Component,OnInit } from '@angular/core';
import { UserService } from '../../../service/user.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-list-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.admin.component.html',
  styleUrl: './user-list.admin.component.scss'
})
export class UserListAdminComponent implements OnInit{
  users: any[] = [];
  selectedFile: File | null = null;
  message: string = '';
  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

 
  loadUsers() {
    this.userService.getUsers().subscribe({
      next: (data: any) => {
        this.users = data;
      },
      error: (err: any) => {
        console.error('Lỗi khi tải danh sách user:', err);
      }
    });
  }

  // Xử lý khi chọn file
  onFileSelect(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.message = ''; // Xóa thông báo cũ
    }
  }

  // Import file lên backend
  importFile() {
    if (!this.selectedFile) {
      this.message = 'Hãy chọn file trước khi import!';
      return;
    }

    this.userService.importUsers(this.selectedFile).subscribe({
      next: () => {
        this.message = 'Import thành công!';
        this.loadUsers(); // Load lại danh sách user sau khi import
      },
      error: (err: any) => {
        console.error('Lỗi khi import file:', err);
        this.message = 'Import thất bại! Vui lòng thử lại.';
      }
    });
  }
  
}
