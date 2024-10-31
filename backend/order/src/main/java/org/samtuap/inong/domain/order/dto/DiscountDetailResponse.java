package org.samtuap.inong.domain.order.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiscountDetailResponse(
        Long id,
        Integer discount, // 할인 금액
        LocalDate startAt,
        LocalDate endAt,
        boolean discountActive
) {
}
