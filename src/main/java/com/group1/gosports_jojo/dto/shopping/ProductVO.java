package com.group1.gosports_jojo.dto.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO {

    private Integer supplierId;
    private String supplierName;
    private List<ProductItem> productList;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductItem{
        private Integer productId;
        private String productName;
        private String productSpec;
        private Integer price;
        private Integer quantity;

    }

}
