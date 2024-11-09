package com.group1.gosports_jojo.model;

import java.sql.Timestamp;


public class ProVO implements java.io.Serializable{
	private Integer productId;
	private Integer vendorId;
	private String productName;
	private String productContent;
	private Integer price;
	private String productSpec;
	private Integer stock;
	private Integer productStatus;
	private Timestamp created_datetime;
	private Timestamp removed_datetime;
	private Timestamp product_updated_datetime;
	private Integer ads;

	//////////jimmy table//////////////
	private byte[] picture;

	// 查證檢舉商品
	private String username;

	// 查詢訂單
	private Integer orderId;
	private Integer userId;



	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductContent() {
		return productContent;
	}

	public void setProductContent(String productContent) {
		this.productContent = productContent;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getProductSpec() {
		return productSpec;
	}

	public void setProductSpec(String productSpec) {
		this.productSpec = productSpec;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getProductStatus() {
		return productStatus;
	}

	public void setProductStatus(Integer productStatus) {
		this.productStatus = productStatus;
	}

	public Timestamp getCreated_datetime() {
		return created_datetime;
	}

	public void setCreated_datetime(Timestamp created_datetime) {
		this.created_datetime = created_datetime;
	}

	public Timestamp getRemoved_datetime() {
		return removed_datetime;
	}

	public void setRemoved_datetime(Timestamp removed_datetime) {
		this.removed_datetime = removed_datetime;
	}

	public Timestamp getProduct_updated_datetime() {
		return product_updated_datetime;
	}

	public void setProduct_updated_datetime(Timestamp product_updated_datetime) {
		this.product_updated_datetime = product_updated_datetime;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public Integer getAds() {
		return ads;
	}

	public void setAds(Integer ads) {
		this.ads = ads;
	}

	@Override
	public String toString() {
		return "ProVO [productId=" + productId +
				", vendorId=" + vendorId +
				", productName=" + productName +
				", productContent=" + productContent +
				", price=" + price +
				", productSpec=" + productSpec+
				", stock=" + stock +
				", productStatus=" + productStatus +
				", created_datetime=" + created_datetime +
				", removed_datetime=" + removed_datetime +
				", product_updated_datetime=" + product_updated_datetime +
				"]";
	}

	// 查證檢舉商品
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	//查詢訂單成立
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}



}
