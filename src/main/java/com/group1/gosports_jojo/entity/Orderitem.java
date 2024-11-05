package com.group1.gosports_jojo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Entity
@Table(name = "order_items")
public class Orderitem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private Integer orderId;

    @NotNull
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "pick_address", nullable = false)
    private String pickAddress;

}