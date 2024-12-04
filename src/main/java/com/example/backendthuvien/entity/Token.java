package com.example.backendthuvien.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "token" ,nullable = false,length = 255)
    private String token;

    @Column(name = "token_type" ,nullable = false,length = 50)
    private String tokenType;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    private String revoked;
    private String expired;

    @ManyToOne @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
}
