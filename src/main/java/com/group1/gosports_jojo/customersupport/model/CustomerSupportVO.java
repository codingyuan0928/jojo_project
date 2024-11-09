package com.group1.gosports_jojo.customersupport.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class CustomerSupportVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Integer formId;
	private Integer administratorId;
	private Integer vendorId;
	private Integer userId;
	private String category;
	private String title;
	private String issueDescription;
	private Timestamp submissionDatetime;
	private String status;
	private Integer referenceId;
	private String notificationContent;
	private Timestamp reviewedDatetime;

	//	加入GroupVO
	private Integer groupId;
	private Integer groupLeaderId;
	private String groupName;
	///////////////////////////////////
	
	//	加入PostVO
	private Integer post_id;
	private Integer user_id;
	private String post_title;
	///////////////////////////////////

	//	加入ReplyVO
	private Integer reply_id;

	///////////////////////////////////
	
	
	//	加入ProVO
	private Integer productId;
	private Integer supplierId;
	private String productName;

	///////////////////////////////////

	
	public Integer getPost_id() {
		return post_id;
	}

	public void setPost_id(Integer post_id) {
		this.post_id = post_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getPost_title() {
		return post_title;
	}

	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}

	public Integer getReply_id() {
		return reply_id;
	}

	public void setReply_id(Integer reply_id) {
		this.reply_id = reply_id;
	}

	public CustomerSupportVO() {
		super();
	}

	public Integer getFormId() {
		return formId;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
	}

	public Integer getAdministratorId() {
		return administratorId;
	}

	public void setAdministratorId(Integer administratorId) {
		this.administratorId = administratorId;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIssueDescription() {
		return issueDescription;
	}

	public void setIssueDescription(String issueDescription) {
		this.issueDescription = issueDescription;
	}

	public Timestamp getSubmissionDatetime() {
		return submissionDatetime;
	}

	public void setSubmissionDatetime(Timestamp submissionDatetime) {
		this.submissionDatetime = submissionDatetime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getNotificationContent() {
		return notificationContent;
	}

	public void setNotificationContent(String notificationContent) {
		this.notificationContent = notificationContent;
	}

	public Timestamp getReviewedDatetime() {
		return reviewedDatetime;
	}

	public void setReviewedDatetime(Timestamp reviewedDatetime) {
		this.reviewedDatetime = reviewedDatetime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	//	加入GroupVO
	
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getGroupLeaderId() {
		return groupLeaderId;
	}

	public void setGroupLeaderId(Integer groupLeaderId) {
		this.groupLeaderId = groupLeaderId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/////////////////////////////////
	
//	加入ProVO
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	
}
