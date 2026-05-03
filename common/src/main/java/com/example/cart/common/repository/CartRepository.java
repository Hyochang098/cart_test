package com.example.cart.common.repository;

import com.example.cart.common.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMember_MemberId(Long memberId);
}
