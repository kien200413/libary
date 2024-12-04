package com.example.backendthuvien.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name" ,nullable = false,length = 100)
    private String name;

    public static String ADMIN="ADMIN";
    public static String USER="USER";
}
