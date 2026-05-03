package com.example.cart.common.repository;

import com.example.cart.common.entity.Sku;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    List<Sku> findBySkuIdIn(Collection<Long> skuIds);
}
