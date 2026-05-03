package com.example.cart.common.dto;

public record CartItemDto(
    Long cartItemId,
    Long skuId,
    String skuName,
    int quantity,
    int price,
    int discountAmount,
    int finalPrice,
    String imageUrl,
    Long storeId,
    String storeName,
    int availableStock
) {
}
