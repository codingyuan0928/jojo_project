package com.group1.gosports_jojo.vendor.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class VendorVO implements java.io.Serializable {
	private Integer vendorId;
	private String username;
	private String password;
	private byte[] avatar;
	private String email;
	private Short enabled;
	private String companyName;
	private String companyAddress;
	private String companyPhone;
	private String companyEmail;
	private byte[] registrationDocument;
	private String shopName;
	private String unifiedBusinessNumber;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private Short status;

	public VendorVO() {
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

	public Short getEnabled() {
		return enabled;
	}

	public void setEnabled(Short enabled) {
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
	
	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

}
