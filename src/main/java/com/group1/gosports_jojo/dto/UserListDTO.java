package com.group1.gosports_jojo.dto;

import java.sql.Timestamp;

public class UserListDTO {
    private Integer userId;
    private String username;
    private String avatarBase64;
    private String email;
    private Integer enabled;
    private String providerName;
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

    public String getAvatarBase64() {
        return avatarBase64;
    }

    public void setAvatarBase64(String avatarBase64) {
        this.avatarBase64 = avatarBase64;
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
