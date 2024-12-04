package com.example.backendthuvien.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT=7;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "image_url",length = 300)
    private String 	imageUrl;

    @ManyToOne @JoinColumn(name = "product_id",referencedColumnName = "id")
    @JsonIgnore
    private Product  product;
}
