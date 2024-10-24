package org.samtuap.inong.domain.chat.dto;

import lombok.Builder;

@Builder
public record CouponDetailResponse(
        Long couponId,
        String couponName,
        Integer discountPercentage,
        Integer quantity
) {
}