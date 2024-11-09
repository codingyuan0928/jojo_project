package com.group1.gosports_jojo.notification.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class NotificationVO implements java.io.Serializable {
	private Integer notificationId;
	private Integer notifiedUserId;
	private Integer notifiedVendorId;
	private Integer referenceId;
	private String referenceIdDesc;
	private String notificationCategory;
	private String notificationItem;
	private String display;
	private String readed;
	private Timestamp sendDatetime;
	private String notificationStatus;

//////////////////////////////////    用於缺席警告通知    //////////////////////////////

	private Integer userId;
	private String memberRole;
	private String presentLog;
	private Timestamp updatedDatetime;
	private Integer rankNo; // for 正取備取篩選新增使用

//////////////////////////////////////////////////////////////////////////////////////
	private Integer count; // 計算未讀通知數

	public NotificationVO() {
		super();
	}

	public Integer getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Integer notificationId) {
		this.notificationId = notificationId;
	}

	public Integer getNotifiedUserId() {
		return notifiedUserId;
	}

	public void setNotifiedUserId(Integer notifiedUserId) {
		this.notifiedUserId = notifiedUserId;
	}

	public Integer getNotifiedVendorId() {
		return notifiedVendorId;
	}

	public void setNotifiedVendorId(Integer notifiedVendorId) {
		this.notifiedVendorId = notifiedVendorId;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceIdDesc() {
		return referenceIdDesc;
	}

	public void setReferenceIdDesc(String referenceIdDesc) {
		this.referenceIdDesc = referenceIdDesc;
	}

	public String getNotificationCategory() {
		return notificationCategory;
	}

	public void setNotificationCategory(String notificationCategory) {
		this.notificationCategory = notificationCategory;
	}

	public String getNotificationItem() {
		return notificationItem;
	}

	public void setNotificationItem(String notificationItem) {
		this.notificationItem = notificationItem;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getReaded() {
		return readed;
	}

	public void setReaded(String readed) {
		this.readed = readed;
	}

	public Timestamp getSendDatetime() {
		return sendDatetime;
	}

	public void setSendDatetime(Timestamp sendDatetime) {
		this.sendDatetime = sendDatetime;
	}

	public String getNotificationStatus() {
		return notificationStatus;
	}

	public void setNotificationStatus(String notificationStatus) {
		this.notificationStatus = notificationStatus;
	}

//////////////////////////////////    用於缺席警告通知    //////////////////////////////

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMemberRole() {
		return memberRole;
	}

	public void setMemberRole(String memberRole) {
		this.memberRole = memberRole;
	}

	public String getPresentLog() {
		return presentLog;
	}

	public void setPresentLog(String presentLog) {
		this.presentLog = presentLog;
	}

	public Timestamp getUpdatedDatetime() {
		return updatedDatetime;
	}

	public void setUpdatedDatetime(Timestamp updatedDatetime) {
		this.updatedDatetime = updatedDatetime;
	}

	public Integer getRankNo() {
		return rankNo;
	}

	public void setRankNo(Integer rankNo) {
		this.rankNo = rankNo;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

//////////////////////////////////////////////////////////////////////////////////////

//	 for join member_list from memberVO
//    public com.member.model.MemberVO getMemberVO() {
//	    com.member.model.DeptService deptSvc = new com.dept.model.DeptService();
//	    com.dept.model.DeptVO deptVO = deptSvc.getOneDept(deptno);
//	    return deptVO;
//    }

}
