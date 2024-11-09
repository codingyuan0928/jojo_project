package com.group1.gosports_jojo.dto.shopping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.BadLocationException;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductStatusUpdateRequest {
    private List<Integer> productIds;
    private Integer status;
    private Boolean isDelete;
}
