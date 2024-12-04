package com.example.backendthuvien.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class categoryDTO {
    @NotBlank(message = "K để trống")
    private String name;
}
