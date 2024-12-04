package com.example.backendthuvien.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor


public class OrderDetailDTO {
    private Long orderDetailId;
    private Long orderId;
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
    private Long productId;
    private String productName;
    private Float productPrice;
    private Float orderDetailPrice;
    private Integer numberOfProducts;
    private Float orderDetailTotalMoney;
    private String color;
    private String productThumbnail;

    public OrderDetailDTO(long orderDetailId, long orderId, long userId, String orderFullname, String orderEmail,
                          String orderPhoneNumber, String orderAddress, String orderNote, Date orderDate,
                          String status, float orderTotalMoney, String shippingMethod, String shippingAddress,
                          LocalDate shippingDate, String trackingNumber, String paymentMethod, Boolean orderActive,
                          long productId, String productName, float productPrice, float orderDetailPrice,
                          int numberOfProducts, float orderDetailTotalMoney, String color, String productThumbnail) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.userId = userId;
        this.orderFullname = orderFullname;
        this.orderEmail = orderEmail;
        this.orderPhoneNumber = orderPhoneNumber;
        this.orderAddress = orderAddress;
        this.orderNote = orderNote;
        this.orderDate = orderDate;
        this.status = status;
        this.orderTotalMoney = orderTotalMoney;
        this.shippingMethod = shippingMethod;
        this.shippingAddress = shippingAddress;
        this.shippingDate = shippingDate;
        this.trackingNumber = trackingNumber;
        this.paymentMethod = paymentMethod;
        this.orderActive = orderActive;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.orderDetailPrice = orderDetailPrice;
        this.numberOfProducts = numberOfProducts;
        this.orderDetailTotalMoney = orderDetailTotalMoney;
        this.color = color;
        this.productThumbnail = productThumbnail;
    }
}


