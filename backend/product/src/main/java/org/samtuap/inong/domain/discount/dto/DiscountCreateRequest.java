package org.samtuap.inong.domain.discount.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.samtuap.inong.domain.discount.entity.Discount;
import org.samtuap.inong.domain.product.entity.PackageProduct;

import java.time.LocalDate;

@Builder
public record DiscountCreateRequest(
         Integer discount,
         LocalDate startAt,
         LocalDate endAt
) {
    public Discount toEntity(PackageProduct packageProduct) {
        return Discount.builder()
                .discount(this.discount)
                .startAt(this.startAt)
                .endAt(this.endAt)
                .packageProduct(packageProduct)
                .discountActive(this.startAt.equals(LocalDate.now())) // 시작일이 오늘이면 활성화
                .build();
    }
}
