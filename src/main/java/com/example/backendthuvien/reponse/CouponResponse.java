package com.example.backendthuvien.reponse;

import com.example.backendthuvien.entity.Coupon;
import com.example.backendthuvien.entity.DiscountType;
import com.example.backendthuvien.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {
    private Long id;
    private String couponCode;
    private DiscountType discountType;  // ENUM: PERCENTAGE = %, FIXED = tiền
    private Float discountValue;//Loại giảm giá (theo phần trăm hoặc giá trị cố định).
    private Float minOrderValue;//Giá trị tối thiểu đơn hàng cần có để áp dụng mã.
    private Float maxDiscountValue;//Giới hạn giảm giá tối đa.
    private LocalDate expiryDate;// Ngày hết hạn của mã.
    private Integer usageLimit;//Số lần mã giảm giá có thể được sử dụng.
    private Integer timesUsed;//Số lần mã giảm giá đã được sử dụng.
    private Boolean active;

    public static CouponResponse fromCoupon(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .couponCode(coupon.getCouponCode())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .minOrderValue(coupon.getMinOrderValue())
                .maxDiscountValue(coupon.getMaxDiscountValue())
                .expiryDate(coupon.getExpiryDate())
                .usageLimit(coupon.getUsageLimit())
                .timesUsed(coupon.getTimesUsed())
                .active(coupon.getActive())
                .build();
    }
}
