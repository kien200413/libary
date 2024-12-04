package com.example.backendthuvien.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

@ManyToOne @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product products;

    @ManyToOne @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order orders;

@Column(name = "quantity")
    private Integer quantity;
}
