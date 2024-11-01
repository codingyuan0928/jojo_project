package com.group1.gosports_jojo.model;

import java.sql.Timestamp;

public class AddProductVO  implements java.io.Serializable{
	    private Integer productId;           // 商品編號
	    private Integer vendorId;            // 廠商編號
	    private String productContent;   // 商品描述
	    private String productName;      // 商品名稱
	    private Integer price;               // 商品價格
	    private String productSpec;      // 商品規格
	    private Integer stock;               // 商品庫存
	    private Timestamp createdDatetime; // 上架時間
	    private Timestamp removedDatetime; // 下架時間
	    private Integer productStatus;       // 商品狀態
	    private Timestamp updatedDatetime; // 商品資料更新時間

	    // Getters and Setters
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

	    public String getProductContent() {
	        return productContent;
	    }

	    public void setProductContent(String productContent) {
	        this.productContent = productContent;
	    }

	    public String getProductName() {
	        return productName;
	    }

	    public void setProductName(String productName) {
	        this.productName = productName;
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

	    public int getStock() {
	        return stock;
	    }

	    public void setStock(int stock) {
	        this.stock = stock;
	    }

	    public Timestamp getCreatedDatetime() {
	        return createdDatetime;
	    }

	    public void setCreatedDatetime(Timestamp createdDatetime) {
	        this.createdDatetime = createdDatetime;
	    }

	    public Timestamp getRemovedDatetime() {
	        return removedDatetime;
	    }

	    public void setRemovedDatetime(Timestamp removedDatetime) {
	        this.removedDatetime = removedDatetime;
	    }

	    public Integer getProductStatus() {
	        return productStatus;
	    }

	    public void setProductStatus(Integer productStatus) {
	        this.productStatus = productStatus;
	    }

	    public Timestamp getUpdatedDatetime() {
	        return updatedDatetime;
	    }

	    public void setUpdatedDatetime(Timestamp updatedDatetime) {
	        this.updatedDatetime = updatedDatetime;
	    }
	}

