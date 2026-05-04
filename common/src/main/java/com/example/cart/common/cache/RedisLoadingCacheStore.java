package com.example.cart.common.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.function.Function;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisLoadingCacheStore<K, V> implements LoadingCacheStore<K, V> {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final Class<V> valueType;
    private final String cacheKeyPrefix;
    private final Duration cacheTtl;

    public RedisLoadingCacheStore(
        StringRedisTemplate stringRedisTemplate,
        ObjectMapper objectMapper,
        Class<V> valueType,
        String cacheKeyPrefix,
        Duration cacheTtl
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.valueType = valueType;
        this.cacheKeyPrefix = cacheKeyPrefix;
        this.cacheTtl = cacheTtl;
    }

    @Override
    public V get(K cacheKey, Function<K, V> valueLoader) {
        String redisKey = buildRedisKey(cacheKey);
        try {
            String cachedJson = stringRedisTemplate.opsForValue().get(redisKey);
            if (cachedJson != null) {
                V cachedValue = readValue(redisKey, cachedJson);
                if (cachedValue != null) {
                    return cachedValue;
                }
            }
        } catch (RedisConnectionFailureException ignored) {
            // Redis 장애 시에도 서비스가 중단되지 않도록 DB 로더로 즉시 폴백한다.
        }

        V loadedValue = valueLoader.apply(cacheKey);
        if (loadedValue == null) {
            return null;
        }

        try {
            String serializedValue = objectMapper.writeValueAsString(loadedValue);
            stringRedisTemplate.opsForValue().set(redisKey, serializedValue, cacheTtl);
        } catch (JsonProcessingException | RedisConnectionFailureException ignored) {
            // 직렬화/저장 실패는 캐시 미적용으로 처리하고 본 요청은 정상 응답한다.
        }
        return loadedValue;
    }

    @Override
    public void evict(K cacheKey) {
        String redisKey = buildRedisKey(cacheKey);
        try {
            stringRedisTemplate.delete(redisKey);
        } catch (RedisConnectionFailureException ignored) {
            // 캐시 삭제 실패는 비즈니스 로직 실패로 보지 않는다.
        }
    }

    private String buildRedisKey(K cacheKey) {
        return cacheKeyPrefix + cacheKey;
    }

    private V readValue(String redisKey, String cachedJson) {
        try {
            return objectMapper.readValue(cachedJson, valueType);
        } catch (JsonProcessingException parsingException) {
            // 역직렬화 실패 키는 제거해 다음 요청에서 정상 데이터로 재생성한다.
            try {
                stringRedisTemplate.delete(redisKey);
            } catch (RedisConnectionFailureException ignored) {
            }
            return null;
        }
    }
}
