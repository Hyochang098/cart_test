package com.example.cart.common.dto;

import java.util.List;

public record CartState(
    Long cartId,
    Long memberId,
    List<CartStateItem> items
) {
}
