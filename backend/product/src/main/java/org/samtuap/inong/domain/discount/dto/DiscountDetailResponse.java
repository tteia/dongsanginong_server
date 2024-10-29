package org.samtuap.inong.domain.discount.dto;

import lombok.Builder;
import org.samtuap.inong.domain.discount.entity.Discount;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DiscountDetailResponse(
        Long id,
        Integer discount,
        LocalDate startAt,
        LocalDate endAt,
        boolean discountActive,
        List<Long>productIdList
) {
    public static DiscountDetailResponse fromEntity(Discount discount, List<Long> productIdList) {
        return DiscountDetailResponse.builder()
                .id(discount.getId())
                .discount(discount.getDiscount())
                .startAt(discount.getStartAt())
                .endAt(discount.getEndAt())
                .discountActive(discount.isDiscountActive())
                .productIdList(productIdList)
                .build();
    }
}
