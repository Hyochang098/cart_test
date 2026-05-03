package com.example.cart.display.controller;

import com.example.cart.common.dto.AddCartItemRequest;
import com.example.cart.common.dto.CartResponse;
import com.example.cart.common.dto.UpdateCartItemRequest;
import com.example.cart.display.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/me")
    public ResponseEntity<CartResponse> getMyCart(
        @RequestHeader(name = "X-Local-Member-Id", required = false) Long headerMemberId
    ) {
        Long memberId = (headerMemberId == null) ? 1L : headerMemberId;
        return ResponseEntity.ok(cartService.getCart(memberId));
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addCartItem(
        @RequestHeader(name = "X-Local-Member-Id", required = false) Long headerMemberId,
        @Valid @RequestBody AddCartItemRequest request
    ) {
        Long memberId = (headerMemberId == null) ? 1L : headerMemberId;
        cartService.addCartItem(memberId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<Void> updateCartItem(
        @RequestHeader(name = "X-Local-Member-Id", required = false) Long headerMemberId,
        @PathVariable Long cartItemId,
        @Valid @RequestBody UpdateCartItemRequest request
    ) {
        Long memberId = (headerMemberId == null) ? 1L : headerMemberId;
        cartService.updateCartItemQuantity(memberId, cartItemId, request.quantity());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
        @RequestHeader(name = "X-Local-Member-Id", required = false) Long headerMemberId,
        @PathVariable Long cartItemId
    ) {
        Long memberId = (headerMemberId == null) ? 1L : headerMemberId;
        cartService.deleteCartItem(memberId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteAllCartItems(
        @RequestHeader(name = "X-Local-Member-Id", required = false) Long headerMemberId
    ) {
        Long memberId = (headerMemberId == null) ? 1L : headerMemberId;
        cartService.deleteAllCartItems(memberId);
        return ResponseEntity.noContent().build();
    }
}
