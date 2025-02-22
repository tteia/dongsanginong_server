package org.samtuap.inong.domain.coupon.repository;

import lombok.RequiredArgsConstructor;
import org.samtuap.inong.common.exception.BaseCustomException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

import static org.samtuap.inong.common.exceptionType.CouponExceptionType.COUPON_SOLD_OUT;

@Repository
@RequiredArgsConstructor
public class CouponRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private String getCouponQuantityKey(Long couponId) {
        return "coupon:quantity:" + couponId;
    }

    public void createCoupon(Long couponId, int quantity) {
        String key = getCouponQuantityKey(couponId);
        redisTemplate.opsForValue().set(key, String.valueOf(quantity));
    }

    public Long decreaseCouponQuantity(Long couponId) {
        String key = getCouponQuantityKey(couponId);
        String currentValue = redisTemplate.opsForValue().get(key);
        if("0".equals(currentValue)) {
            throw new BaseCustomException(COUPON_SOLD_OUT);
        }
        if ("-1".equals(currentValue)) {
            return -1L;
        }
        return redisTemplate.opsForValue().decrement(key);
    }

    public Long increaseCouponQuantity(Long couponId) {
        String key = getCouponQuantityKey(couponId);
        String currentValue = redisTemplate.opsForValue().get(key);
        if ("-1".equals(currentValue)) {
            return -1L;
        }
        return redisTemplate.opsForValue().increment(key);
    }

    public void deleteCoupon(Long couponId) {
        String key = getCouponQuantityKey(couponId);
        redisTemplate.delete(key);
    }

    public int getCouponQuantity(Long couponId) {
        String key = getCouponQuantityKey(couponId);
        String quantityStr = redisTemplate.opsForValue().get(key);
        if (quantityStr != null) {
            return Integer.parseInt(quantityStr);
        } else {
            throw new RuntimeException("쿠폰을 찾을 수 없습니다.");
        }
    }

    public void deleteAll() {
        Set<String> keys = redisTemplate.keys("coupon:quantity:*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void setCouponQuantity(Long couponId, int quantity) {
        String key = getCouponQuantityKey(couponId);
        redisTemplate.opsForValue().set(key, String.valueOf(quantity));
    }
}