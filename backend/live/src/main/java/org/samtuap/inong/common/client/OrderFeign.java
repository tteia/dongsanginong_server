package org.samtuap.inong.common.client;

import org.samtuap.inong.config.FeignConfig;
import org.samtuap.inong.domain.chat.dto.CouponDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="order-service", configuration = FeignConfig.class)
public interface OrderFeign {

    @GetMapping("/coupon/{couponId}")
    CouponDetailResponse getCoupon(@PathVariable("couponId") Long couponId);
}
