package org.samtuap.inong.domain.discount.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.samtuap.inong.domain.discount.entity.Discount;

import java.time.LocalDate;

@Builder
public record DiscountResponse(
         Long id,
         Integer discount,
         LocalDate startAt,
         LocalDate endAt,
         boolean discountActive,
         Long packageProductId
) {
    public static DiscountResponse fromEntity(Discount discount) {
        return DiscountResponse.builder()
                .id(discount.getId())
                .discount(discount.getDiscount())
                .startAt(discount.getStartAt())
                .endAt(discount.getEndAt())
                .discountActive(discount.isDiscountActive())
                .packageProductId(discount.getPackageProduct().getId())
                .build();
    }
}
