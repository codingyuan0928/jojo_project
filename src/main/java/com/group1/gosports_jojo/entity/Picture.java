package com.group1.gosports_jojo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "pictures")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_id", nullable = false)
    private Integer id;

    @Column(name = "pic_created_datetime")
    private Instant picCreatedDatetime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

}