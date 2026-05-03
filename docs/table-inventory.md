# Table Inventory

## SEEDED

아래 테이블은 로컬 비교 서버에서 실제 생성/시드됩니다.

| table | status | expected row count | reason |
|---|---|---:|---|
| member | SEEDED | 100 | 비교용 회원 |
| store | SEEDED | 20 | 비교용 상점 |
| category | SEEDED | 5 | 카테고리 기준 |
| product | SEEDED | 500 | SKU 모체 |
| sku | SEEDED | 500 | 장바구니 조회 대상 |
| sku_image | SEEDED | 500 | SKU 표시 정보 |
| inventory | SEEDED | 500 | 재고 조회 대상 |
| cart | SEEDED | 100 | 회원별 장바구니 |
| cart_item | SEEDED | 2080 | 비교용 장바구니 부하 데이터 |
| product_option | SEEDED | 5 | 옵션 메타 |
| product_option_value | SEEDED | 25 | 옵션 값 |
| option_value_sku | SEEDED | 500 | 옵션-SKU 연결 |
| certification | SEEDED | 5 | 상점 인증 참조 |
| store_certification | SEEDED | 30 | 상점 인증 연결 |

## EMPTY

아래 테이블은 장바구니 조회 비교 범위 밖이며, 운영 스키마 참고용으로만 관리합니다.

| table | status | expected row count | empty reason |
|---|---|---:|---|
| orders | EMPTY | 0 | 주문 흐름은 비교 범위 밖 |
| order_item | EMPTY | 0 | 주문 흐름은 비교 범위 밖 |
| payment | EMPTY | 0 | 결제 흐름은 비교 범위 밖 |
| payment_transaction | EMPTY | 0 | 결제 정산은 비교 범위 밖 |
| delivery | EMPTY | 0 | 배송 흐름은 비교 범위 밖 |
| receipt | EMPTY | 0 | 영수증 도메인은 비교 범위 밖 |
| card_receipt | EMPTY | 0 | 영수증 도메인은 비교 범위 밖 |
| virtual_account_receipt | EMPTY | 0 | 영수증 도메인은 비교 범위 밖 |
| store_line | EMPTY | 0 | 영수증 도메인은 비교 범위 밖 |
| claim | EMPTY | 0 | 클레임 도메인은 비교 범위 밖 |
| claim_item | EMPTY | 0 | 클레임 도메인은 비교 범위 밖 |
| claim_image | EMPTY | 0 | 클레임 도메인은 비교 범위 밖 |
| review | EMPTY | 0 | 리뷰 도메인은 비교 범위 밖 |
| review_comment | EMPTY | 0 | 리뷰 도메인은 비교 범위 밖 |
| review_image | EMPTY | 0 | 리뷰 도메인은 비교 범위 밖 |
| coupon_template | EMPTY | 0 | 쿠폰 도메인은 비교 범위 밖 |
| member_coupon | EMPTY | 0 | 쿠폰 도메인은 비교 범위 밖 |
| monthly_coupon_issue_snapshot | EMPTY | 0 | 월간 쿠폰 배치는 비교 범위 밖 |
| discount_template | EMPTY | 0 | 할인 도메인은 비교 범위 밖 |
| sku_discount | EMPTY | 0 | 할인 도메인은 비교 범위 밖 |
| order_discount | EMPTY | 0 | 할인 도메인은 비교 범위 밖 |
| order_item_discount | EMPTY | 0 | 할인 도메인은 비교 범위 밖 |
| notification_type | EMPTY | 0 | 알림 도메인은 비교 범위 밖 |
| notification_setting | EMPTY | 0 | 알림 도메인은 비교 범위 밖 |
| notification | EMPTY | 0 | 알림 도메인은 비교 범위 밖 |
| point_balance | EMPTY | 0 | 포인트 도메인은 비교 범위 밖 |
| point_history | EMPTY | 0 | 포인트 도메인은 비교 범위 밖 |
| address | EMPTY | 0 | 배송지 도메인은 비교 범위 밖 |
| credential | EMPTY | 0 | 인증 도메인은 비교 범위 밖 |
| member_store_notification | EMPTY | 0 | 상점 알림 도메인은 비교 범위 밖 |
| member_store_favorite | EMPTY | 0 | 찜 도메인은 비교 범위 밖 |
| store_background_image | EMPTY | 0 | 상점 부가 정보는 비교 범위 밖 |
| product_badge | EMPTY | 0 | 상품 배지는 비교 범위 밖 |
| sku_badge | EMPTY | 0 | 상품 배지는 비교 범위 밖 |
| inventory_history | EMPTY | 0 | 재고 이력은 비교 범위 밖 |
| popular_category | EMPTY | 0 | 탐색 도메인은 비교 범위 밖 |
| keyword_stat | EMPTY | 0 | 검색 통계는 비교 범위 밖 |
| subscription | EMPTY | 0 | 구독 도메인은 비교 범위 밖 |

## NOT_CREATED

아래 테이블은 로컬 장바구니 비교 프로젝트에서 생성하지 않습니다.

| table | status | row count | reason |
|---|---|---:|---|
| outbox | NOT_CREATED | N/A | 이벤트 발행 인프라 제외 |
| inbox | NOT_CREATED | N/A | 이벤트 소비 인프라 제외 |
| order_no_daily_sequence | NOT_CREATED | N/A | 주문번호 발급 로직 제외 |
