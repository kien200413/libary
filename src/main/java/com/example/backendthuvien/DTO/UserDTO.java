package com.example.backendthuvien.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserDTO {
    private Long id;
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "SDDT khong de trong")
    private String phoneNumber;

    private String address;
    @NotBlank(message = "password khonf de trong")
    private String password;


    @JsonProperty("retype_password")
    private String retypePassword;//mat khau nhap lai

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;


//    @JsonProperty("facebook_acount_id")
//    private int facebookAcountId;
//
//    @JsonProperty("google_acount_id")
//    private int googleAcountId;





}
