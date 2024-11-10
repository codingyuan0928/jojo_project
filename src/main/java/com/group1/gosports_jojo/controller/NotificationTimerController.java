package com.group1.gosports_jojo.controller;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.group1.gosports_jojo.friend.model.FriendServiceC;
import com.group1.gosports_jojo.friend.model.FriendVO;
import com.group1.gosports_jojo.model.ProVO;
import com.group1.gosports_jojo.service.impl.PostService;
import com.group1.gosports_jojo.service.impl.ReplyService;
import com.group1.gosports_jojo.service.impl.shop.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group1.gosports_jojo.customersupport.model.*;
import com.group1.gosports_jojo.group.model.*;
import com.group1.gosports_jojo.notification.model.*;
import com.group1.gosports_jojo.vendor.model.*;


@Component
public class NotificationTimerController{
	
	TimeZone timezone = TimeZone.getTimeZone("GMT");
	
	@Autowired
	GroupService groupSvc;
	
	@Autowired
	NotificationService notificationSvc;
	
	@Autowired
	CustomerSupportService customerSupportSvc;
	
	@Autowired
	PostService postSvc;
	
	@Autowired
	ReplyService replySvc;

	@Autowired
	ProService proSvc;
	
	@Autowired
	VendorService vendorSvc;
	
	@Autowired
	FriendServiceC friendSvc;
	
	Date dateExecute = new Date();

//////////////////////////////////    成團通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertGroupSuccessNotification() {
				
System.out.println(dateExecute+" 執行insertGroupSuccessNotification");
				
				List<GroupVO> listGroupSuccess = groupSvc.getGroupSuccessMemberlist();


				for (int i = 0; i < listGroupSuccess.size(); i++) {

					Integer userId = listGroupSuccess.get(i).getUserId();
					Integer groupId = listGroupSuccess.get(i).getGroupId();
					String groupName = listGroupSuccess.get(i).getGroupName();

					System.out.println("taskGroupSuccess: " + userId + "--" + groupId + "--" + groupName);

					notificationSvc.insertGroupSuccessNotification(userId, groupId, groupName);
				}
				;
			}

//////////////////////////////////    流團通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertGroupCancelNotification() {
System.out.println(dateExecute+" 執行insertGroupCancelNotification");

				List<GroupVO> listGroupCancel = groupSvc.getGroupCancelMemberlist();

				for (int i = 0; i < listGroupCancel.size(); i++) {

					Integer userId = listGroupCancel.get(i).getUserId();
					Integer groupId = listGroupCancel.get(i).getGroupId();
					String groupName = listGroupCancel.get(i).getGroupName();

					System.out.println("taskGroupCancel: " + userId + "--" + groupId + "--" + groupName);

					notificationSvc.insertGroupCancelNotification(userId, groupId, groupName);
				}
				;
			}

//////////////////////////////////    活動提醒通知    //////////////////////////////////
			
			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertGroupStartNotification() {
System.out.println(dateExecute+" 執行insertGroupStartNotification");

				List<GroupVO> listGroupStart = groupSvc.getGroupStartMemberlist();

				for (int i = 0; i < listGroupStart.size(); i++) {

					Integer userId = listGroupStart.get(i).getUserId();
					Integer groupId = listGroupStart.get(i).getGroupId();
					String groupName = listGroupStart.get(i).getGroupName();

					System.out.println("listGroupStart: " + userId + "--" + groupId + "--" + groupName);

					notificationSvc.insertGroupStartNotification(userId, groupId, groupName);

				}
				;
			}

//////////////////////////////////    候補失敗通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertGroupSecondaryListNotification() {
System.out.println(dateExecute+" 執行insertGroupSecondaryListNotification");

				List<GroupVO> listGroupSecondary = groupSvc.getGroupSecondaryList();

				for (int i = 0; i < listGroupSecondary.size(); i++) {

					Integer userId = listGroupSecondary.get(i).getUserId();
					Integer groupId = listGroupSecondary.get(i).getGroupId();
					String groupName = listGroupSecondary.get(i).getGroupName();

					System.out.println("listGroupSecondaryList: " + userId + "--" + groupId + "--" + groupName);

					notificationSvc.insertGroupSecondaryListNotification(userId, groupId, groupName);

				}
				;
			}

//////////////////////////////////    團長回覆團員出缺席通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertGroupPresentReplyNotification() {
System.out.println(dateExecute+" 執行insertGroupPresentReplyNotification");

				List<GroupVO> listGroupPresentReplyLeader = groupSvc.getGroupPresentReplyLeader();

				for (int i = 0; i < listGroupPresentReplyLeader.size(); i++) {

					Integer userId = listGroupPresentReplyLeader.get(i).getUserId();
					Integer groupId = listGroupPresentReplyLeader.get(i).getGroupId();
					String groupName = listGroupPresentReplyLeader.get(i).getGroupName();

					System.out.println("listGroupPresentReplyLeader: " + userId + "--" + groupId + "--" + groupName);

					notificationSvc.insertGroupPresentReplyNotification(userId, groupId, groupName);

				}
				;
			}

//////////////////////////////////    新增缺席警告通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertGroupAbsenceAlertyNotification() {
System.out.println(dateExecute+" 執行insertGroupAbsenceAlertyNotification");

				List<NotificationVO> listGroupAbsenceAlert = notificationSvc.getGroupAbsenceAlertList();

				for (int i = 0; i < listGroupAbsenceAlert.size(); i++) {

					Integer userId = listGroupAbsenceAlert.get(i).getUserId();

					System.out.println("listGroupAbsenceAlert: " + userId);

					notificationSvc.insertGroupAbsenceAlertyNotification(userId);

				}
				;
			}
			
//////////////////////////////////    新增移除檢舉揪團通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertGroupReportNotification() {
System.out.println(dateExecute+" 執行insertGroupReportNotification");

				List<CustomerSupportVO> listGroupReport = customerSupportSvc.getReportedGroupList();

				for (int i = 0; i < listGroupReport.size(); i++) {

					Integer groupLeaderId = listGroupReport.get(i).getGroupLeaderId();
					Integer groupId = listGroupReport.get(i).getReferenceId();
					String groupName = listGroupReport.get(i).getGroupName();

					System.out.println("listGroupReport: " + groupLeaderId + "--" + groupId + "--" + groupName);

					System.out.println("Group updateHidden: " + groupId);
					groupSvc.updateHidden(groupId);

					notificationSvc.insertGroupReportNotification(groupLeaderId, groupId, groupName);
				}
				;
			}

//////////////////////////////////    新增移除檢舉文章通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertArticleReportNotification() {
System.out.println(dateExecute+" 執行insertArticleReportNotification");

				List<CustomerSupportVO> listArticleReport = customerSupportSvc.getReportedArticleList();

				for (int i = 0; i < listArticleReport.size(); i++) {

					Integer postUserId = listArticleReport.get(i).getUser_id();
					Integer postId = listArticleReport.get(i).getReferenceId();
					String postTitle = listArticleReport.get(i).getPost_title();

					System.out.println("listArticleReport: " + postUserId + "--" + postId + "--" + postTitle);

					System.out.println("PostUpdateHidden: " + postId);
					postSvc.updateHidden(postId);

					notificationSvc.insertArticleReportNotification(postUserId, postId, postTitle);
				}
				;
			}

//////////////////////////////////    新增移除檢舉留言通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertReplyReportNotification() {
System.out.println(dateExecute+" 執行insertReplyReportNotification");

				List<CustomerSupportVO> listReplyReport = customerSupportSvc.getReportedReplyList();

				for (int i = 0; i < listReplyReport.size(); i++) {

					Integer replyUserId = listReplyReport.get(i).getUserId();
					Integer replyId = listReplyReport.get(i).getReferenceId();
					String postTitle = listReplyReport.get(i).getPost_title();

					System.out.println("listReplyReport: " + replyUserId + "--" + replyId + "--" + postTitle);

					System.out.println("ReplyUpdateHidden: " + replyId);
					replySvc.updateHidden(replyId);

					notificationSvc.insertReplyReportNotification(replyUserId, replyId, postTitle);
				}
				;
			}

//////////////////////////////////    新增移除檢舉商品通知    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertProductReportNotification() {
System.out.println(dateExecute+" 執行insertProductReportNotification");

				List<CustomerSupportVO> listProductReport = customerSupportSvc.getReportedProductList();

				for (int i = 0; i < listProductReport.size(); i++) {

					Integer productVendorId = listProductReport.get(i).getVendorId();
					Integer productId = listProductReport.get(i).getReferenceId();
					String productName = listProductReport.get(i).getProductName();

					System.out.println("listProductReport: " + productVendorId + "--" + productId + "--" + productName);

					System.out.println("ProductUpdateHidden: " + productId);
					proSvc.updateHidden(productId);
					notificationSvc.insertProductReportNotification(productVendorId, productId, productName);

				}
				;
			}

//////////////////////////////////    新增訂單成立通知(buyer & seller)    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertOrderCreatedNotification() {
System.out.println(dateExecute+" 執行insertOrderCreatedNotification");

				List<ProVO> listOrderCreatedList = proSvc.getOrderCreatedList();

				for (int i = 0; i < listOrderCreatedList.size(); i++) {

					Integer orderUserId = listOrderCreatedList.get(i).getUserId();
					Integer ordertId = listOrderCreatedList.get(i).getOrderId();
					Integer ordertVendorId = listOrderCreatedList.get(i).getVendorId();

					// 新增訂單成立通知(buyer)
					System.out.println("listOrderCreatedList_buyer: " + orderUserId + "--" + ordertId);
					notificationSvc.insertOrderCreatedBuyerNotification(orderUserId, ordertId);

					// 新增訂單成立通知(seller)
					System.out.println("listOrderCreatedList_Seller: " + ordertVendorId + "--" + ordertId);
					notificationSvc.insertOrderCreatedSellerNotification(ordertVendorId, ordertId);
				}
				;
			}

//////////////////////////////////    新增訂單完成通知(buyer)    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertOrderCompletedBuyerNotification() {
System.out.println(dateExecute+" 執行insertOrderCompletedBuyerNotification");

				List<ProVO> listOrderCompleted = proSvc.getOrderCompletedList();

				for (int i = 0; i < listOrderCompleted.size(); i++) {

					Integer orderUserId = listOrderCompleted.get(i).getUserId();
					Integer ordertId = listOrderCompleted.get(i).getOrderId();

					System.out.println("listOrderCompleted: " + orderUserId + "--" + ordertId);
					notificationSvc.insertOrderCompletedBuyerNotification(orderUserId, ordertId);
				}
				;
			}

//////////////////////////////////    新增廠商停權(經檢舉)(seller)    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertVendorDeactivateNotification() {
System.out.println(dateExecute+" 執行insertVendorDeactivateNotification");

				List<CustomerSupportVO> listVendorDeactivate = customerSupportSvc.getReportedVendorList();

				for (int i = 0; i < listVendorDeactivate.size(); i++) {

					Integer VendorId = listVendorDeactivate.get(i).getReferenceId();
					
					//更新廠商enabled狀態為0
					System.out.println("VendorUpdateHidden: " + VendorId);
					vendorSvc.updateEnabled0(VendorId);

					//新增廠商停權通知
					System.out.println("listVendorDeactivate: " + VendorId);
					notificationSvc.insertVendorDeactivateNotification(VendorId);
				}
				;
			}

//////////////////////////////////    新增「廠商資格審核(seller)-1通知」    //////////////////////////////////

			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertVendorCheckNotification1() {
System.out.println(dateExecute+" 執行insertVendorCheckNotification1");

				List<VendorVO> listVendorForCheck = vendorSvc.getNewVendor();

				for (int i = 0; i < listVendorForCheck.size(); i++) {

					Integer VendorId = listVendorForCheck.get(i).getVendorId();
					
					//新增「廠商資格審核(seller)-1通知」
					System.out.println("listVendorForCheck-NotificationList: " + VendorId);
					notificationSvc.insertVendorCheckNotification1(VendorId);
				}
				;
			}
		
//////////////////////////////////    新增「廠商資格審核(seller)-2通知」，並設置停權(enabled=0)    //////////////////////////////////
		
			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertVendorCheckNotification2() {
System.out.println(dateExecute+" 執行insertVendorCheckNotification2");
				
				List<VendorVO> listVendorForCheck2 = vendorSvc.getNotPassVendor();
				
				for (int i = 0; i < listVendorForCheck2.size(); i++) {
					
					Integer VendorId = listVendorForCheck2.get(i).getVendorId();
					
					//新增「廠商資格審核(seller)-2通知」
					System.out.println("listVendorForCheck2: " + VendorId);
					notificationSvc.insertVendorCheckNotification2(VendorId);
					
					//將3天前，狀態更新為2的廠商，設定廠商停權(使enabled=0)
					vendorSvc.updateEnabled0(VendorId);
					System.out.println("updateEnabled0: " + VendorId);
				}
				;
			}
	
			
//////////////////////////////////    新增「好友邀請通知」    //////////////////////////////////
		
			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertFriendPendingNotification() {
System.out.println(dateExecute+" 執行insertFriendPendingNotification");
				
				List<FriendVO> listFriendPending = friendSvc.getFriendPending();
				
				for (int i = 0; i < listFriendPending.size(); i++) {
					
					Integer UserId = listFriendPending.get(i).getUserId();
					Integer FriendId = listFriendPending.get(i).getFriendId();
					String UserName = listFriendPending.get(i).getUsernaame();
					
					System.out.println("listFriendPending: 發起邀請用戶-" + UserId + "；被請求成為好友用戶-" + FriendId + "-發起邀請用戶-" + UserName);
					notificationSvc.insertFriendPendingNotification(FriendId, UserId, UserName);
				}
				;
			}
		
//////////////////////////////////    新增「好友成立通知」    //////////////////////////////////
		
			@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
			public void insertFriendAcceptedNotification() {
System.out.println(dateExecute+" 執行insertFriendAcceptedNotification");
				
				List<FriendVO> listFriendAccepted = friendSvc.getFriendAccepted();
				
				for (int i = 0; i < listFriendAccepted.size(); i++) {
					
					Integer UserId = listFriendAccepted.get(i).getUserId();
					Integer FriendId = listFriendAccepted.get(i).getFriendId();
					String UserName = listFriendAccepted.get(i).getUsernaame();
					
					System.out.println("listFriendAccepted: 發起邀請用戶-" + UserId + "；被請求成為好友用戶-" + FriendId + "-發起邀請用戶-" + UserName);
					notificationSvc.insertFriendAcceptedNotification(UserId, FriendId, UserName);
				}
				;
			}
		
		////////////////////////////////////////////////////////////////////////////////////


}
