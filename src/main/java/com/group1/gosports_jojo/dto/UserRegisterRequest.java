package com.group1.gosports_jojo.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.sql.Timestamp;

public class UserRegisterRequest {


    @NotBlank(message = "Email不能空白")
    @Email(message = "Email格式不正確")
    private String email;

    @NotBlank(message = "密碼不能空白")
    private String password;

    @NotBlank(message = "使用者名稱不能空白")
    @Size(max = 255, message = "使用者名稱不能超過85個中文字，請填寫正確中文名稱")
    private String username;

    private MultipartFile avatar; // 用於接收上傳的文件
    private byte[] avatarBytes;    // 用於存儲文件數據

    @Size(max = 255, message = "Provider name cannot exceed 255 characters")
    private String providerName;

    @Size(max = 255, message = "Access token cannot exceed 255 characters")
    private String accessToken;

    @Size(max = 255, message = "Refresh token cannot exceed 255 characters")
    private String refreshToken;

    private Timestamp accessTokenExpiry;
    private Timestamp refreshTokenExpiry;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer newsletterSubscriptionConsentField;

    @NotNull
    @Min(value = 1, message = "請選擇有效的標籤")
    @Max(value = 11)
    private Integer interestsTag;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public byte[] getAvatarBytes() {
        return avatarBytes;
    }

    public void setAvatarBytes(MultipartFile avatar) {
        if (avatar != null && !avatar.isEmpty()) {
            try {
                this.avatarBytes = avatar.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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


    public Integer getInterestsTag() {
        return interestsTag;
    }

    public void setInterestsTag(Integer interestsTag) {
        this.interestsTag = interestsTag;
    }
}
