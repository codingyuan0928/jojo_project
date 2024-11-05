package com.group1.gosports_jojo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "orders")

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @NotNull
    @Column(name = "order_status", nullable = false)
    private Integer orderStatus;

    @NotNull
    @Column(name = "created_datetime", nullable = false)
    private Timestamp createdDatetime;

    @NotNull
    @Column(name = "updated_datetime", nullable = false)
    private Timestamp updatedDatetime;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vendor_id", nullable = false)
    private Vendor vendor;

    @Column(name = "pickup_date", insertable = false, updatable = false)
    private Timestamp pickupDate;

}