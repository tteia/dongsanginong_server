package org.samtuap.inong.domain.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.samtuap.inong.domain.discount.entity.Discount;
import org.samtuap.inong.domain.product.entity.PackageProduct;

import java.time.LocalDateTime;

@Builder
public record TopPackageGetResponse(@NotNull Long id,
                                    @NotNull String packageName,
                                    @NotNull Long farmId,
                                    @NotNull String farmName,
                                    String imageUrl,
                                    @NotNull Long price,
                                    Long orderCount,
                                    @NotNull Integer deliveryCycle,
                                    Long discountId,
                                    Integer discount,
                                    boolean discountActive,
                                    LocalDateTime createdAt) {

    public static TopPackageGetResponse fromEntity(PackageProduct packageProduct, String thumbnailUrl, Long orderCount, Integer discount, boolean discountActive) {
        return TopPackageGetResponse.builder()
                .id(packageProduct.getId())
                .packageName(packageProduct.getPackageName())
                .farmId(packageProduct.getFarm().getId())
                .farmName(packageProduct.getFarm().getFarmName())
                .imageUrl(thumbnailUrl)
                .price(packageProduct.getPrice())
                .orderCount(orderCount)
                .deliveryCycle(packageProduct.getDelivery_cycle())
                .discountId(packageProduct.getDiscountId())
                .discount(discount)
                .discountActive(discountActive)
                .createdAt(packageProduct.getCreatedAt())
                .build();
    }

}
