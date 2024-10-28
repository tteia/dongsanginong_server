package org.samtuap.inong.common.client;


import org.samtuap.inong.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@FeignClient(name = "live-service", url="http://live-service", configuration = FeignConfig.class)
public interface LiveFeignProd extends LiveFeign {
}
