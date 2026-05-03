package com.example.cart.state;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example.cart")
@EnableJpaRepositories(basePackages = "com.example.cart.common.repository")
@EntityScan(basePackages = "com.example.cart.common.entity")
public class CartStateServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartStateServerApplication.class, args);
    }
}
