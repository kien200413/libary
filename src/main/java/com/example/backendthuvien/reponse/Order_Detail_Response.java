package com.example.backendthuvien.reponse;


import com.example.backendthuvien.entity.Order_Detail;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
public class Order_Detail_Response {
    private Long id;
    private Long orderId;
    private Long productId;
    private String color;
    private int numberOfProducts;
    private float price;
    private float totalMoney;

    // Thông tin bổ sung từ Order
    private Long userId;
    private String orderFullname;
    private String orderEmail;
    private String orderPhoneNumber;
    private String orderAddress;
    private String orderNote;
    private Date orderDate;
    private String status;
    private Float orderTotalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private LocalDate shippingDate;
    private String trackingNumber;
    private String paymentMethod;
    private Boolean orderActive;

    // Thông tin bổ sung từ Product
    private String productName;
    private String productThumbnail;

    public static Order_Detail_Response fromOrderDetail(Order_Detail orderDetail) {
        return Order_Detail_Response.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .color(orderDetail.getColor())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .price(orderDetail.getPrice())
                .totalMoney(orderDetail.getTotalMoney())

                // Các thông tin từ Order
                .userId(orderDetail.getOrder().getUser().getId())
                .orderFullname(orderDetail.getOrder().getFullName())
                .orderEmail(orderDetail.getOrder().getEmail())
                .orderPhoneNumber(orderDetail.getOrder().getPhoneNumber())
                .orderAddress(orderDetail.getOrder().getAddress())
                .orderNote(orderDetail.getOrder().getNote())
                .orderDate(orderDetail.getOrder().getOrderDate())
                .status(orderDetail.getOrder().getStatus())
                .orderTotalMoney(orderDetail.getOrder().getTotalMoney())
                .shippingMethod(orderDetail.getOrder().getShippingMethod())
                .shippingAddress(orderDetail.getOrder().getShippingAddress())
                .shippingDate(orderDetail.getOrder().getShippingDate())
                .trackingNumber(orderDetail.getOrder().getTrackingNumber())
                .paymentMethod(orderDetail.getOrder().getPaymentMethod())
                .orderActive(orderDetail.getOrder().getActive())

                // Các thông tin từ Product
                .productName(orderDetail.getProduct().getName())
                .productThumbnail(orderDetail.getProduct().getThumbnail())

                .build();
    }
}
