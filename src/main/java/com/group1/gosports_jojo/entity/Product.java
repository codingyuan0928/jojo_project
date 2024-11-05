package com.group1.gosports_jojo.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;



@Entity
@Data
@Table(name = "products", schema = "go_sports")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Size(max = 255)
    @Column(name = "product_spec")
    private String productSpec;

    @Size(max = 255)
    @Column(name = "product_content")
    private String productContent;

    @Column(name = "price")
    private Integer price;

    @Column(name = "stock")
    private Integer stock;

    @Size(max = 10)
    @Column(name = "product_name", length = 10)
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;


    @Column(name = "product_status")
    private Integer productStatus;


    @Column(name = "ads")
    private Integer ads;

    @Column(name = "created_datetime")
    private Instant createdDatetime;

    @Column(name = "product_updated_datetime")
    private Instant productUpdatedDatetime;

    @Column(name = "removed_datetime")
    private Instant removedDatetime;

}