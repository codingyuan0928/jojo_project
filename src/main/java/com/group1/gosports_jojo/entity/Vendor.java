package com.group1.gosports_jojo.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="vendors")
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vendor_id")
    private Integer vendorId;

    @Column(name="username",nullable = false)
    private String username;
    @Column(name="password",nullable = false)
    private String password;
    @Column(name="avatar",columnDefinition ="longblob")
    private byte[] avatar;
    @Column(name="email",nullable = false, unique = true)
    private String email;
    @Column(name="enabled",columnDefinition = "TINYINT DEFAULT 1 COMMENT '0=刪除, 1=啟用'")
    private Integer enabled=1;
    @Column(name="company_name",unique = true)
    private String companyName;
    @Column(name="company_address")
    private String companyAddress;
    @Column(name = "company_phone",unique = true)
    private String companyPhone;
    @Column(name="company_email",unique = true)
    private String companyEmail;
    @Column(name = "registration_document",columnDefinition = "longblob COMMENT '營業登記相關文件'")
    private byte[] registrationDocument;
    @Column(name = "shop_name",nullable = false, unique = true)
    private String shopName;
    @Column(name="unified_business_number")
    private String unifiedBusinessNumber;git 
    @CreationTimestamp
    @Column(name="created_at",updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    @Column(name="updated_at")
    private Timestamp updatedAt;
    @Column(name="status",columnDefinition = "TINYINT DEFAULT 0 COMMENT '0=未審核, 1=審核通過, 2=審核沒通過'")
    private Integer status=0;

    public Vendor(){
        super();
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
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

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public byte[] getRegistrationDocument() {
        return registrationDocument;
    }

    public void setRegistrationDocument(byte[] registrationDocument) {
        this.registrationDocument = registrationDocument;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUnifiedBusinessNumber() {
        return unifiedBusinessNumber;
    }

    public void setUnifiedBusinessNumber(String unifiedBusinessNumber) {
        this.unifiedBusinessNumber = unifiedBusinessNumber;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
