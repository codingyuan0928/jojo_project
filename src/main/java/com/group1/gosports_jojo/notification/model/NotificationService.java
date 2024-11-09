package com.group1.gosports_jojo.notification.model;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("NotificationService")
public class NotificationService {

	private NotificationDAO_interface dao;

	@Autowired
	public NotificationService(NotificationDAO_interface dao) {
//		dao = new NotificationDAO();
		this.dao = dao;
	}

//////////////////////////////////新增成團通知    //////////////////////////////////

	public void insertGroupSuccessNotification(Integer notifiedUserId, Integer referenceId, String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertGroupSuccessNotification(notificationVO);

	}

//////////////////////////////////新增候補失敗通知    //////////////////////////////////

	public void insertGroupSecondaryListNotification(Integer notifiedUserId, Integer referenceId,
			String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertGroupSecondaryListNotification(notificationVO);

	}

//////////////////////////////////新增流團(取消揪團)通知    //////////////////////////////////

	public void insertGroupCancelNotification(Integer notifiedUserId, Integer referenceId, String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertGroupCancelNotification(notificationVO);

	}

//////////////////////////////////新增活動提醒通知    //////////////////////////////////

	public void insertGroupStartNotification(Integer notifiedUserId, Integer referenceId, String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertGroupStartNotification(notificationVO);

	}

//////////////////////////////////新增提醒團長回覆團員出缺通知    //////////////////////////////////

	public void insertGroupPresentReplyNotification(Integer notifiedUserId, Integer referenceId,
			String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertGroupPresentReplyNotification(notificationVO);

	}

//////////////////////////////////查詢缺席警告通知名單    //////////////////////////////////

	public List<NotificationVO> getGroupAbsenceAlertList() {
		return dao.getGroupAbsenceAlertList();

	}

//////////////////////////////////新增缺席警告通知    //////////////////////////////////

	public void insertGroupAbsenceAlertyNotification(Integer notifiedUserId) {
		dao.insertGroupAbsenceAlertyNotification(notifiedUserId);
	}

//////////////////////////////////新增移除檢舉揪團通知    //////////////////////////////////

	public void insertGroupReportNotification(Integer notifiedUserId, Integer referenceId, String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertGroupReportNotification(notificationVO);
	}

//////////////////////////////////新增移除檢舉文章通知    //////////////////////////////////

	public void insertArticleReportNotification(Integer notifiedUserId, Integer referenceId, String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertArticleReportNotification(notificationVO);
	}

//////////////////////////////////新增移除檢舉留言通知    //////////////////////////////////

	public void insertReplyReportNotification(Integer notifiedUserId, Integer referenceId, String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertReplyReportNotification(notificationVO);
	}

//////////////////////////////////新增好友邀請通知    //////////////////////////////////

	public void insertFriendPendingNotification(Integer notifiedUserId, Integer referenceId, String referenceIdDesc) {
		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertFriendPendingNotification(notificationVO);
	}

//////////////////////////////////新增好友成立通知    //////////////////////////////////

	public void insertFriendAcceptedNotification(Integer notifiedUserId, Integer referenceId, String referenceIdDesc) {
		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertFriendAcceptedNotification(notificationVO);
	}

//////////////////////////////////新增移除檢舉商品通知    //////////////////////////////////

	public void insertProductReportNotification(Integer notifiedVendorId, Integer referenceId, String referenceIdDesc) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedVendorId(notifiedVendorId);
		notificationVO.setReferenceId(referenceId);
		notificationVO.setReferenceIdDesc(referenceIdDesc);

		dao.insertProductReportNotification(notificationVO);
	}

//////////////////////////////////新增訂單成立通知(buyer)    //////////////////////////////////

	public void insertOrderCreatedBuyerNotification(Integer notifiedUserId, Integer referenceId) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);

		dao.insertOrderCreatedBuyerNotification(notificationVO);
	}

//////////////////////////////////新增訂單成立通知(seller)    //////////////////////////////////

	public void insertOrderCreatedSellerNotification(Integer notifiedVendorId, Integer referenceId) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedVendorId(notifiedVendorId);
		notificationVO.setReferenceId(referenceId);

		dao.insertOrderCreatedSellerNotification(notificationVO);
	}

//////////////////////////////////新增訂單完成通知(buyer)    //////////////////////////////////

	public void insertOrderCompletedBuyerNotification(Integer notifiedUserId, Integer referenceId) {

		NotificationVO notificationVO = new NotificationVO();

		notificationVO.setNotifiedUserId(notifiedUserId);
		notificationVO.setReferenceId(referenceId);

		dao.insertOrderCompletedBuyerNotification(notificationVO);
	}

//////////////////////////////////新增廠商停權(經檢舉)(seller)    //////////////////////////////////

	public void insertVendorDeactivateNotification(Integer notifiedVendorId) {
		dao.insertVendorDeactivateNotification(notifiedVendorId);
	}

//////////////////////////////////新增「17.廠商資格審核(seller)-1」通知    //////////////////////////////////

	public void insertVendorCheckNotification1(Integer notifiedVendorId) {
		dao.insertVendorCheckNotification1(notifiedVendorId);
	}

//////////////////////////////////新增「18.廠商資格審核(seller)-2」通知    //////////////////////////////////

	public void insertVendorCheckNotification2(Integer notifiedVendorId) {
		dao.insertVendorCheckNotification2(notifiedVendorId);
	}

///////////////////////////////////////////查詢user id 列出所有通知清單///////////////////////////////////////////////////////////////////////
	public List<NotificationVO> getNotificationByUser(Integer notifiedUserId) {
		return dao.getNotificationByUser(notifiedUserId);
	}

///////////////////////////////////////////查詢vendor id 列出所有通知清單///////////////////////////////////////////////////////////////////////
	public List<NotificationVO> getNotificationByVendor(Integer notifiedVendorId) {
		return dao.getNotificationByVendor(notifiedVendorId);
	}

	
	
	
//用戶/廠商隱藏通知(設定display=N)
	public void hiddenNotification(Integer notifiedUserId) {
		dao.hiddenNotification(notifiedUserId);
	}

// 查詢用戶未讀通知id
	public List<NotificationVO> getUnreadNotificationC(Integer notifiedUserId) {
		return dao.getUnreadNotificationC(notifiedUserId);
	}

//查詢用戶未讀通知數
	public NotificationVO getCountUnreadNotificationC(Integer notifiedUserId) {
		return dao.getCountUnreadNotificationC(notifiedUserId);
	}

//將「用戶未讀的所有通知id」，更新為已讀(設定readed ='Y')
	public void updateNotificationReadedC(Integer notifiedUserId) {
		dao.updateNotificationReadedC(notifiedUserId);
	}

// 查詢廠商未讀通知id
	public List<NotificationVO> getUnreadNotificationV(Integer notifiedVendorId) {
		return dao.getUnreadNotificationV(notifiedVendorId);
	}

//查詢廠商未讀通知數
	public NotificationVO getCountUnreadNotificationV(Integer notifiedVendorId) {
		return dao.getCountUnreadNotificationV(notifiedVendorId);
	}

//將「廠商未讀的所有通知id」，更新為已讀(設定readed ='Y')
	public void updateNotificationReadedV(Integer notifiedVendorId) {
		dao.updateNotificationReadedV(notifiedVendorId);
	}

}
