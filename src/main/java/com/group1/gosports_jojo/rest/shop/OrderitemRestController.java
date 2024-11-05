package com.group1.gosports_jojo.rest.shop;

import com.group1.gosports_jojo.dto.shopping.CheckoutRequest;
import com.group1.gosports_jojo.dto.shopping.MyOrderItemResponse;
import com.group1.gosports_jojo.entity.Order;
import com.group1.gosports_jojo.service.impl.shop.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class OrderitemRestController {
    @Autowired
    private OrderItemService orderItemService;
    @PostMapping("/order")
    @Operation(summary = "成功購買",description = "前往結帳按鈕")
    public ResponseEntity<String> processCheckout(@RequestBody CheckoutRequest checkoutRequest) {
        orderItemService.saveOrder(checkoutRequest);
        return ResponseEntity.ok("成功購買");
    }
    @GetMapping("/order/{userId}")
    @Operation(summary = "我的訂單列表",description = "")
    public ResponseEntity<List<Order>>getMyOrder(@PathVariable Integer userId){
        List<Order> myOrders = orderItemService.getMyOrder(userId);
        return ResponseEntity.ok(myOrders);
    }

//    @GetMapping("/orderitem/{orderitemId}")
//    @Operation(summary = "查看單筆訂單明細",description = "會回傳orderitemVO 包含產品id、數量、價格")
//    public ResponseEntity<List<Orderitem>> getAllOrderitem(@PathVariable Integer orderitemId){
//        List<Orderitem> orderitem = orderItemService.getAll(orderitemId);
//        return ResponseEntity.ok(orderitem);
//    }

    @GetMapping("/order/findOrderDetailsByOrderId/{orderId}")
    @Operation(summary = "我的訂單明細 popup",description = "")
    public ResponseEntity<List<MyOrderItemResponse>>findOrderDetailsByOrderId(@PathVariable Integer orderId){
        List<MyOrderItemResponse> myOrderitems = orderItemService.getmyOrderitems(orderId);
        return ResponseEntity.ok(myOrderitems);
    }
}
