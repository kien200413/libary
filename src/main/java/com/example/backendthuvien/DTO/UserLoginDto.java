package com.example.backendthuvien.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @JsonProperty("phone_number")
    @NotBlank(message = "SDDT khong de trong")
    private String phoneNumber;

    @NotBlank(message = "password khonf de trong")
    private String password;

    @JsonProperty("role_id")

    private Long roleId;

}
