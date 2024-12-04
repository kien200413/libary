import { Component, ViewChild ,OnInit} from '@angular/core';
import { LoginDTO } from '../../dtos/user/login.dto';
import { UserService } from '../../service/user.service';
import { Router } from '@angular/router';
import { NgForm, FormsModule } from '@angular/forms'; // Sử dụng NgForm để làm việc với form
import { CommonModule } from '@angular/common';
import { LoginResponse } from '../../responses/user/login.responses';
import { TokenService } from '../../service/token.service';
import { RoleService } from '../../service/role.service';
import { Role } from '../../responses/user/role.responses';
import { RouterModule } from '@angular/router';
import { UserResponse } from '../../responses/user/user.reponse';
import { HeaderComponent } from '../../header/header.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule,CommonModule,RouterModule,HeaderComponent],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  @ViewChild('loginForm') loginForm!: NgForm; // Sử dụng NgForm để kiểm tra tính hợp lệ của form
  phoneNumber: string = '';
  password: string = '';


  roles: Role[] = []; // Biến lưu trữ danh sách roles
  rememberMe: boolean = true;
  selectedRole : Role | undefined;
  userResponse?:UserResponse;
  constructor(
    private router: Router,
    private userService: UserService,
    private tokenService: TokenService,
    private roleService: RoleService
  ) {}
  
  ngOnInit() {
    // Gọi API lấy danh sách roles và lưu vào biến roles
    debugger;  // Sử dụng debugger để kiểm tra trước khi API được gọi
    this.roleService.getRoles().subscribe({
      next: (roles: Role[]) => {

        
          // Xử lý khi nhận dữ liệu
          this.roles=roles;
          this.selectedRole=roles.length >0? roles[0] :undefined;

      },
      error: (err: any) => {
        console.error('Lỗi từ phía server:', err);
       
      }
    });
  }
  
  
  login() {
    // Kiểm tra tính hợp lệ của form trước khi xử lý
   
     debugger;
      const loginDTO: LoginDTO = {
        phone_number: this.phoneNumber,
        password: this.password,
        role_id:this.selectedRole?.id??1
      }; 

      this.userService.login(loginDTO).subscribe({
        next: (response: LoginResponse) => {
          console.log("User detail response:", response);
         debugger;
          const { token } = response;  // Sử dụng interface LoginResponse để đảm bảo kiểu
        if(this.rememberMe){
          
          this.tokenService.setToken(token);
          debugger
          this.userService.getUserDetail(token).subscribe({
            next: (reponse: any)=>{
              debugger;
              this.userResponse={
                id:reponse.id,
                fullname:reponse.fullName,
                address: reponse.address, 
                phone_number:reponse.phoneNumber,
                active: reponse.active,
                date_of_birth: new Date(reponse.dateOfBirth),
                role:reponse.role,
                
              };
              console.log('Thông tin người dùng:', this.userResponse);
              this.userService.saveUserResponseToLocalStorage(this.userResponse);
             if(this.userResponse?.role.name=='ADMIN'){
              this.router.navigate(['/admin']);
             }else if(this.userResponse?.role.name=='USER'){
              this.router.navigate(['/']);
             }
            },
            complete: ()=>{
debugger;
            },
            error: (err: any) => {
              console.error("Error getting user details:", err);
              alert('Không lấy được thông tin chi tiết người dùng, vui lòng thử lại sau. ' + err?.message);
            }
            


          })
        }//muốn sử dụng token trong cac API

         
        },
        complete: ()=>{

        },
        error: (err: any) => {
          alert('Đăng nhập không thành công, vui lòng thử lại sau. ' + err?.err?.message); // Nối chuỗi thông báo và message từ lỗi
        }
         
      });
  
  }
}
