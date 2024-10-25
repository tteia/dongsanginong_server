package org.samtuap.inong.domain.product.dto;

import lombok.Builder;
import org.samtuap.inong.domain.discount.entity.Discount;
import org.samtuap.inong.domain.product.entity.PackageProduct;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record PackageProductDiscountResponse(
        Long productId,
        String productName,
        Long productPrice,
        Long discountId,
        Integer discountPrice,
        boolean discountActive,
        LocalDate startAt,
        LocalDate endAt,
        LocalDateTime createdAt // discount의 createdAt
) {
    public static PackageProductDiscountResponse from(PackageProduct product, Discount discount) {
        return PackageProductDiscountResponse.builder()
                .productId(product.getId())
                .productName(product.getPackageName())
                .productPrice(product.getPrice())
                .discountId(product.getDiscountId())
                .discountPrice(discount.getDiscount())
                .discountActive(discount.isDiscountActive())
                .startAt(discount.getStartAt())
                .endAt(discount.getEndAt())
                .createdAt(discount.getCreatedAt())
                .build();
    }
}
