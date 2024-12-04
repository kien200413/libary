import {
    IsString,
    IsNotEmpty,
    IsPhoneNumber,
    IsOptional,
    IsNumber
} from 'class-validator';

export class LoginDTO {

    @IsPhoneNumber() // Kiểm tra số điện thoại
    phone_number: string; // Đổi từ String thành string (kiểu dữ liệu primitive)

    @IsString()
    @IsNotEmpty() // Kiểm tra không rỗng
    password: string; // Đổi từ String thành string (kiểu dữ liệu primitive)

    @IsOptional() // Đánh dấu là thuộc tính tùy chọn
    @IsNumber() // Kiểm tra kiểu số cho role_id
    role_id?: number; // Thêm dấu ? để chỉ định đây là thuộc tính tùy chọn

    constructor(data: any) {
        this.phone_number = data.phone_number;
        this.password = data.password;
        this.role_id = data.role_id; // Khởi tạo role_id từ dữ liệu đầu vào nếu có
    }
}
