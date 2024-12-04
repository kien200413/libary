package com.example.backendthuvien.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@NoArgsConstructor
@Setter
@Builder
@AllArgsConstructor
public class ProductImageDTO {
@JsonProperty("product_id")
@Min(value = 1,message = "productId >=1")
private Long productId;


@Size(min = 5,max = 200,message = "image name")
@JsonProperty("timage_url")
    private String 	imageUrl;
}
