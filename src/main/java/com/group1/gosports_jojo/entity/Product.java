package com.group1.gosports_jojo.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Data
@Table(name = "products", schema = "go_sports")
public class Product {
    @Id
    @Column(name = "product_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @Column(name = "product_name", length = 100)
    private String productName;

    @Column(name = "product_content")
    private String productContent;

    @Column(name = "price")
    private Integer price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "created_datetime")
    private Instant createdDatetime;


    @Column(name = "removed_datetime")
    private Instant removedDatetime;

    @Column(name = "product_status")
    private Integer productStatus;

    @Column(name = "product_updated_datetime")
    private Instant productUpdatedDatetime;

    @Column(name = "product_spec", length = 10)
    private String productSpec;

    @Column(name = "ads")
    private Integer ads;

}