package com.example.backendthuvien.reponse;

import com.example.backendthuvien.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class RegisterRespone{
        @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;

    private List<String> details;


}
