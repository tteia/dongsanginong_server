package org.samtuap.inong.domain.discount.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.samtuap.inong.domain.common.BaseEntity;
import org.samtuap.inong.domain.discount.dto.DiscountUpdateRequest;
import org.samtuap.inong.domain.product.entity.PackageProduct;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE discount SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at is NULL")
public class Discount extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer discount;

    private LocalDate startAt;

    private LocalDate endAt;

    private boolean discountActive; // 할인 활성화 상태

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private PackageProduct packageProduct;

    public void updateDiscount(Integer discount, LocalDate startAt, LocalDate endAt, boolean discountActive) {
        this.discount = discount;
        this.startAt = startAt;
        this.endAt = endAt;
        this.discountActive = discountActive;
    }

    public void updateDiscountActive(boolean discountActive) {
        this.discountActive = discountActive;
    }
}
