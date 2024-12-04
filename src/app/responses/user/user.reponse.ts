import { Role } from "./role.responses";

export interface UserResponse{
    id:number,
    fullname:string,
    phone_number:string,
    active:boolean,
    address:string,
    date_of_birth:Date,
    role:Role

}