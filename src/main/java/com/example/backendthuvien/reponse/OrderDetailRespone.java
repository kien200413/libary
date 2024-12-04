package com.example.backendthuvien.reponse;

import com.example.backendthuvien.entity.Order;
import com.example.backendthuvien.entity.Order_Detail;
import com.example.backendthuvien.entity.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class OrderDetailRespone {
    private long id;


    @JsonProperty("order_id")
    private Long orderId;


    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("price")
    private float price;

    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    @JsonProperty("color")
    private String color;

    public static OrderDetailRespone fromOrderDetail(Order_Detail order_detail){
        OrderDetailRespone orderDetailRespone=OrderDetailRespone.builder()
                .id(order_detail.getId())
                .orderId(order_detail.getOrder().getId())
                .productId(order_detail.getProduct().getId())
                .color(order_detail.getColor())
                .numberOfProducts(order_detail.getNumberOfProducts())
                .price(order_detail.getPrice())
                .totalMoney(order_detail.getTotalMoney())

                .build();
        return orderDetailRespone;
    }

}
