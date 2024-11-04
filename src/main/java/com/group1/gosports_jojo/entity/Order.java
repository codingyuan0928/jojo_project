package com.group1.gosports_jojo.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "vendor_id", nullable = false)
    private Integer vendorId;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "order_status", nullable = false)
    private Integer orderStatus;

    @Column(name = "created_datetime", nullable = false)
    private Timestamp createdDatetime;

    @Column(name = "updated_datetime", nullable = false)
    private Timestamp updatedDatetime;

    @Column(name = "pickup_date", nullable = false)
    private Timestamp pickupDate;

}
