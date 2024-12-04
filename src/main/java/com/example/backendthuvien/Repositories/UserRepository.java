package com.example.backendthuvien.Repositories;

import com.example.backendthuvien.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Boolean existsByPhoneNumber(String phoneNumber);


    Optional<User> findByPhoneNumber(String phoneNumber);//select*from user where phoneNumbe=?
}
