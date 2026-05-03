package com.example.cart.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sku")
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sku_id")
    private Long skuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "sku_name", nullable = false, length = 50)
    private String skuName;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "selling_status", length = 20)
    private SkuSellingStatus sellingStatus = SkuSellingStatus.SELLING;

    @Column(name = "sku_review_count", nullable = false)
    private Integer skuReviewCount = 0;

    @Column(name = "sku_info", nullable = false, length = 255)
    private String skuInfo;

    @Column(name = "sku_description", nullable = false, length = 100)
    private String skuDescription;

    @Column(name = "is_display", nullable = false)
    private boolean isDisplay = true;

    protected Sku() {
    }

    public Sku(Product product, Store store, Category category, String skuName, Integer price) {
        this.product = product;
        this.store = store;
        this.category = category;
        this.skuName = skuName;
        this.price = price;
        this.skuInfo = "{\"freshness\":\"A\"}";
        this.skuDescription = "장바구니 비교용 SKU";
    }

    public Long getSkuId() {
        return skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public Integer getPrice() {
        return price;
    }

    public Store getStore() {
        return store;
    }
}
