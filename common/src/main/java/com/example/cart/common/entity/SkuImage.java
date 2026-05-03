package com.example.cart.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sku_image")
public class SkuImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sku_image_id")
    private Long skuImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id", nullable = false)
    private Sku sku;

    @Column(nullable = false, length = 255)
    private String image;

    protected SkuImage() {
    }

    public SkuImage(Sku sku, String image) {
        this.sku = sku;
        this.image = image;
    }

    public Sku getSku() {
        return sku;
    }

    public String getImage() {
        return image;
    }
}
