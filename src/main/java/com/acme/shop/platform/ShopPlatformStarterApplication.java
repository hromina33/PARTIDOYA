package com.acme.shop.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShopPlatformStarterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopPlatformStarterApplication.class, args);
    }
}
