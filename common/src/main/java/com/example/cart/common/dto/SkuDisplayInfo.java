package com.example.cart.common.dto;

public record SkuDisplayInfo(
    Long skuId,
    String skuName,
    int price,
    String imageUrl,
    Long storeId,
    String storeName
) {
}
