package com.group1.gosports_jojo.notificationitem.model;

import org.springframework.stereotype.Component;

@Component
public class NotificationItemListVO implements java.io.Serializable{
	private Integer notificationId;
	private String notificationItem;
	
	
	public Integer getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}
	public String getNotificationItem() {
		return notificationItem;
	}
	public void setNotificationItem(String notificationItem) {
		this.notificationItem = notificationItem;
	}

	
}
