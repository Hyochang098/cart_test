package com.example.cart.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(
    @NotNull Long skuId,
    @NotNull @Min(1) Integer quantity
) {
}
