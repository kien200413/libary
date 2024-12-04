package com.example.backendthuvien.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Order_DetaiDTO {
        @JsonProperty("order_id")
    @Min(value = 1,message = "OrderId > 0 nha")
    private Long orderId;

@JsonProperty("product_id")
@Min(value = 1,message = "ProductId > 0 nha")
    private Long productId;

    @Min(value = 1,message = "Price > 0 nha")
private Long price;

    @Min(value = 1,message = "numberOfProducts > 0 nha")
    @JsonProperty("number_of_products")

    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 1,message = "totalMoney > 0 nha")
    private float totalMoney;

    private String color;

}
