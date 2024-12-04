package com.example.backendthuvien.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Product extends baseEnity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" ,nullable = false,length = 100)
    private String name;

    private Float price;

    @Column(name = "thumbnail",length = 300)
    private String thumbnail;

    @Column(name = "description")
    private String description;

    @Column(name = "author")
    private String 	author;


    @Column(name = "catalogue")
    private String catalogue;
    @ManyToOne @JoinColumn(name = "category_id",referencedColumnName = "id")
    private Categories  categories;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> productImages;

}
