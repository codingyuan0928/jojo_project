package com.group1.gosports_jojo.customersupport.model;

import java.util.*;

public interface CustomerSupportDAO_interface {
	
	public void insertVendorForCheck(Integer vendorId);
	// -- 傳入廠商資格審核
	// INSERT_VENDOR_STMT

	public void insertFeedback(CustomerSupportVO customerSupportVO);
	// -- 傳入客服表單
	// INSERT_FEEDBACK_STMT

	public CustomerSupportVO findNewFeedback(Integer userId);
	//	-- 回傳客服表單傳送成功內容
	// GET_NEW_FEEDBACK_STMT
	
	public CustomerSupportVO updateFeedback2(CustomerSupportVO customerSupportVO);
	// -- 後臺管理員處理客服表單後(2: 結案_毋須處理)，更新狀態
	// UPDATE_FEEDBACK_STMT
	
	public CustomerSupportVO updateFeedback3(CustomerSupportVO customerSupportVO);
	// -- 後臺管理員處理客服表單後(3: 結案_發送通知信件)，更新狀態
	// UPDATE_FEEDBACK_STMT

	public void updateVendorResult(CustomerSupportVO customerSupportVO);
	// -- 後臺管理員審核廠商資格後，更新狀態
	// UPDATE_VENDOR_STMT

	public List<CustomerSupportVO> findPendingCaseV();
	// 查詢administrator_id = 1廠商審核單，狀態「未處理、待處理」
	// GET_PENDING_V_STMT

	public List<CustomerSupportVO> getAllV();
	// 查詢所有administrator_id = 1廠商審核單 (不分狀態)
	// GET_ALL_V_STMT

	public List<CustomerSupportVO> findPendingCaseC();
	// 查詢administrator_id = 2客服表單，狀態「未處理、待處理」
	// GET_PENDING_C_STMT

	public List<CustomerSupportVO> getAllC();
	// 查詢所有administrator_id = 2客服表單，不分狀態
	// GET_ALL_C_STMT
	
	public List<CustomerSupportVO> getReportedGroupList();
	// 查詢過去30分鐘更新狀態，且須發送通知的「移除檢舉揪團」名單
	//GET_REPORTED_GROUP_LIST
	
	public List<CustomerSupportVO> getReportedArticleList();
	// 查詢過去30分鐘更新狀態，且須發送通知的「移除檢舉文章」名單
	//GET_REPORTED_ARTICLE_LIST
	
	public List<CustomerSupportVO> getReportedReplyList();
	// 查詢過去30分鐘更新狀態，且須發送通知的「移除檢舉留言」名單
	//GET_REPORTED_REPLY_LIST
	
	public List<CustomerSupportVO> getReportedProductList();
	// 查詢過去30分鐘更新狀態，且須發送通知的「商品下架(經檢舉)(seller)」名單
	//GET_REPORTED_PRODUCT_LIST
	
	public List<CustomerSupportVO> getReportedVendorList();
	// 查詢過去30分鐘更新狀態，且須發送通知的「廠商停權(經檢舉)(seller)」名單
	//GET_REPORTED_VENDOR_LIST
	
}
