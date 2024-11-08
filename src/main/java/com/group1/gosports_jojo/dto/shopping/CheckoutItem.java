package com.group1.gosports_jojo.dto.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutItem {
    private Integer productId;
    private Integer quantity;
    private Integer price;
    private Integer vendorId;
    private String pickAddress;  // 取貨地址
}
