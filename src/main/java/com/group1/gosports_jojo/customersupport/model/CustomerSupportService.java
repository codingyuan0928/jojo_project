package com.group1.gosports_jojo.customersupport.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.group1.gosports_jojo.notification.model.NotificationDAO;


@Service("CustomerSupportService")
public class CustomerSupportService {

	private CustomerSupportDAO_interface dao;
	

	@Autowired
	public CustomerSupportService(CustomerSupportDAO_interface dao) {
//		dao = new CustomerSupportDAO();
		this.dao = dao;
	}

	public void insertVendorForCheck(Integer vendorId) {
		dao.insertVendorForCheck(vendorId);
	}

	public CustomerSupportVO addFeedbackForm(Integer userId, String category, String title, String issueDescription) {

		CustomerSupportVO customerSupportVO = new CustomerSupportVO();

		customerSupportVO.setUserId(userId);
		customerSupportVO.setCategory(category);
		customerSupportVO.setTitle(title);
		customerSupportVO.setIssueDescription(issueDescription);
		dao.insertFeedback(customerSupportVO);

		return customerSupportVO;
	}

	public CustomerSupportVO findNewFeedback(Integer userId) {
		return dao.findNewFeedback(userId);
	}

	public CustomerSupportVO updateFeedback2(String status, String notificationContent, Integer formId) {
		
		CustomerSupportVO customerSupportVO = new CustomerSupportVO();

		customerSupportVO.setStatus(status);
		customerSupportVO.setNotificationContent(notificationContent);
		customerSupportVO.setFormId(formId);
		return dao.updateFeedback2(customerSupportVO);
	}
	
	public CustomerSupportVO updateFeedback3(String status, Integer referenceId, String notificationContent, Integer formId) {
		
		CustomerSupportVO customerSupportVO = new CustomerSupportVO();

		customerSupportVO.setStatus(status);
		customerSupportVO.setReferenceId(referenceId);
		customerSupportVO.setNotificationContent(notificationContent);
		customerSupportVO.setFormId(formId);
		return dao.updateFeedback3(customerSupportVO);
	}
	
	public List<CustomerSupportVO> findPendingCaseV() {
		return dao.findPendingCaseV();
	}

	public List<CustomerSupportVO> getAllV() {
		return dao.getAllV();
	}

	public List<CustomerSupportVO> findPendingCaseC() {
		return dao.findPendingCaseC();
	}

	public List<CustomerSupportVO> getAllC() {
		return dao.getAllC();
	}
	
	
	public List<CustomerSupportVO> getReportedGroupList() {
		return dao.getReportedGroupList();
	}
	
	
	public List<CustomerSupportVO> getReportedArticleList() {
		return dao.getReportedArticleList();
	}
	
	
	public List<CustomerSupportVO> getReportedReplyList(){
		return dao.getReportedReplyList();
	}
	
	public List<CustomerSupportVO> getReportedProductList(){
		return dao.getReportedProductList();
	}
	
	public List<CustomerSupportVO> getReportedVendorList(){
		return dao.getReportedVendorList();
	}
	

}
