package com.example.cart.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Province province;

    @Column(name = "owner_name", nullable = false, length = 30)
    private String ownerName;

    @Column(name = "business_number", nullable = false, length = 20)
    private String businessNumber;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 30)
    private String shortDescription;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false, length = 255)
    private String businessHour;

    @Column(nullable = false, length = 255)
    private String closedDate;

    @Column(nullable = false, length = 100)
    private String noticeMessage;

    @Column(nullable = false)
    private boolean isDisplay;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt = Instant.now();

    protected Store() {
    }

    public Store(String name, Province province, String ownerName, String businessNumber, String address, String phoneNumber) {
        this.name = name;
        this.province = province;
        this.ownerName = ownerName;
        this.businessNumber = businessNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.shortDescription = "신선 식재료";
        this.description = "지역 상점";
        this.businessHour = "09:00-21:00";
        this.closedDate = "매주 일요일";
        this.noticeMessage = "오늘도 신선하게 배송합니다.";
        this.isDisplay = true;
    }

    public Long getStoreId() {
        return storeId;
    }

    public String getName() {
        return name;
    }
}
