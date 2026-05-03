package com.example.cart.display;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.cart")
@EnableJpaRepositories(basePackages = "com.example.cart.common.repository")
@EntityScan(basePackages = "com.example.cart.common.entity")
public class CartDisplayServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartDisplayServerApplication.class, args);
    }
}
