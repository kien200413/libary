package com.example.backendthuvien.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UpdateCategoryReponses {
    @JsonProperty("message")
    private String message;

    @JsonProperty("token")
    private String token;

}
