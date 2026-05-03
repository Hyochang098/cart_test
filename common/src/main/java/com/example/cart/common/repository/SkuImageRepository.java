package com.example.cart.common.repository;

import com.example.cart.common.entity.SkuImage;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkuImageRepository extends JpaRepository<SkuImage, Long> {
    List<SkuImage> findBySku_SkuIdIn(Collection<Long> skuIds);
}
