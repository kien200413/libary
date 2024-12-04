package com.example.backendthuvien.Repositories;

import com.example.backendthuvien.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepo extends JpaRepository<Coupon,Long> {
    Optional<Coupon> findByCouponCodeAndActive(String couponCode, Boolean active);

}
