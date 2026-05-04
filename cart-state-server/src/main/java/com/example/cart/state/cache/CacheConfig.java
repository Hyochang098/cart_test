package com.example.cart.state.cache;

import com.example.cart.common.cache.LoadingCacheStore;
import com.example.cart.common.cache.RedisLoadingCacheStore;
import com.example.cart.common.dto.CartState;
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
            "state:cart:",
            Duration.ofMinutes(10)
        );
    }
}
