package com.example.backendthuvien.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "provider" ,nullable = false,length = 20)
    private String provider;

    @Column(name = "provide_id" ,nullable = false,length = 50)
    private String 	provideId;




    @Column(name = "name" ,nullable = false,length = 150)
    private String name;


    @Column(name = "email" ,nullable = false,length = 150)
    private String email;
}
