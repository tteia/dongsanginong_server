package org.samtuap.inong.domain.discount.dto;

import lombok.Builder;
import org.samtuap.inong.domain.discount.entity.Discount;

import java.time.LocalDate;

@Builder
public record DiscountUpdateRequest(
        Integer discount,
        LocalDate startAt,
        LocalDate endAt,
        boolean discountActive
) {
    public void updateEntity(Discount discount) {
        discount.updateDiscount(this.discount, this.startAt, this.endAt, this.discountActive);
    }
}
