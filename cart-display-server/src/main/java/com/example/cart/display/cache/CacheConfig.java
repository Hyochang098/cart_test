package com.example.cart.display.cache;

import com.example.cart.common.cache.LoadingCacheStore;
import com.example.cart.common.cache.RedisLoadingCacheStore;
import com.example.cart.common.dto.CartState;
import com.example.cart.common.dto.SkuDisplayInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class CacheConfig {
    @Bean
    public LoadingCacheStore<Long, CartState> cartStateCache(
        StringRedisTemplate stringRedisTemplate,
        ObjectMapper objectMapper
    ) {
        return new RedisLoadingCacheStore<>(
            stringRedisTemplate,
            objectMapper,
            CartState.class,
            "display:cart:",
            Duration.ofMinutes(10)
        );
    }

    @Bean
    public LoadingCacheStore<Long, SkuDisplayInfo> skuDisplayCache(
        StringRedisTemplate stringRedisTemplate,
        ObjectMapper objectMapper
    ) {
        return new RedisLoadingCacheStore<>(
            stringRedisTemplate,
            objectMapper,
            SkuDisplayInfo.class,
            "display:sku:",
            Duration.ofMinutes(30)
        );
    }
}
