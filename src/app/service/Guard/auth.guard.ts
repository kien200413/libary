import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, RouterStateSnapshot, CanActivate, Router ,CanActivateFn} from '@angular/router';

import { inject } from '@angular/core';
import { TokenService } from '../token.service';
import { UserService } from '../user.service';
import { UserResponse } from '../../responses/user/user.reponse';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
  constructor(private tokenService: TokenService, private router: Router) {}

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const isTokenExpired = this.tokenService.isTokenExpired(); // Kiểm tra token
    const isUserIdValid = this.tokenService.getUserId() > 0; // Kiểm tra ID người dùng
   
debugger
    if (!isTokenExpired && isUserIdValid) {
      return true; // Cho phép truy cập
    } else {
      // Nếu không authenticated, điều hướng về trang login
      this.router.navigate(['/login']);
      return false; // Ngăn không cho truy cập
    }
  }
}

// Sử dụng functional guard như sau:
export const AuthGuardFn: CanActivateFn = (next: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  debugger;
  return inject(AuthGuard).canActivate(next, state);
};
