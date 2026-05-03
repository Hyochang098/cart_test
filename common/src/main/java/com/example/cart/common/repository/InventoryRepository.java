package com.example.cart.common.repository;

import com.example.cart.common.entity.Inventory;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySku_SkuIdIn(Collection<Long> skuIds);

    Optional<Inventory> findBySku_SkuId(Long skuId);
}
