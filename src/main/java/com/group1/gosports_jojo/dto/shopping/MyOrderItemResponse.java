package com.group1.gosports_jojo.dto.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyOrderItemResponse {
    private Integer orderId;
    private Integer userId;
    private Integer totalAmount;
    private Timestamp createdDatetime;
    private Integer vendorId;
    private Timestamp pickupDate;
    private Integer productId;
    private Integer quantity;
    private Integer price;
    private String pickAddress;
    private String productName;
    private String shopName;
    private String username;
    private String email;
}
