package com.example.backendthuvien.reponse;

import com.example.backendthuvien.entity.Order;
import com.example.backendthuvien.entity.Order_Detail;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class orderRespone {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;


    @JsonProperty("note")
    private String note;

    @JsonProperty("status")
    private String status;

    @JsonProperty("order_date")
    private Date orderDate;

    @JsonProperty("total_money")
    private double totalMoney;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("payment_method")
    private String paymentMethod	;

    @JsonProperty("order_detail")
    private List<Order_Detail> orderDetailz;

    @JsonProperty("active")
    private String active;

    public static orderRespone fromOrder(Order order) {
        return orderRespone.builder()
                .id(order.getId())
                .userId(order.getUser().getId())               // Đảm bảo phương thức getUserId() có trong Order
                .fullname(order.getFullName())
                .phoneNumber(order.getPhoneNumber())
                .email(order.getEmail())
                .address(order.getAddress())
                .note(order.getNote())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())         // orderDate kiểu Date
                .totalMoney(order.getTotalMoney())
                .shippingDate(order.getShippingDate())   // shippingDate kiểu LocalDate
                .shippingMethod(order.getShippingMethod())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .orderDetailz(order.getOrderDetails())   // orderDetailz kiểu List<Order_Detail>
                .active(order.getActive() ? "Đang hoạt động" : "Đã dừng")

                .build();
    }




}
