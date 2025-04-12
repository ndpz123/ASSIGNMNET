package com.jetbrains.assign.service;

import com.jetbrains.assign.model.CartItem;
import com.jetbrains.assign.model.Product;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CartService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String FIREBASE_CART_BASE = "https://assignment-34f27-default-rtdb.asia-southeast1.firebasedatabase.app/cart";
    private static final String FIREBASE_PRODUCT_BASE = "https://assignment-34f27-default-rtdb.asia-southeast1.firebasedatabase.app/products";

    // Lấy toàn bộ giỏ hàng của user
    public List<CartItem> getAll(String userId) {
        String url = FIREBASE_CART_BASE + "/" + userId + ".json";
        ResponseEntity<Map<String, CartItem>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<CartItem> list = new ArrayList<>();
        if (response.getBody() != null) {
            response.getBody().forEach((id, item) -> {
                item.setId(id);
                list.add(item);
            });
        }
        return list;
    }

    // Thêm item vào giỏ hàng của user
    public void save(String userId, CartItem item) {
        String url = FIREBASE_CART_BASE + "/" + userId + ".json";
        HttpEntity<CartItem> request = new HttpEntity<>(item);
        restTemplate.postForObject(url, request, CartItem.class);
    }

    // Cập nhật lại item cụ thể trong giỏ hàng của user
    public void update(String userId, String itemId, CartItem item) {
        String url = FIREBASE_CART_BASE + "/" + userId + "/" + itemId + ".json";
        restTemplate.put(url, item);
    }

    // Xoá 1 item khỏi giỏ hàng
    public void delete(String userId, String itemId) {
        String url = FIREBASE_CART_BASE + "/" + userId + "/" + itemId + ".json";
        restTemplate.delete(url);
    }

    // Xoá toàn bộ giỏ hàng của user
    public void deleteAll(String userId) {
        String url = FIREBASE_CART_BASE + "/" + userId + ".json";
        restTemplate.delete(url);
    }

    // Lấy 1 item cụ thể từ giỏ hàng
    public CartItem getById(String userId, String itemId) {
        String url = FIREBASE_CART_BASE + "/" + userId + "/" + itemId + ".json";
        return restTemplate.getForObject(url, CartItem.class);
    }

    // Thêm phương thức trừ số lượng sản phẩm khi thanh toán
    public void updateStockOnCheckout(String userId, List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            String productUrl = FIREBASE_PRODUCT_BASE + "/" + item.getProductId() + ".json";
            Product product = restTemplate.getForObject(productUrl, Product.class);

            if (product != null && product.getStock() >= item.getQuantity()) {
                int newStock = product.getStock() - item.getQuantity();
                product.setStock(newStock);
                restTemplate.put(productUrl, product);
            } else {
                throw new RuntimeException("Sản phẩm " + item.getName() + " không đủ số lượng trong kho.");
            }
        }
    }
}