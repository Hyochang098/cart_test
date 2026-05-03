package com.example.cart.common.dto;

import java.util.List;

public record CartResponse(
    Long cartId,
    Long memberId,
    List<CartItemDto> items,
    int totalItemCount,
    int totalAmount
) {
}
