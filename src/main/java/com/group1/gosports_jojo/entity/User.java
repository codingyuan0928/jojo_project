package com.group1.gosports_jojo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "username", length = 30)
    private String username;

    @Column(name = "avatar",columnDefinition = "longblob")
    private byte[] avatar;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Byte enabled;

    @Size(max = 255)
    @Column(name = "provider_name")
    private String providerName;

    @Size(max = 255)
    @Column(name = "access_token")
    private String accessToken;

    @Size(max = 255)
    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "access_token_expiry")
    private Instant accessTokenExpiry;

    @Column(name = "refresh_token_expiry")
    private Instant refreshTokenExpiry;

    @NotNull
    @Column(name = "newsletter_subscription_consent_field", nullable = false)
    private Byte newsletterSubscriptionConsentField;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(name = "group_points", nullable = false)
    private Integer groupPoints;

    @NotNull
    @Column(name = "interests_tag", nullable = false)
    private Integer interestsTag;

}