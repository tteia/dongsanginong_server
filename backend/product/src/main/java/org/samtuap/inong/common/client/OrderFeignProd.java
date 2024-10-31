package org.samtuap.inong.common.client;

import org.samtuap.inong.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@FeignClient(name = "order-service", url="http://order-service", configuration = FeignConfig.class)
public interface OrderFeignProd extends OrderFeign {
}
