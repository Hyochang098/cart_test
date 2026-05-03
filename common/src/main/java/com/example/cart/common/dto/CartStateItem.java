package com.example.cart.common.dto;

public record CartStateItem(
    Long cartItemId,
    Long skuId,
    int quantity
) {
}
