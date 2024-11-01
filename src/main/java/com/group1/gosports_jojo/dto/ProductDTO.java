package com.group1.gosports_jojo.dto;
//顯示在前端所要的資料
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private int productId;
    private String productName;
    private int price;
    private int stock;
    private Integer productStatus; // 0:下架 1:上架
}
