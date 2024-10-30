package com.group1.gosports_jojo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "member_lists")
public class MemberList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_list_id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    @JsonIgnore  // 防止序列化懶加載對象
    private GroupList groupId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Size(max = 10)
    @NotNull
    @Column(name = "member_role", nullable = false, length = 10)
    private String memberRole;

    @Size(max = 10)
    @NotNull
    @Column(name = "present_log", nullable = false, length = 10)
    private String presentLog;

    @NotNull
    @Column(name = "updated_datetime", nullable = false)
    private Instant updatedDatetime;

}