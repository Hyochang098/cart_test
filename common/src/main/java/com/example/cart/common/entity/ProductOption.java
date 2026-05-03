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
@Table(name = "product_option")
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(length = 20)
    private String name;

    @Column(name = "is_display", nullable = false)
    private boolean isDisplay = true;

    protected ProductOption() {
    }

    public ProductOption(Category category, String name) {
        this.category = category;
        this.name = name;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Category getCategory() {
        return category;
    }
}
