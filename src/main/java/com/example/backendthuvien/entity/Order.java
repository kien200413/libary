package com.example.backendthuvien.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "fullname",length = 100)
    private String fullName;

    @ManyToOne @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @Column(name = "email",length = 200)
    private String email;

    @Column(name = "phone_number",length = 12,nullable = false)
    private String phoneNumber;


    @Column(name = "address",length = 200)
    private String address;



    @Column(name = "note",length = 100)
    private String note;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "status")
    private String status;

    @Column(name = "total_money")
    private float 	totalMoney;

    @Column(name = "shipping_method")
    private String shippingMethod;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "shipping_date")
    private LocalDate shippingDate;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "active")
    private Boolean active;//thuộc về admin

@OneToMany (mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Order_Detail> orderDetails;

    @ManyToOne @JoinColumn(name = "coupon_code", referencedColumnName = "coupon_code")
    private Coupon coupon;



}
