package org.samtuap.inong.domain.discount.dto;

import lombok.extern.slf4j.Slf4j;
import org.samtuap.inong.domain.discount.entity.Discount;

@Slf4j
public class DiscountUtil {

    // 할인 활성화 메서드
    public static void activateDiscount(Discount discount) {
        log.info(">>>>>active 변경전2: {}", discount.isDiscountActive());
        discount.updateDiscountActive(true);
        log.info(">>>>>active 변경후2: {}", discount.isDiscountActive());
    }

    // 할인 비활성화 메서드
    public static void deactivateDiscount(Discount discount) {
        discount.updateDiscountActive(false);
    }
}
