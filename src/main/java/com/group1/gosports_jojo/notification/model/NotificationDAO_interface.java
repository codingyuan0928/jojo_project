package com.group1.gosports_jojo.notification.model;

import java.util.List;

public interface NotificationDAO_interface {

	public void insertGroupSuccessNotification(NotificationVO notificationVO);
	// 新增成團通知
	// INSERT_GROUP_SUCCESS_NOTIFICATION

	public void insertGroupCancelNotification(NotificationVO notificationVO);
	// 新增取消揪團通知
	// INSERT_GROUP_CANCEL_NOTIFICATION

	public void insertGroupStartNotification(NotificationVO notificationVO);
	// 新增活動提醒通知
	// INSERT_GROUP_START_NOTIFICATION

	public void insertGroupSecondaryListNotification(NotificationVO notificationVO);
	// 新增候補失敗通知
	// INSERT_GROUP_SECONDARY_LIST_NOTIFICATION

	public void insertGroupPresentReplyNotification(NotificationVO notificationVO);
	// 新增團長回覆團員出缺席通知
	// INSERT_GROUP_PRESENT_REPLY_NOTIFICATION

	public List<NotificationVO> getGroupAbsenceAlertList();
	// 查詢缺席警告名單
	// GET_GROUP_ABSENCE_ALERT_LIST

	public void insertGroupAbsenceAlertyNotification(Integer notifiedUserId);
	// 新增缺席警告通知
	// INSERT_GROUP_ABSENCE_ALERT_NOTIFICATION

	public void insertGroupReportNotification(NotificationVO notificationVO);
	// 新增移除檢舉揪團通知
	// INSERT_GROUP_REPORT_NOTIFICATION

	public void insertArticleReportNotification(NotificationVO notificationVO);
	// 新增移除檢舉文章通知
	// INSERT_ARTICLE_REPORT_NOTIFICATION

	public void insertReplyReportNotification(NotificationVO notificationVO);
	// 新增移除檢舉留言通知
	// INSERT_REPLY_REPORT_NOTIFICATION

	public void insertProductReportNotification(NotificationVO notificationVO);
	// 新增移除檢舉商品通知
	// INSERT_PRODUCT_REPORT_NOTIFICATION

	public void insertFriendPendingNotification(NotificationVO notificationVO);
	// 新增好友邀請通知
	// INSERT_FRIEND_PENDING_NOTIFICATION

	public void insertFriendAcceptedNotification(NotificationVO notificationVO);
	//// 新增好友成立通知
	// INSERT_FRIEND_ACCEPTED_NOTIFICATION

	public void insertOrderCreatedBuyerNotification(NotificationVO notificationVO);
	// 新增訂單成立通知(buyer)
	// INSERT_ORDER_CREATED_BUYER_NOTIFICATION

	public void insertOrderCreatedSellerNotification(NotificationVO notificationVO);
	// 新增訂單成立通知(seller)
	// INSERT_ORDER_CREATED_SELLER_NOTIFICATION

	public void insertOrderCompletedBuyerNotification(NotificationVO notificationVO);
	// 新增訂單完成通知(buyer)
	// INSERT_ORDER_COMPLETED_BUYER_NOTIFICATION

	public void insertVendorDeactivateNotification(Integer notifiedVendorId);
	// 新增廠商停權(經檢舉)(seller)
	// INSERT_VENDOR_DEACTIVATE_NOTIFICATION

	public void insertVendorCheckNotification1(Integer notifiedVendorId);
	// 新增「17.廠商資格審核(seller)-1」通知
	// INSERT_VENDOR_CHECK_NOTIFICATION_1

	public void insertVendorCheckNotification2(Integer notifiedVendorId);
	// 新增「18.廠商資格審核(seller)-2」通知
	// INSERT_VENDOR_CHECK_NOTIFICATION_2

////////////////////用 user id 查詢所有通知/////////////////////////////////////////////////////////
	public List<NotificationVO> getNotificationByUser(Integer notifiedUserId);

////////////////////用 vendor id 查詢所有通知/////////////////////////////////////////////////////////
	public List<NotificationVO> getNotificationByVendor(Integer notifiedVendorId);

	
	
	
	// 用戶/廠商隱藏通知(設定display=N)
	public void hiddenNotification(Integer notificationId);

	//查詢用戶未讀通知id
	public List<NotificationVO> getUnreadNotificationC(Integer notifiedUserId);
	
	// 查詢用戶未讀通知數
	public NotificationVO getCountUnreadNotificationC(Integer notifiedUserId);

	// 將「用戶未讀的所有通知id」，更新為已讀(設定readed ='Y')
	public void updateNotificationReadedC(Integer notifiedUserId);

	
	//查詢廠商未讀通知id
	public List<NotificationVO> getUnreadNotificationV(Integer notifiedVendorId);
	
	// 查詢廠商未讀通知數
	public NotificationVO getCountUnreadNotificationV(Integer notifiedVendorId);

	// 將「廠商未讀的所有通知id」，更新為已讀(設定readed ='Y')
	public void updateNotificationReadedV(Integer notifiedVendorId);

}
