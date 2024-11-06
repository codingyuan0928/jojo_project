package com.group1.gosports_jojo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "group_lists")
public class GroupList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false)

    private Integer groupId;

    @Size(max = 50)
    @Column(name = "group_name", length = 50)
    private String groupName;

}