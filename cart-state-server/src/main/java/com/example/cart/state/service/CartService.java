package com.example.cart.state.service;

import com.example.cart.common.dto.AddCartItemRequest;
import com.example.cart.common.dto.CartItemDto;
import com.example.cart.common.dto.CartResponse;
import com.example.cart.common.dto.CartState;
import com.example.cart.common.dto.CartStateItem;
import com.example.cart.common.entity.Cart;
import com.example.cart.common.entity.CartItem;
import com.example.cart.common.entity.Inventory;
import com.example.cart.common.entity.Sku;
import com.example.cart.common.entity.SkuImage;
import com.example.cart.common.repository.CartItemRepository;
import com.example.cart.common.repository.CartRepository;
import com.example.cart.common.repository.InventoryRepository;
import com.example.cart.common.repository.SkuImageRepository;
import com.example.cart.common.repository.SkuRepository;
import com.github.benmanes.caffeine.cache.Cache;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final SkuRepository skuRepository;
    private final SkuImageRepository skuImageRepository;
    private final InventoryRepository inventoryRepository;
    private final Cache<Long, CartState> cartStateCache;

    public CartService(
        CartRepository cartRepository,
        CartItemRepository cartItemRepository,
        SkuRepository skuRepository,
        SkuImageRepository skuImageRepository,
        InventoryRepository inventoryRepository,
        Cache<Long, CartState> cartStateCache
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.skuRepository = skuRepository;
        this.skuImageRepository = skuImageRepository;
        this.inventoryRepository = inventoryRepository;
        this.cartStateCache = cartStateCache;
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(Long memberId) {
        CartState cartState = cartStateCache.get(memberId, this::loadCartStateFromDatabase);
        List<Long> skuIds = cartState.items().stream().map(CartStateItem::skuId).toList();
        Map<Long, Sku> skuMap = skuRepository.findBySkuIdIn(skuIds).stream()
            .collect(Collectors.toMap(Sku::getSkuId, Function.identity()));
        Map<Long, String> imageMap = skuImageRepository.findBySku_SkuIdIn(skuIds).stream()
            .collect(Collectors.toMap(skuImage -> skuImage.getSku().getSkuId(), SkuImage::getImage, (left, right) -> left));
        Map<Long, Integer> stockMap = inventoryRepository.findBySku_SkuIdIn(skuIds).stream()
            .collect(Collectors.toMap(inventory -> inventory.getSku().getSkuId(), Inventory::getAvailableQuantity));

        int totalAmount = 0;
        int totalItemCount = 0;
        List<CartItemDto> items = new java.util.ArrayList<>();
        for (CartStateItem stateItem : cartState.items()) {
            Sku sku = skuMap.get(stateItem.skuId());
            if (sku == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "SKU 정보를 찾을 수 없습니다: " + stateItem.skuId());
            }
            int finalPrice = sku.getPrice();
            totalItemCount += stateItem.quantity();
            totalAmount += finalPrice * stateItem.quantity();

            items.add(new CartItemDto(
                stateItem.cartItemId(),
                stateItem.skuId(),
                sku.getSkuName(),
                stateItem.quantity(),
                sku.getPrice(),
                0,
                finalPrice,
                imageMap.getOrDefault(stateItem.skuId(), ""),
                sku.getStore().getStoreId(),
                sku.getStore().getName(),
                stockMap.getOrDefault(stateItem.skuId(), 0)
            ));
        }

        return new CartResponse(
            cartState.cartId(),
            cartState.memberId(),
            items,
            totalItemCount,
            totalAmount
        );
    }

    public void addCartItem(Long memberId, AddCartItemRequest request) {
        validatePositiveQuantity(request.quantity());
        Cart cart = findCartByMemberId(memberId);
        Sku sku = skuRepository.findById(request.skuId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SKU를 찾을 수 없습니다."));

        CartItem cartItem = cartItemRepository.findByCart_CartIdAndSku_SkuId(cart.getCartId(), sku.getSkuId())
            .orElse(new CartItem(cart, sku, 0));
        cartItem.updateQuantity(cartItem.getQuantity() + request.quantity());
        cartItemRepository.save(cartItem);
        cartStateCache.invalidate(memberId);
    }

    public void updateCartItemQuantity(Long memberId, Long cartItemId, int quantity) {
        validatePositiveQuantity(quantity);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니 아이템을 찾을 수 없습니다."));
        if (!cartItem.getCart().getMember().getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "다른 회원의 장바구니 아이템에는 접근할 수 없습니다.");
        }
        cartItem.updateQuantity(quantity);
        cartStateCache.invalidate(memberId);
    }

    public void deleteCartItem(Long memberId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "장바구니 아이템을 찾을 수 없습니다."));
        if (!cartItem.getCart().getMember().getMemberId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "다른 회원의 장바구니 아이템에는 접근할 수 없습니다.");
        }
        cartItemRepository.delete(cartItem);
        cartStateCache.invalidate(memberId);
    }

    public void deleteAllCartItems(Long memberId) {
        Cart cart = findCartByMemberId(memberId);
        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(cart.getCartId());
        cartItemRepository.deleteAll(cartItems);
        cartStateCache.invalidate(memberId);
    }

    private CartState loadCartStateFromDatabase(Long memberId) {
        Cart cart = findCartByMemberId(memberId);
        List<CartStateItem> stateItems = cartItemRepository.findByCart_CartId(cart.getCartId()).stream()
            .map(cartItem -> new CartStateItem(
                cartItem.getCartItemId(),
                cartItem.getSku().getSkuId(),
                cartItem.getQuantity()
            ))
            .toList();
        return new CartState(cart.getCartId(), memberId, stateItems);
    }

    private Cart findCartByMemberId(Long memberId) {
        return cartRepository.findByMember_MemberId(memberId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 장바구니를 찾을 수 없습니다."));
    }

    private void validatePositiveQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수량은 1 이상이어야 합니다.");
        }
    }
}
