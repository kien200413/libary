import {
    IsString,
    IsNotEmpty,
    IsPhoneNumber,
    IsDate,
    IsOptional
} from 'class-validator';
export class RegisterDTO {
    @IsString()
    "fullname": String;

    @IsPhoneNumber()
    "phone_number": String;

    @IsString()
    "address": String;

    @IsString()
    @IsNotEmpty()
    "password": String;


    @IsString()
    @IsNotEmpty()
    "retype_password": String;

    @IsDate()
    "date_of_birth": Date; // Định dạng ngày sinh
    "facebook_acount_id": number=0;
    "google_acount_id": number=0;
   

    @IsString()
  @IsOptional() // Token reCAPTCHA là tùy chọn trong một số trường hợp
  recaptchaToken: string | null = null;
    constructor(data: any) {
        this.fullname = data.fullname;
        this.phone_number = data.phone_number;
        this.address = data.address;
        this.password = data.password;
        this.retype_password = data.password;
        this.date_of_birth = data.date_of_birth;
        this.facebook_acount_id = data.facebook_acount_id || 0;
        this.google_acount_id = data.google_acount_id || 0;
       
        this.recaptchaToken = data.recaptchaToken || null;



    }
}