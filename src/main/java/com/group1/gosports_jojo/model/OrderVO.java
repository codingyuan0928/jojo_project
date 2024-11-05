package com.group1.gosports_jojo.model;

import java.sql.Timestamp;

public class OrderVO  implements java.io.Serializable{
	private Integer orderId;
	private Integer vendorId;
	private Integer userId;
	private Integer orderStatus;
	private Integer totalAmount;
	private Timestamp createdDatetime;
	@Override
	public String toString() {
		return "OrderVO [orderId=" + orderId + ", vendorId=" + vendorId + ", userId=" + userId + ", orderStatus="
				+ orderStatus + ", totalAmount=" + totalAmount + ", createdDatetime=" + createdDatetime
				+ ", updatedDatetime=" + updatedDatetime + ", pickupDate=" + pickupDate + "]";
	}
	private Timestamp updatedDatetime; 
	private Timestamp pickupDate;
	
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public Timestamp getCreatedDatetime() {
		return createdDatetime;
	}
	public void setCreatedDatetime(Timestamp createdDatetime) {
		this.createdDatetime = createdDatetime;
	}
	public Timestamp getUpdatedDatetime() {
		return updatedDatetime;
	}
	public void setUpdatedDatetime(Timestamp updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}
	public Timestamp getPickupDate() {
		return pickupDate;
	}
	public void setPickupDate(Timestamp pickupDate) {
		this.pickupDate = pickupDate;
	}
}