package com.group1.gosports_jojo.repository;

import com.group1.gosports_jojo.entity.Orderitem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<Orderitem, Integer> {

    @Query(value ="SELECT o.order_id, o.user_id, o.total_amount, o.created_datetime, o.vendor_id, o.pickup_date, " +
            "oi.product_id, oi.quantity, oi.price, oi.pick_address," +
            "p.product_name, " +
            "v.shop_name, " +
            "u.username, u.email " +
            "FROM `orders` o " +
            "LEFT JOIN `users` u ON o.user_id = u.user_id " +
            "LEFT JOIN `vendors` v ON o.vendor_id = v.vendor_id " +
            "LEFT JOIN `order_items` oi ON o.order_id = oi.order_id " +
            "LEFT JOIN `products` p ON p.product_id = oi.product_id " +
            "WHERE o.order_id = ?", nativeQuery = true)
    List<Object[]> findOrderDetailsByOrderId(@Param("orderId") Integer orderId);

}
