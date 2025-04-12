package com.jetbrains.assign.controller;

import com.jetbrains.assign.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jetbrains.assign.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Lấy toàn bộ giỏ hàng của user
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable String userId) {
        List<CartItem> cartItems = cartService.getAll(userId);
        return ResponseEntity.ok(cartItems);
    }

    // Thêm 1 item vào giỏ hàng
    @PostMapping("/{userId}")
    public ResponseEntity<Void> addItem(@PathVariable String userId, @RequestBody CartItem item) {
        cartService.save(userId, item);
        return ResponseEntity.ok().build();
    }

    // Cập nhật số lượng của 1 item trong giỏ hàng
    @PatchMapping("/{userId}/{itemId}")
    public ResponseEntity<Void> updateQuantity(@PathVariable String userId,
                                               @PathVariable String itemId,
                                               @RequestBody CartItem item) {
        cartService.update(userId, itemId, item);
        return ResponseEntity.ok().build();
    }

    // Xoá 1 item khỏi giỏ hàng
    @DeleteMapping("/{userId}/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable String userId,
                                           @PathVariable String itemId) {
        cartService.delete(userId, itemId);
        return ResponseEntity.ok().build();
    }

    // Xoá toàn bộ giỏ hàng của user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.deleteAll(userId);
        return ResponseEntity.ok().build();
    }

    // Lấy 1 item cụ thể trong giỏ hàng
    @GetMapping("/{userId}/{itemId}")
    public ResponseEntity<CartItem> getItem(@PathVariable String userId,
                                            @PathVariable String itemId) {
        CartItem item = cartService.getById(userId, itemId);
        return ResponseEntity.ok(item);
    }
    // Thanh toán giỏ hàng
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable String userId) {
        List<CartItem> cartItems = cartService.getAll(userId);

        if (cartItems.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Trừ số lượng sản phẩm trong kho
            cartService.updateStockOnCheckout(userId, cartItems);

            // Sau khi thanh toán thành công, xóa toàn bộ giỏ hàng của user
            cartService.deleteAll(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}