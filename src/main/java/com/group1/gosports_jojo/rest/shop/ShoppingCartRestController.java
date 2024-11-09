package com.group1.gosports_jojo.rest.shop;

import com.group1.gosports_jojo.dto.shopping.ProductVO;
import com.group1.gosports_jojo.service.impl.shop.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    // 加入商品
    @PostMapping("/add")
    @Operation(summary = "加入購物車",description = "userId: 使用者編號, productId: 商品編號, quantity: 要加入購物車的商品數量")
    public ResponseEntity<String> addProductToCart(@RequestParam Integer userId, @RequestParam Integer productId, @RequestParam Integer quantity) {
        shoppingCartService.addProductToCart(userId, productId, quantity);
        return ResponseEntity.ok("商品成功加入購物車");
    }


    // 移除單個商品
    @DeleteMapping("/remove")
    @Operation(summary = "商品從購物車中刪除",description = "userId: 使用者編號, productId: 商品編號   該商品會從購物車內直接被移除")
    public ResponseEntity<String> removeProductFromCart(@RequestParam Integer userId, @RequestParam Integer productId) {
        shoppingCartService.removeProductFromCart(userId, productId);
        return ResponseEntity.ok("商品成功移出購物車");
    }

    // 移除多個商品
    @DeleteMapping("/remove/batchproducts")
    @Operation(summary = "商品從購物車中多個刪除",description = "")
    public ResponseEntity<String> removeProductsFromCart(@RequestParam Integer userId, @RequestParam List<Integer> productIds) {
        shoppingCartService.removeProductsFromCart(userId, productIds);
        return ResponseEntity.ok("多個商品成功移出購物車");
    }

    // 更新商品數量
    @PutMapping("/update")
    @Operation(summary = "更新商品在購物車中的數量",description = "userId: 使用者編號, productId: 商品編號, quantity: 更改購物車中指定商品的數量")
    public ResponseEntity<String> updateProductQuantity(@RequestParam Integer userId, @RequestParam Integer productId, @RequestParam Integer quantity) {
        try {
            shoppingCartService.updateProductQuantity(userId, productId, quantity);
            return ResponseEntity.ok("商品在購物車中的數量成功更新");
        } catch (RuntimeException e) {
            // 捕捉例外並回傳錯誤訊息
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 查看購物車
    @GetMapping("/view")
    @Operation(summary = "查看購物中的商品",description = "會回傳 ProductVO 包含 商品編號, 商品名稱, 剩餘庫存")
    public ResponseEntity<List<ProductVO>> viewCart(@RequestParam Integer userId) {
        List<ProductVO> cart = shoppingCartService.viewCart(userId);
        return ResponseEntity.ok(cart);
    }

    // 查看購物車
    @GetMapping("/picture/{productId}")
    @Operation(summary = "商品圖片",description = "")
    public ResponseEntity<byte[]> viewPicture(@PathVariable Integer productId) {
        byte[] image = shoppingCartService.productPicture(productId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 根據圖片類型設置 Content-Type
                .body(image);
    }

}
