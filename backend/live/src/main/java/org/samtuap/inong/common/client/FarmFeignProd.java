package org.samtuap.inong.common.client;

import org.samtuap.inong.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@FeignClient(name = "product-service", url="http://product-service", configuration = FeignConfig.class)
public interface FarmFeignProd extends FarmFeign {
}
