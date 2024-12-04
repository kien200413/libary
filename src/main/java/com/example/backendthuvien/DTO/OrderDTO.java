package com.example.backendthuvien.DTO;

import com.example.backendthuvien.entity.CartItems;
import com.example.backendthuvien.entity.Coupon;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderDTO {

    @JsonProperty("fullname")
    private String fullName;

    private String email;

    @JsonProperty("phone_number")
    @NotBlank(message = "khong de trong")
    @Size(min = 5,message = "SDT khong hop le")
    private String phoneNumber;

    private String address;

    private String note;

    @JsonProperty("total_money")
    @Min(value = 0,message = "Gia tien lon hon or bang 0")
    private float totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;


    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @Min(value = 1,message = "Id > 0 nha")
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("active")
    private Boolean active;//thuộc về admin

@JsonProperty("cart_items")
    private List<CartItemsDTO> cartItems;

    @JsonProperty("coupon_code")
    private String couponCode;







}
