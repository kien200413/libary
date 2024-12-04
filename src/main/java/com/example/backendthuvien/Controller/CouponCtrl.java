package com.example.backendthuvien.Controller;

import com.example.backendthuvien.Services.CouponService;
import com.example.backendthuvien.entity.Categories;
import com.example.backendthuvien.entity.Coupon;
import com.example.backendthuvien.reponse.CouponResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/coupons")
@RequiredArgsConstructor
public class CouponCtrl {
    @Autowired
    private CouponService couponService;

    @GetMapping("")
    public ResponseEntity<?> index() {
        try{
            List<Coupon> coupons = couponService.getAllCoupons();
            return ResponseEntity.ok(coupons);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // API để lấy mã giảm giá theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Coupon> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

    // API để tạo mã giảm giá mới
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody Coupon coupon) {
        Coupon newCoupon = couponService.createCoupon(coupon);
        CouponResponse couponResponse = CouponResponse.fromCoupon(newCoupon);
        return ResponseEntity.ok(couponResponse);
    }
    // API để cập nhật mã giảm giá
    @PutMapping("/{id}")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon coupon) {
        Coupon updatedCoupon = couponService.updateCoupon(id, coupon);
        return ResponseEntity.ok(updatedCoupon);
    }

    // API để xóa mã giảm giá theo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Coupon with ID " + id + " has been deleted.");
    }


    @PostMapping("/validate")
    public ResponseEntity<Coupon> validateCoupon(@RequestBody Map<String, Object> payload) {
        String couponCode = (String) payload.get("couponCode");
        float orderTotal = Float.parseFloat(payload.get("orderTotal").toString());

        // Gọi CouponService
        Coupon coupon = couponService.validateCoupon(couponCode, orderTotal);
        return ResponseEntity.ok(coupon);
    }

}
