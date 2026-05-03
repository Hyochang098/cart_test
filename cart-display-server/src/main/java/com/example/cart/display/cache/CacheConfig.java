package com.example.cart.display.cache;

import com.example.cart.common.dto.CartState;
import com.example.cart.common.dto.SkuDisplayInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    @Bean
    public Cache<Long, CartState> cartStateCache() {
        return Caffeine.newBuilder()
            .maximumSize(200)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();
    }

    @Bean
    public Cache<Long, SkuDisplayInfo> skuDisplayCache() {
        return Caffeine.newBuilder()
            .maximumSize(1_000)
            .expireAfterWrite(Duration.ofMinutes(30))
            .build();
    }
}
