package com.jetbrains.assign.service;

import com.jetbrains.assign.model.Product;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProductService {

    private final RestTemplate restTemplate = new RestTemplate();

    // URL bảng product trên Firebase (dán trực tiếp)
    private static final String FIREBASE_PRODUCT_URL = "https://assignment-34f27-default-rtdb.asia-southeast1.firebasedatabase.app/product.json";

    public List<Product> getAll() {
        String url = FIREBASE_PRODUCT_URL + ".json";
        ResponseEntity<Map<String, Product>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<Product> list = new ArrayList<>();
        if (response.getBody() != null) {
            response.getBody().forEach((id, product) -> {
                product.setId(id);
                list.add(product);
            });
        }
        return list;
    }

    public void save(Product product) {
        String url = FIREBASE_PRODUCT_URL + ".json";
        HttpEntity<Product> request = new HttpEntity<>(product);
        restTemplate.postForObject(url, request, Product.class);
    }

    public void update(String id, Product product) {
        String url = FIREBASE_PRODUCT_URL + "/" + id + ".json";
        restTemplate.put(url, product);
    }

    public void delete(String id) {
        String url = FIREBASE_PRODUCT_URL + "/" + id + ".json";
        restTemplate.delete(url);
    }

    public Product getById(String id) {
        String url = FIREBASE_PRODUCT_URL + "/" + id + ".json";
        return restTemplate.getForObject(url, Product.class);
    }
}