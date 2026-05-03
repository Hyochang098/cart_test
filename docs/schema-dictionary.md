# Schema Dictionary

본 문서는 `migration` 폴더(V1~V24)의 최종 스키마 기준으로 작성했습니다.

- 상태 정의
  - `SEEDED`: 로컬 비교 서버에서 실제 생성/시드되는 테이블
  - `EMPTY`: 운영 스키마 참고용(장바구니 조회 비교 범위 밖), 로컬 비교 시 데이터 0행 기준
  - `NOT_CREATED`: 로컬 비교 서버에서 생성하지 않는 테이블

## SEEDED

### 1) member
- purpose: 회원 기본 정보
- status: `SEEDED`
- columns
  - `member_id BIGSERIAL NOT NULL PK`
  - `name VARCHAR(50) NOT NULL`
  - `birth DATE NOT NULL`
  - `gender VARCHAR(10) NOT NULL DEFAULT 'UNKNOWN'`
  - `profile_image_url VARCHAR(255) NULL`
  - `phone_number VARCHAR(20) NULL`
  - `nick_name VARCHAR(50) NOT NULL`
  - `role VARCHAR(20) NOT NULL DEFAULT 'VISITOR'` (V22: `VERIFIED` 포함)
  - `status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'`
  - `created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`
  - `updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`
  - `deleted_at TIMESTAMPTZ NULL`
- relations: 없음(루트)
- seed policy: 100명 생성

### 2) store
- purpose: 상점 기본 정보
- status: `SEEDED`
- columns
  - `store_id BIGSERIAL NOT NULL PK`
  - `name VARCHAR(50) NOT NULL`
  - `province VARCHAR(20) NOT NULL`
  - `owner_name VARCHAR(30) NOT NULL`
  - `business_number VARCHAR(20) NOT NULL`
  - `address VARCHAR(200) NOT NULL`
  - `phone_number VARCHAR(20) NOT NULL`
  - `image VARCHAR(255) NULL`
  - `short_description VARCHAR(30) NOT NULL`
  - `description VARCHAR(100) NOT NULL`
  - `business_hour VARCHAR(255) NOT NULL`
  - `closed_date VARCHAR(255) NOT NULL`
  - `notice_message VARCHAR(100) NOT NULL`
  - `rating DECIMAL(2,1) NOT NULL DEFAULT 0.0`
  - `review_count INT NOT NULL DEFAULT 0`
  - `favorite_count INT NOT NULL DEFAULT 0`
  - `notification_count INT NOT NULL DEFAULT 0`
  - `recent_order_count INT NOT NULL DEFAULT 0`
  - `created_at TIMESTAMP NOT NULL DEFAULT now()`
  - `updated_at TIMESTAMP NOT NULL DEFAULT now()`
  - `deleted_at TIMESTAMP NULL`
  - `is_display BOOLEAN NOT NULL DEFAULT false`
- relations: 없음(루트)
- seed policy: 20개 생성

### 3) category
- purpose: 카테고리 마스터
- status: `SEEDED`
- columns
  - `category_id BIGSERIAL NOT NULL PK`
  - `parent_category_id BIGINT NULL FK -> category.category_id`
  - `name VARCHAR(20) NOT NULL`
  - `image VARCHAR(255) NULL`
- relations: 자기참조 FK
- seed policy: 5개 생성

### 4) product
- purpose: 상품 마스터
- status: `SEEDED`
- columns
  - `product_id BIGSERIAL NOT NULL PK`
  - `product_name VARCHAR(50) NOT NULL`
  - `deleted_at TIMESTAMP NULL`
- relations: 없음
- seed policy: 500개 생성

### 5) sku
- purpose: 판매 단위 SKU
- status: `SEEDED`
- columns
  - `sku_id BIGSERIAL NOT NULL PK`
  - `product_id BIGINT NOT NULL FK -> product.product_id`
  - `store_id BIGINT NOT NULL FK -> store.store_id`
  - `category_id BIGINT NOT NULL FK -> category.category_id`
  - `sku_name VARCHAR(50) NOT NULL`
  - `price INT NOT NULL`
  - `selling_status VARCHAR(20) NULL`
  - `sku_review_count INT NOT NULL`
  - `sku_info JSONB NOT NULL` (V7/V13 반영)
  - `sku_description VARCHAR(100) NOT NULL`
  - `deleted_at TIMESTAMP NULL`
  - `is_display BOOLEAN NOT NULL DEFAULT true`
  - `item VARCHAR(255) NOT NULL DEFAULT ''` (V5 반영)
- relations: product/store/category 참조
- seed policy: 500개 생성

### 6) sku_image
- purpose: SKU 대표 이미지
- status: `SEEDED`
- columns
  - `sku_image_id BIGSERIAL NOT NULL PK`
  - `sku_id BIGINT NOT NULL FK -> sku.sku_id`
  - `image VARCHAR(255) NOT NULL`
- relations: sku 참조
- seed policy: SKU당 1개(500개)

### 7) inventory
- purpose: SKU 재고
- status: `SEEDED`
- columns
  - `inventory_id BIGSERIAL NOT NULL PK`
  - `sku_id BIGINT NOT NULL UNIQUE FK -> sku.sku_id`
  - `on_hand_quantity INT NOT NULL DEFAULT 0`
  - `reserved_quantity INT NOT NULL DEFAULT 0`
  - `available_quantity INT GENERATED`
  - `created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`
  - `updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`
- relations: sku 참조
- seed policy: SKU당 1개(500개)

### 8) cart
- purpose: 회원별 장바구니 헤더
- status: `SEEDED`
- columns
  - `cart_id BIGSERIAL NOT NULL PK`
  - `member_id BIGINT NOT NULL UNIQUE FK -> member.member_id`
  - `created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`
  - `updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`
- relations: member 참조
- seed policy: 회원당 1개(100개)

### 9) cart_item
- purpose: 장바구니 아이템
- status: `SEEDED`
- columns
  - `cart_item_id BIGSERIAL NOT NULL PK`
  - `cart_id BIGINT NOT NULL FK -> cart.cart_id`
  - `sku_id BIGINT NOT NULL FK -> sku.sku_id`
  - `quantity INT NOT NULL DEFAULT 1`
  - `created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`
  - `updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()`
  - `UNIQUE(cart_id, sku_id)`
- relations: cart/sku 참조
- seed policy: member1=100개, 나머지99명=각20개(총 2,080개)

### 10) product_option
- purpose: 카테고리 옵션
- status: `SEEDED`
- columns
  - `option_id BIGSERIAL NOT NULL PK`
  - `category_id BIGINT NOT NULL FK -> category.category_id`
  - `name VARCHAR(20) NULL`
  - `is_display BOOLEAN NOT NULL DEFAULT true` (V20 반영)
- relations: category 참조
- seed policy: 카테고리당 1개(총 5개)

### 11) product_option_value
- purpose: 옵션 값
- status: `SEEDED`
- columns
  - `option_value_id BIGSERIAL NOT NULL PK`
  - `option_id BIGINT NOT NULL FK -> product_option.option_id`
  - `value_name VARCHAR(20) NULL`
- relations: product_option 참조
- seed policy: 옵션당 5개(총 25개)

### 12) option_value_sku
- purpose: 옵션 값과 SKU 매핑
- status: `SEEDED`
- columns
  - `mapping_id BIGSERIAL NOT NULL PK`
  - `option_value_id BIGINT NOT NULL FK -> product_option_value.option_value_id`
  - `sku_id BIGINT NOT NULL FK -> sku.sku_id`
  - `option_id BIGINT NOT NULL`
- relations: option_value/sku 참조
- seed policy: SKU당 1개(500개)

### 13) certification
- purpose: 인증마크 마스터
- status: `SEEDED`
- columns
  - `certification_id BIGSERIAL NOT NULL PK`
  - `name VARCHAR(30) NOT NULL UNIQUE`
  - `image VARCHAR(255) NOT NULL`
  - `type VARCHAR(20) NOT NULL`
- relations: 없음
- seed policy: 5개 생성

### 14) store_certification
- purpose: 상점-인증마크 매핑
- status: `SEEDED`
- columns
  - `store_certification_id BIGSERIAL NOT NULL PK`
  - `store_id BIGINT NOT NULL FK -> store.store_id`
  - `certification_id BIGINT NOT NULL FK -> certification.certification_id`
  - `UNIQUE(store_id, certification_id)`
- relations: store/certification 참조
- seed policy: 스토어당 1~2개

## EMPTY

아래는 운영 스키마 참고용이며 장바구니 조회 비교 범위 밖입니다. 로컬 비교 시 데이터는 0행 기준입니다.

### 주문/결제/배송
- `orders`: `order_id PK, member_id FK, order_no, order_status, total_item_amount, total_discount_amount, used_point_amount, final_amount, public_order_no(V12), created_at, updated_at, deleted_at`
- `order_item`: `order_item_id PK, order_id FK, sku_id, store_id, product_name, option_text, unit_price, quantity, item_amount, discount_amount, item_status(V12), claim_status, created_at, updated_at`
- `payment`: `payment_id PK, order_id FK, order_no, payment_key(nullable, V6), payment_method, payment_status(V9: PARTIAL_CANCELED 포함), payment_type, amount, requested_at, approved_at, secret(V6)`
- `payment_transaction`: `payment_transaction_id PK, payment_id FK, claim_id, transaction_type, amount, transaction_status, pg_transaction_id, created_at`
- `delivery`: `delivery_id PK, order_item_id FK, delivery_status(V10), tracking_number, carrier, receiver_name, receiver_phone, road_address, detail_address, request, optional, delivery_completed_at(V18), created_at, updated_at`
- `receipt`: `receipt_id PK, order_id, payment_id, order_no, payment_method, total_item_amount, delivery_fee, total_discount_amount, instant_discount_amount, bulk_discount_amount, coupon_discount_amount, used_point_amount, final_amount, created_at`
- `card_receipt`: `receipt_id PK/FK, card_company, transaction_type, installment_months, card_no_masked, approved_at, approval_no, pg_payment_id`
- `virtual_account_receipt`: `receipt_id PK/FK, bank_name, account_no, account_holder, depositor_name, approval_no, usage_type, buyer_identifier, tax_free_amount, transaction_amount, confirmed_at` (`receipt_receipt_id` 제거, V23)
- `store_line`: `store_line_id PK, receipt_id FK, store_id, product_name, quantity, taxable_amount, tax_free_amount, vat, total_amount`
- `order_discount`: `discount_id PK, order_id FK, member_coupon_id, name, discount_type, discount_value, applied_amount`
- `order_item_discount`: `item_discount_id PK, order_item_id FK, sku_discount_id NOT NULL(V12), discount_method NOT NULL(V12), name, discount_type, applied_quantity, discount_value, applied_amount`

### 클레임/리뷰
- `claim`: `claim_id PK, order_id FK, claim_type(V4), claim_status, total_item_amount, delivery_fee, revoked_discount_amount, restored_point_amount, refund_amount, created_at, processed_at`
- `claim_item`: `claim_item_id PK, claim_id FK, order_item_id FK, quantity, reason, detail_reason, claim_item_status, delivery_fee_policy, item_amount, delivery_fee, revoked_discount_amount, restored_point_amount, refund_amount, created_at, processed_at`
- `claim_image`: `claim_image_id PK, claim_id FK, image_url, created_at, updated_at, deleted_at`
- `review`: `review_id PK, member_id FK, order_item_id FK, content, rating, like_count, dislike_count, created_at, updated_at, deleted_at`
- `review_comment`: `comment_id PK, parent_comment_id FK, content, like_count, dislike_count, created_at, updated_at, deleted_at, review_id FK, member_id FK` (`user_id` 제거, V24)
- `review_image`: `review_image_id PK, review_id FK, image, created_at, updated_at, deleted_at`

### 쿠폰/포인트/알림/회원부가
- `coupon_template`: `coupon_template_id PK, name, type, discount_amount, discount_rate, min_order_amount, max_discount_amount, valid_from, valid_to, status, created_at, updated_at`
- `member_coupon`: `member_coupon_id PK, member_id FK, coupon_template_id FK, coupon_code, status, issued_at, expires_at, used_at, used_order_id`
- `monthly_coupon_issue_snapshot`: `monthly_coupon_issue_snapshot_id PK, target_year_month, coupon_template_id FK, member_id FK, created_at, issued_at`
- `discount_template`: `discount_template_id PK, name, discount_method, min_quantity, discount_type, discount_value, valid_from, valid_to, created_at`
- `sku_discount`: `sku_discount_id PK, sku_id FK, discount_template_id FK, applied_at, expired_at, max_count(V4)`
- `notification_type`: `notification_type_id PK, policy, event, title_template, body_template, default_target_type`
- `notification_setting`: `member_id PK/FK, delivery_enabled, store_enabled, cs_enabled, story_enabled, event_enabled, ad_push_enabled, store_video_auto_enabled, night_enabled`
- `notification`: `notification_id PK, member_id FK, notification_type_id FK, target_id, params, is_read, created_at, deleted_at, read_at, expires_at`
- `point_balance`: `point_balance_id PK, member_id FK UNIQUE, balance, updated_at`
- `point_history`: `point_history_id PK, member_id FK, order_id, refund_id, reason, amount, balance_after, created_at`
- `address`: `address_id PK, member_id FK, receiver_name, receiver_phone, address_label, address_name, road_address, detail_address, is_default, created_at, updated_at, deleted_at`
- `credential`: `credential_id PK, member_id FK, provider, identifier, password, is_verified, created_at, updated_at`
- `subscription`: `subscription_id PK, member_id FK, subscription_status, start_date, end_date, created_at, updated_at`

### 상점/탐색/재고 기타
- `member_store_notification`: `member_store_notification_id PK, member_id FK, store_id FK, created_at`
- `member_store_favorite`: `member_store_favorite_id PK, member_id FK, store_id FK, created_at`
- `store_background_image`: `store_image_id PK, store_id FK, image`
- `product_badge`: `product_badge_id PK, product_badge_name UNIQUE`
- `sku_badge`: `sku_badge_id PK, sku_id FK, product_badge_id FK, start_at, end_at, deleted_at(V14)` (`uq_sku_badge` 제거, V15)
- `inventory_history`: `inventory_history_id PK, inventory_id FK, ref_type, ref_id, change_quantity, change_reason(V8), created_at`
- `popular_category`: `popular_category_id PK, category_id FK, sku_id FK, keyword, image, rank, sold_count, created_at` (V5 재정의)
- `keyword_stat`: `stat_id PK, keyword_name UNIQUE, search_count, click_count, last_searched_at, relative_score`

## NOT_CREATED

### 1) outbox
- purpose: 이벤트 발행 아웃박스
- status: `NOT_CREATED`
- columns: `outbox_id PK, aggregate_type, aggregate_id, event_type, payload, outbox_status, retry_count, last_error, event_id, idempotency_key, max_retry_count, next_retry_at, sequence, created_at, published_at`
- relations: 없음
- empty/not_created reason: 메시징 인프라 비교 범위 밖

### 2) inbox
- purpose: 이벤트 중복처리 인박스(V6)
- status: `NOT_CREATED`
- columns: `id PK, idempotency_key, consumer_name, processed_at`
- relations: 없음
- empty/not_created reason: 컨슈머 인프라 비교 범위 밖

### 3) order_no_daily_sequence
- purpose: 주문번호 일자별 시퀀스(V12)
- status: `NOT_CREATED`
- columns: `order_date PK, next_sequence`
- relations: 없음
- empty/not_created reason: 주문 생성 기능 비교 범위 밖
