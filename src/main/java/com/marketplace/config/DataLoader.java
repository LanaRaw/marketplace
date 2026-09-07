package com.marketplace.config;

import com.marketplace.model.*;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            // Создаем пользователей с зашифрованными паролями
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@marketplace.com");
            admin.setPassword(passwordEncoder.encode("admin123"));  // ← ЗАШИФРОВАНО!
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            userRepository.save(admin);

            User seller = new User();
            seller.setUsername("seller");
            seller.setEmail("seller@marketplace.com");
            seller.setPassword(passwordEncoder.encode("seller123"));  // ← ЗАШИФРОВАНО!
            seller.setRole(Role.SELLER);
            seller.setIsActive(true);
            userRepository.save(seller);

            User buyer = new User();
            buyer.setUsername("buyer");
            buyer.setEmail("buyer@marketplace.com");
            buyer.setPassword(passwordEncoder.encode("buyer123"));  // ← ЗАШИФРОВАНО!
            buyer.setRole(Role.BUYER);
            buyer.setIsActive(true);
            userRepository.save(buyer);

            // Товары (без изменений)
            Product product1 = new Product();
            product1.setName("iPhone 15 Pro");
            product1.setDescription("Новейший смартфон от Apple с титановым корпусом");
            product1.setPrice(new BigDecimal("999.99"));
            product1.setQuantityInStock(10);
            product1.setCategory(Category.ELECTRONICS);
            product1.setSeller(seller);
            product1.setImageUrl("https://via.placeholder.com/300x200?text=iPhone");
            product1.setIsActive(true);
            productRepository.save(product1);

            Product product2 = new Product();
            product2.setName("Книга 'Java для начинающих'");
            product2.setDescription("Полное руководство по Java с примерами и задачами");
            product2.setPrice(new BigDecimal("49.99"));
            product2.setQuantityInStock(50);
            product2.setCategory(Category.BOOKS);
            product2.setSeller(seller);
            product2.setImageUrl("https://via.placeholder.com/300x200?text=Java+Book");
            product2.setIsActive(true);
            productRepository.save(product2);

            Product product3 = new Product();
            product3.setName("Футболка с принтом");
            product3.setDescription("Хлопковая футболка с крутым принтом");
            product3.setPrice(new BigDecimal("25.00"));
            product3.setQuantityInStock(30);
            product3.setCategory(Category.CLOTHING);
            product3.setSeller(seller);
            product3.setImageUrl("https://via.placeholder.com/300x200?text=T-Shirt");
            product3.setIsActive(true);
            productRepository.save(product3);

            System.out.println("✅ Тестовые данные загружены с зашифрованными паролями!");
            System.out.println("👤 Админ: admin@marketplace.com / admin123");
            System.out.println("🛒 Продавец: seller@marketplace.com / seller123");
            System.out.println("🛍️ Покупатель: buyer@marketplace.com / buyer123");
        }
    }
}