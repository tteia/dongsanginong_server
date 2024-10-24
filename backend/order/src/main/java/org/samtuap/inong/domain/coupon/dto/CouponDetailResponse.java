package org.samtuap.inong.domain.coupon.dto;

import lombok.Builder;
import org.samtuap.inong.domain.coupon.entity.Coupon;

@Builder
public record CouponDetailResponse(
        Long couponId,
        String couponName,
        Integer discountPercentage,
        Integer quantity
) {
    public static CouponDetailResponse fromEntity(Coupon coupon) {
        return new CouponDetailResponse(
                coupon.getId(),
                coupon.getCouponName(),
                coupon.getDiscountPercentage(),
                coupon.getQuantity()
        );
    }
}