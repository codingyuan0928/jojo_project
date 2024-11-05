package com.group1.gosports_jojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatusUpdateRequest {
    private List<Integer> productIds;
    private Integer status;
}
