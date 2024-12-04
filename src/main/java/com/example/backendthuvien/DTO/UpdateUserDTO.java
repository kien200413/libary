package com.example.backendthuvien.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateUserDTO {

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")

    private String phoneNumber;

    private String address;

    private String password;




    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_acount_id")
    private int facebookAcountId;

    @JsonProperty("google_acount_id")
    private int googleAcountId;





}
