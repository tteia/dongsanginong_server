package org.samtuap.inong.domain.coupon.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.samtuap.inong.domain.coupon.dto.CouponRequestMessage;
import org.samtuap.inong.domain.coupon.repository.CouponRedisRepository;
import org.samtuap.inong.domain.coupon.service.CouponService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponConsumer {

    private final CouponService couponService;
    private final CouponRedisRepository couponRedisRepository;

    @KafkaListener(topics = "coupon-issue-topic", groupId = "coupon-order-group", containerFactory = "kafkaListenerContainerFactory")
    public void consume(String couponMessage) {
        CouponRequestMessage couponRequestMessage = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            couponRequestMessage = objectMapper.readValue(couponMessage, CouponRequestMessage.class);
            couponService.processCouponIssue(couponRequestMessage.couponId(), couponRequestMessage.memberId());
            log.info("쿠폰 발급 처리 성공: {}", couponMessage);
        } catch (Exception e) {
            if (couponRequestMessage != null) {
                // Redis 수량 롤백
                couponRedisRepository.increaseCouponQuantity(couponRequestMessage.couponId());
                // 재처리 로직 또는 DLQ로 메시지 전송 가능
            }
        }
    }
}