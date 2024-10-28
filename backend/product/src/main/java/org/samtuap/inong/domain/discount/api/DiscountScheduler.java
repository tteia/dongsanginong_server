package org.samtuap.inong.domain.discount.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.samtuap.inong.domain.discount.dto.DiscountUtil;
import org.samtuap.inong.domain.discount.entity.Discount;
import org.samtuap.inong.domain.discount.repository.DiscountRepository;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscountScheduler {

    private final DiscountRepository discountRepository;
    private final CacheManager cacheManager;

    // 자정마다 실행되도록 스케줄러 설정 (매일 0시 0분 0초에 실행)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
//    @Scheduled(cron = "0 */1 * * * ?") // 1분마다
    @Transactional
    public void applyDiscounts() {
        LocalDate now = LocalDate.now();

        // 현재 날짜와 startAt이 "같은" 할인 목록을 가져와 활성화 처리
        List<Discount> discountsToActivate = discountRepository.findAllByStartAt(now);
        log.info(">>>현재 날짜 : {}", now);
        log.info(">>>>>반환값 : {} ", discountsToActivate);
        for (Discount discount : discountsToActivate) {
            log.info(">>>>discount: {}", discount);

            log.info(">>>>>active 변경전1: {}", discount.isDiscountActive());
            DiscountUtil.activateDiscount(discount);  // 할인 활성화 (discountActive 필드를 true로 변경)
            log.info(">>>>>active 변경후1: {}", discount.isDiscountActive());
            discountRepository.save(discount);  // 변경된 할인 정보 저장
        }

        // 종료일(endAt)이 현재 날짜를 넘은 할인 목록을 가져와 비활성화 처리
        List<Discount> discountsToDeactivate = discountRepository.findAllByEndAtBefore(now);
        for (Discount discount : discountsToDeactivate) {
            DiscountUtil.deactivateDiscount(discount);  // 할인 비활성화 (discountActive 필드를 false로 변경)
            discountRepository.save(discount);  // 변경된 할인 정보 저장
        }

        Objects.requireNonNull(cacheManager.getCache("PackageDetail")).clear();
    }
}
