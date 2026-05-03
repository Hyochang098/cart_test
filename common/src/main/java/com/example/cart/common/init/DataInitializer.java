package com.example.cart.common.init;

import com.example.cart.common.entity.Cart;
import com.example.cart.common.entity.CartItem;
import com.example.cart.common.entity.Category;
import com.example.cart.common.entity.Certification;
import com.example.cart.common.entity.Inventory;
import com.example.cart.common.entity.Member;
import com.example.cart.common.entity.OptionValueSku;
import com.example.cart.common.entity.Product;
import com.example.cart.common.entity.ProductOption;
import com.example.cart.common.entity.ProductOptionValue;
import com.example.cart.common.entity.Province;
import com.example.cart.common.entity.Sku;
import com.example.cart.common.entity.SkuImage;
import com.example.cart.common.entity.Store;
import com.example.cart.common.entity.StoreCertification;
import com.example.cart.common.repository.CartItemRepository;
import com.example.cart.common.repository.CartRepository;
import com.example.cart.common.repository.CategoryRepository;
import com.example.cart.common.repository.CertificationRepository;
import com.example.cart.common.repository.InventoryRepository;
import com.example.cart.common.repository.MemberRepository;
import com.example.cart.common.repository.OptionValueSkuRepository;
import com.example.cart.common.repository.ProductOptionRepository;
import com.example.cart.common.repository.ProductOptionValueRepository;
import com.example.cart.common.repository.ProductRepository;
import com.example.cart.common.repository.SkuImageRepository;
import com.example.cart.common.repository.SkuRepository;
import com.example.cart.common.repository.StoreCertificationRepository;
import com.example.cart.common.repository.StoreRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;
    private final SkuImageRepository skuImageRepository;
    private final InventoryRepository inventoryRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductOptionValueRepository productOptionValueRepository;
    private final OptionValueSkuRepository optionValueSkuRepository;
    private final CertificationRepository certificationRepository;
    private final StoreCertificationRepository storeCertificationRepository;

    public DataInitializer(
        MemberRepository memberRepository,
        StoreRepository storeRepository,
        CategoryRepository categoryRepository,
        ProductRepository productRepository,
        SkuRepository skuRepository,
        SkuImageRepository skuImageRepository,
        InventoryRepository inventoryRepository,
        CartRepository cartRepository,
        CartItemRepository cartItemRepository,
        ProductOptionRepository productOptionRepository,
        ProductOptionValueRepository productOptionValueRepository,
        OptionValueSkuRepository optionValueSkuRepository,
        CertificationRepository certificationRepository,
        StoreCertificationRepository storeCertificationRepository
    ) {
        this.memberRepository = memberRepository;
        this.storeRepository = storeRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.skuRepository = skuRepository;
        this.skuImageRepository = skuImageRepository;
        this.inventoryRepository = inventoryRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productOptionRepository = productOptionRepository;
        this.productOptionValueRepository = productOptionValueRepository;
        this.optionValueSkuRepository = optionValueSkuRepository;
        this.certificationRepository = certificationRepository;
        this.storeCertificationRepository = storeCertificationRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (memberRepository.count() > 0) {
            return;
        }

        List<Category> categories = seedCategories();
        List<Store> stores = seedStores();
        List<Certification> certifications = seedCertifications();
        seedStoreCertification(stores, certifications);
        List<ProductOption> options = seedProductOptions(categories);
        List<ProductOptionValue> optionValues = seedProductOptionValues(options);

        List<Product> products = seedProducts(500);
        List<Sku> skus = seedSkus(products, stores, categories);
        seedOptionValueSkus(skus, optionValues);
        seedSkuImages(skus);
        seedInventories(skus);

        List<Member> members = seedMembers(100);
        List<Cart> carts = seedCarts(members);
        seedCartItems(carts, skus);
    }

    private List<Category> seedCategories() {
        List<Category> categoryList = List.of(
            new Category("의류"),
            new Category("식품"),
            new Category("가전"),
            new Category("뷰티"),
            new Category("생활용품")
        );
        return categoryRepository.saveAll(categoryList);
    }

    private List<Store> seedStores() {
        Province[] provinces = Province.values();
        List<Store> stores = new ArrayList<>();
        for (int index = 0; index < 20; index++) {
            stores.add(new Store(
                "스토어-" + (index + 1),
                provinces[index % provinces.length],
                "대표자-" + (index + 1),
                "100-00-" + String.format("%04d", index + 1),
                "서울시 테스트구 테스트로 " + (index + 1),
                "010-1000-" + String.format("%04d", index + 1)
            ));
        }
        return storeRepository.saveAll(stores);
    }

    private List<Certification> seedCertifications() {
        List<Certification> certificationList = List.of(
            new Certification("친환경", "/images/cert/eco.png", "OFFICIAL"),
            new Certification("HACCP", "/images/cert/haccp.png", "OFFICIAL"),
            new Certification("오늘입고", "/images/cert/today.png", "INTERNAL"),
            new Certification("산지직송", "/images/cert/direct.png", "INTERNAL"),
            new Certification("인기상점", "/images/cert/popular.png", "INTERNAL")
        );
        return certificationRepository.saveAll(certificationList);
    }

    private void seedStoreCertification(List<Store> stores, List<Certification> certifications) {
        List<StoreCertification> storeCertifications = new ArrayList<>();
        for (int index = 0; index < stores.size(); index++) {
            Store store = stores.get(index);
            Certification primaryCertification = certifications.get(index % certifications.size());
            Certification secondaryCertification = certifications.get((index + 1) % certifications.size());
            storeCertifications.add(new StoreCertification(store, primaryCertification));
            if (index % 2 == 0) {
                storeCertifications.add(new StoreCertification(store, secondaryCertification));
            }
        }
        storeCertificationRepository.saveAll(storeCertifications);
    }

    private List<ProductOption> seedProductOptions(List<Category> categories) {
        List<ProductOption> options = new ArrayList<>();
        for (Category category : categories) {
            options.add(new ProductOption(category, "사이즈"));
        }
        return productOptionRepository.saveAll(options);
    }

    private List<ProductOptionValue> seedProductOptionValues(List<ProductOption> options) {
        String[] sizeLabels = {"S", "M", "L", "XL", "XXL"};
        List<ProductOptionValue> values = new ArrayList<>();
        for (ProductOption option : options) {
            for (String sizeLabel : sizeLabels) {
                values.add(new ProductOptionValue(option, sizeLabel));
            }
        }
        return productOptionValueRepository.saveAll(values);
    }

    private List<Product> seedProducts(int productCount) {
        List<Product> products = new ArrayList<>();
        for (int index = 0; index < productCount; index++) {
            products.add(new Product("상품-" + (index + 1)));
        }
        return productRepository.saveAll(products);
    }

    private List<Sku> seedSkus(List<Product> products, List<Store> stores, List<Category> categories) {
        List<Sku> skus = new ArrayList<>();
        for (int index = 0; index < products.size(); index++) {
            Product product = products.get(index);
            Store store = stores.get(index % stores.size());
            Category category = categories.get(index % categories.size());
            int price = 1000 + (index % 40) * 250;
            skus.add(new Sku(product, store, category, "SKU-" + (index + 1), price));
        }
        return skuRepository.saveAll(skus);
    }

    private void seedOptionValueSkus(List<Sku> skus, List<ProductOptionValue> optionValues) {
        if (optionValues.isEmpty()) {
            throw new IllegalStateException("옵션 값이 비어 있어 option_value_sku를 생성할 수 없습니다.");
        }
        List<OptionValueSku> mappings = new ArrayList<>();
        for (int index = 0; index < skus.size(); index++) {
            Sku sku = skus.get(index);
            ProductOptionValue value = optionValues.get(index % optionValues.size());
            mappings.add(new OptionValueSku(value, sku, value.getOption().getOptionId()));
        }
        optionValueSkuRepository.saveAll(mappings);
    }

    private void seedSkuImages(List<Sku> skus) {
        List<SkuImage> images = new ArrayList<>();
        for (Sku sku : skus) {
            images.add(new SkuImage(sku, "/images/sku/" + sku.getSkuId() + ".png"));
        }
        skuImageRepository.saveAll(images);
    }

    private void seedInventories(List<Sku> skus) {
        Random random = new Random(42);
        List<Inventory> inventories = new ArrayList<>();
        for (Sku sku : skus) {
            int onHandQuantity = 50 + random.nextInt(151);
            int reservedQuantity = random.nextInt(10);
            inventories.add(new Inventory(sku, onHandQuantity, reservedQuantity));
        }
        inventoryRepository.saveAll(inventories);
    }

    private List<Member> seedMembers(int memberCount) {
        List<Member> members = new ArrayList<>();
        for (int index = 0; index < memberCount; index++) {
            members.add(new Member(
                "회원-" + (index + 1),
                LocalDate.of(1990, 1, 1).plusDays(index),
                "닉네임-" + (index + 1),
                "010-2000-" + String.format("%04d", index + 1)
            ));
        }
        return memberRepository.saveAll(members);
    }

    private List<Cart> seedCarts(List<Member> members) {
        List<Cart> carts = new ArrayList<>();
        for (Member member : members) {
            carts.add(new Cart(member));
        }
        return cartRepository.saveAll(carts);
    }

    private void seedCartItems(List<Cart> carts, List<Sku> skus) {
        if (skus.size() < 100) {
            throw new IllegalStateException("SKU는 최소 100개 이상이어야 장바구니 시드를 만들 수 있습니다.");
        }

        List<CartItem> cartItems = new ArrayList<>();
        int skuCursor = 0;
        for (int cartIndex = 0; cartIndex < carts.size(); cartIndex++) {
            Cart cart = carts.get(cartIndex);
            int itemCount = (cartIndex == 0) ? 100 : 20;
            for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
                Sku sku = skus.get(skuCursor % skus.size());
                int quantity = (itemIndex % 3) + 1;
                cartItems.add(new CartItem(cart, sku, quantity));
                skuCursor++;
            }
        }
        cartItemRepository.saveAll(cartItems);
    }
}
