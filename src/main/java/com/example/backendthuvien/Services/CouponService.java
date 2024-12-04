package com.example.backendthuvien.Services;

import com.example.backendthuvien.DTO.OrderDTO;
import com.example.backendthuvien.Repositories.CouponRepo;
import com.example.backendthuvien.entity.Coupon;
import com.example.backendthuvien.entity.DiscountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    @Autowired
    private CouponRepo couponRepository;

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
    }

    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Long id, Coupon coupon) {
        Coupon existingCoupon = getCouponById(id);
        existingCoupon.setCouponCode(coupon.getCouponCode());
        existingCoupon.setDiscountType(coupon.getDiscountType());
        existingCoupon.setDiscountValue(coupon.getDiscountValue());
        existingCoupon.setMinOrderValue(coupon.getMinOrderValue());
        existingCoupon.setMaxDiscountValue(coupon.getMaxDiscountValue());
        existingCoupon.setExpiryDate(coupon.getExpiryDate());
        existingCoupon.setUsageLimit(coupon.getUsageLimit());
        existingCoupon.setActive(coupon.getActive());
        return couponRepository.save(existingCoupon);
    }

    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    public Coupon validateCoupon(String couponCode, float orderTotal) {
        // Tìm mã giảm giá theo mã code và trạng thái active
        Optional<Coupon> optionalCoupon = couponRepository.findByCouponCodeAndActive(couponCode, true);

        if (optionalCoupon.isEmpty()) {
            throw new RuntimeException("Mã giảm giá không tồn tại hoặc không hoạt động.");
        }

        Coupon coupon = optionalCoupon.get();

        // Kiểm tra ngày hết hạn
        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Mã giảm giá đã hết hạn.");
        }

        // Kiểm tra giá trị đơn hàng tối thiểu
        if (orderTotal < coupon.getMinOrderValue()) {
            System.out.println("Giá trị đơn hàng: " + orderTotal);
            System.out.println("Giá trị tối thiểu để áp dụng mã giảm giá: " + coupon.getMinOrderValue());
            throw new RuntimeException("Giá trị đơn hàng không đủ để áp dụng mã giảm giá.");
        }


        return coupon;
    }

    // Tính toán giá trị giảm giá (mới thêm vào)
    public float calculateDiscount(OrderDTO orderDTO, Coupon coupon) {
        float discountAmount = 0;

        // Kiểm tra kiểu giảm giá
        if (coupon.getDiscountType().equals(DiscountType.percentage)) {
            discountAmount = orderDTO.getTotalMoney() * coupon.getDiscountValue() / 100;
            if (coupon.getMaxDiscountValue() != null && discountAmount > coupon.getMaxDiscountValue()) {
                discountAmount = coupon.getMaxDiscountValue();
            }
        } else if (coupon.getDiscountType().equals(DiscountType.fixed)) {
            discountAmount = coupon.getDiscountValue();
        }

        // Đảm bảo số tiền giảm không vượt quá giá trị đơn hàng
        if (discountAmount > orderDTO.getTotalMoney()) {
            discountAmount = orderDTO.getTotalMoney();
        }

        return discountAmount;
    }
}
