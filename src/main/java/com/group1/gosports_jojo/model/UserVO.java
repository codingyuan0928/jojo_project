package com.group1.gosports_jojo.model;


import java.sql.Timestamp;

public class UserVO {
    private Integer userId;
    private String username;
    private String password;
    private byte[] avatarBytes;
    private String email;
    private Integer enabled;
    private String providerName;
    private String accessToken;
    private String refreshToken;
    private Timestamp accessTokenExpiry;
    private Timestamp refreshTokenExpiry;
    private Integer newsletterSubscriptionConsentField;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer groupPoints;
    private Integer interestsTag;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatarBytes() {
        return avatarBytes;
    }

    public void setAvatarBytes(byte[] avatar) {
        this.avatarBytes = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Timestamp getAccessTokenExpiry() {
        return accessTokenExpiry;
    }

    public void setAccessTokenExpiry(Timestamp accessTokenExpiry) {
        this.accessTokenExpiry = accessTokenExpiry;
    }

    public Timestamp getRefreshTokenExpiry() {
        return refreshTokenExpiry;
    }

    public void setRefreshTokenExpiry(Timestamp refreshTokenExpiry) {
        this.refreshTokenExpiry = refreshTokenExpiry;
    }

    public Integer getNewsletterSubscriptionConsentField() {
        return newsletterSubscriptionConsentField;
    }

    public void setNewsletterSubscriptionConsentField(Integer newsletterSubscriptionConsentField) {
        this.newsletterSubscriptionConsentField = newsletterSubscriptionConsentField;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getGroupPoints() {
        return groupPoints;
    }

    public void setGroupPoints(Integer groupPoints) {
        this.groupPoints = groupPoints;
    }

    public Integer getInterestsTag() {
        return interestsTag;
    }

    public void setInterestsTag(Integer interestsTag) {
        this.interestsTag = interestsTag;
    }
}
