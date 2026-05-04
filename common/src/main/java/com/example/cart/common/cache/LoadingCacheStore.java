package com.example.cart.common.cache;

import java.util.function.Function;

public interface LoadingCacheStore<K, V> {
    V get(K cacheKey, Function<K, V> valueLoader);

    void evict(K cacheKey);
}
