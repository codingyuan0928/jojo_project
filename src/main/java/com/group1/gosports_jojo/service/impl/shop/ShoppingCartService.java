package com.group1.gosports_jojo.service.impl.shop;


import com.group1.gosports_jojo.dto.shopping.ProductVO;
import com.group1.gosports_jojo.entity.Picture;
import com.group1.gosports_jojo.entity.Product;
import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    @Autowired
    private  RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;


    // 加入商品到購物車
    public void addProductToCart(Integer userId, Integer productId, int quantity) {
        String cartKey = "cart:" + userId;
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到商品"));

        if (product.getStock() >= quantity) {
            // 檢查購物車是否已經有此商品
            Integer currentQuantity = (Integer) redisTemplate.opsForHash().get(cartKey, productId.toString());

            if (currentQuantity != null) {
                // 如果購物車已有此商品，則累加數量
                quantity += currentQuantity;
            }

            // 如果沒有
            redisTemplate.opsForHash().put(cartKey, productId.toString(), quantity);

        } else {
            throw new RuntimeException("商品數量不夠");
        }
        //redisTemplate.opsForHash().put(cartKey, productId.toString(), quantity);
    }

    // 從購物車中移除單個商品
    public void removeProductFromCart(Integer userId, Integer productId) {
        String cartKey = "cart:" + userId;
        redisTemplate.opsForHash().delete(cartKey, productId.toString());

        // TODO: 檢查是否還有庫存

    }

    // 從購物車中移除多個商品
    public void removeProductsFromCart(Integer userId, List<Integer> productId) {
        String cartKey = "cart:" + userId;
        for (Integer id : productId) {
            redisTemplate.opsForHash().delete(cartKey, id.toString());
        }

        // TODO: 檢查是否還有庫存

    }

    // 更新購物車中的商品數量
    public void updateProductQuantity(Integer userId, Integer productId, Integer quantity) {
        String cartKey = "cart:" + userId;
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("找不到商品"));

        // 檢查商品是否上架
        if (product.getProductStatus() != 1) {
            // 商品已下架，從購物車中刪除該商品
            redisTemplate.opsForHash().delete(cartKey, productId.toString());
            throw new RuntimeException("商品已下架，無法添加到購物車");
        }

        // 檢查庫存是否足夠
        if (product.getStock() < quantity) {
            throw new RuntimeException("庫存不足，無法添加到購物車");
        }

        // 如果商品已上架且庫存足夠，則更新購物車中的商品數量
        redisTemplate.opsForHash().put(cartKey, productId.toString(), quantity);

    }

    public List<ProductVO> viewCart(Integer userId) {
        String cartKey = "cart:" + userId;

        // 從 Redis 中獲取購物車所有產品購買數量並轉換為 Integer Map
        Map<Integer, Integer> cartResult = redisTemplate.opsForHash().entries(cartKey).entrySet().stream()
                .collect(Collectors.toMap(
                        e -> Integer.parseInt(e.getKey().toString()), // 將 key 轉換為 Integer
                        e -> Integer.parseInt(e.getValue().toString()) // 將 value 轉換為 Integer
                ));

        // 一次性查詢所有產品資訊，並將結果存到 Map 中，減少重複查詢
        List<Product> products = productRepository.findAllById(cartResult.keySet());

        // 廠商 Map，使用 Vendor 作為 Key，商品清單作為 Value
        Map<Vendor, List<ProductVO.ProductItem>> vendorMap = new HashMap<>();

        products.forEach(product -> {
            Vendor vendor = product.getVendor();

            // 構建商品項目
            ProductVO.ProductItem productItem = new ProductVO.ProductItem(
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductSpec(),
                    product.getPrice(),
                    cartResult.get(product.getProductId())
            );

            // 如果 Map 中已經存在該 Vendor，則添加到該 Vendor 的商品清單
            vendorMap.computeIfAbsent(vendor, k -> new ArrayList<>()).add(productItem);
        });

        // 最後將 Map 轉換為 List<ProductVO>
        return vendorMap.entrySet().stream()
                .map(entry -> new ProductVO(
                        entry.getKey().getVendorId(),
                        entry.getKey().getShopName(),
                        entry.getValue()
                ))
                .sorted(Comparator.comparing(ProductVO::getSupplierId)) // 依照 vendorId 排序
                .collect(Collectors.toList());
    }


    public byte[] productPicture(Integer productId) {
        List<Picture> byProductProductId = pictureRepository.findByProduct_ProductId(productId);
        if(!byProductProductId.isEmpty()){
            Picture firstPicture = byProductProductId.get(0);
            return firstPicture.getPicture();
        }
        return null;
    }

}
