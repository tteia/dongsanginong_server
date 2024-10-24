package org.samtuap.inong.domain.discount.dto;

import lombok.Builder;
import org.samtuap.inong.domain.discount.entity.Discount;

import java.time.LocalDate;
import java.util.List;

@Builder
public record DiscountCreateRequest(
         Integer discount,
         LocalDate startAt,
         LocalDate endAt,
         List<Long> productIdList
) {
    public Discount toEntity() {
        return Discount.builder()
                .discount(this.discount)
                .startAt(this.startAt)
                .endAt(this.endAt)
                .discountActive(this.startAt.equals(LocalDate.now())) // 시작일이 오늘이면 활성화
                .build();
    }
}
