import { Component, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';
import { UserService } from '../../service/user.service';
import { RegisterDTO } from '../../dtos/user/register.dto';
import { RecaptchaModule } from 'ng-recaptcha';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  imports: [HttpClientModule, FormsModule, CommonModule, RecaptchaModule],
})
export class RegisterComponent {
  @ViewChild('registerForm') registerForm!: NgForm;
  phone: string = '';
  password!: string;
  retypePassword!: string;
  fullname: string = '';
  address: string = '';
  isAccepted: boolean = false;
  dateOfBirth: Date;
  passwordMismatch: boolean = false;
  phoneExists: boolean = false;
  recaptchaToken: string | null = null;

  constructor(private userService: UserService, private router: Router) {
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.fullname = '';
    this.address = '';
    this.isAccepted = false;
    this.passwordMismatch = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
  }

  onPhoneChange() {
    console.log('Checking if phone exists:', this.phone);
    if (this.phone.length >= 8) {
      this.userService.checkPhoneExists(this.phone).subscribe({
        next: (exists: boolean) => {
          this.phoneExists = exists;
          console.log('Phone exists:', exists);
        },
        error: (err: any) => {
          console.error('Error checking phone:', err);
        },
      });
    } else {
      this.phoneExists = false;
      console.log('Phone length is too short to check existence.');
    }
  }

  register() {
    console.log('Register button clicked.');
    console.log('Form data:', {
      phone: this.phone,
      password: this.password,
      fullname: this.fullname,
      address: this.address,
      dateOfBirth: this.dateOfBirth,
      recaptchaToken: this.recaptchaToken,
    });

    if (!this.isAccepted) {
      alert('Bạn phải đồng ý với các điều khoản để tiếp tục.');
      console.log('User did not accept the terms and conditions.');
      return;
    }

    if (!this.recaptchaToken) {
      alert('Vui lòng xác thực reCAPTCHA.');
      console.log('reCAPTCHA token is missing.');
      return;
    }

    if (this.registerForm.valid) {
      const registerDTO: RegisterDTO = {
        fullname: this.fullname,
        phone_number: this.phone,
        address: this.address,
        password: this.password,
        retype_password: this.retypePassword,
        date_of_birth: this.dateOfBirth,
        facebook_acount_id: 0,
        google_acount_id: 0,
        recaptchaToken: this.recaptchaToken,
      };

      console.log('Sending register request:', registerDTO);

      this.userService.register(registerDTO).subscribe({
        next: (response: any) => {
          console.log('Response from server:', response);
          if (response && response.id) {
            this.router.navigate(['/login']);
            alert('Đăng ký thành công');
          } else {
            alert('Đăng ký không thành công');
          }
        },
        complete: () => {
          console.log('Register request completed.');
        },
        error: (err: any) => {
          console.error('Error during registration:', err);
          if (err.status === 400 && err.error.message) {
            alert(err.error.message);
          } else {
            console.error(
              `Unexpected error from server. Code: ${err.status}, Response:`,
              err.error
            );
          }
        },
      });
    } else {
      alert('Form không hợp lệ');
      console.log('Form validation failed.');
    }
  }

  checkPasswordsMatch() {
    this.passwordMismatch = this.password !== this.retypePassword;
    console.log(
      'Password match check:',
      this.passwordMismatch ? 'Passwords do not match.' : 'Passwords match.'
    );
  }

  checkAge() {
    const today = new Date();
    const birthDate = new Date(this.dateOfBirth);
    let age = today.getFullYear() - birthDate.getFullYear();
    let monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }

    if (age < 18) {
      this.registerForm.form.controls['dateOfBirth'].setErrors({ invalidAge: true });
      console.log('Age check failed: User is under 18.');
    } else {
      this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      console.log('Age check passed.');
    }
  }

  onCaptchaResolved(token: string | null) {
    if (token) {
      this.recaptchaToken = token;
      console.log('reCAPTCHA token received:', token);
    } else {
      console.error('Invalid reCAPTCHA token!');
    }
  }
}
