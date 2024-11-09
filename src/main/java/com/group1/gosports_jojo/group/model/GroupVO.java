package com.group1.gosports_jojo.group.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component

public class GroupVO  implements java.io.Serializable{
	private Integer groupId;
	private Integer groupLeaderId;
	private String groupName;
	private String groupStatusDesc;
	private String groupType;
	private String groupCity;
	private String groupAddress;
	private Timestamp groupPlayingDatetime;
	private Timestamp groupJoinDeadline;
	private Integer groupPrimaryMember;
	private Integer secondaryMember;
	private byte[] groupPic;
	private String groupNote;
	private Timestamp groupUpdateDatetime;
	private String groupShow;
	private Integer groupModifyCount;

	//////////////////////////////////   用於揪團通知    //////////////////////////////////
	private Integer memberListId;
	private Integer userId;
	private String memberRole;
	private String presentLog;
	private Timestamp updatedDatetime;
	private Integer rankNo;
	private Integer groupPoints;


	
	public GroupVO() {
		super();
	}
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
	public String getGroupStatusDesc() {
		return groupStatusDesc;
	}
	public void setGroupStatusDesc(String groupStatusDesc) {
		this.groupStatusDesc = groupStatusDesc;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getGroupCity() {
		return groupCity;
	}
	public void setGroupCity(String groupCity) {
		this.groupCity = groupCity;
	}
	public String getGroupAddress() {
		return groupAddress;
	}
	public void setGroupAddress(String groupAddress) {
		this.groupAddress = groupAddress;
	}
	public Timestamp getGroupPlayingDatetime() {
		return groupPlayingDatetime;
	}
	public void setGroupPlayingDatetime(Timestamp groupPlayingDatetime) {
		this.groupPlayingDatetime = groupPlayingDatetime;
	}
	public Timestamp getGroupJoinDeadline() {
		return groupJoinDeadline;
	}
	public void setGroupJoinDeadline(Timestamp groupJoinDeadline) {
		this.groupJoinDeadline = groupJoinDeadline;
	}
	public Integer getGroupPrimaryMember() {
		return groupPrimaryMember;
	}
	public void setGroupPrimaryMember(Integer groupPrimaryMember) {
		this.groupPrimaryMember = groupPrimaryMember;
	}
	public Integer getSecondaryMember() {
		return secondaryMember;
	}
	public void setSecondaryMember(Integer secondaryMember) {
		this.secondaryMember = secondaryMember;
	}
	public byte[] getGroupPic() {
		return groupPic;
	}
	public void setGroupPic(byte[] groupPic) {
		this.groupPic = groupPic;
	}

	
	public Timestamp getGroupUpdateDatetime() {
		return groupUpdateDatetime;
	}
	public void setGroupUpdateDatetime(Timestamp groupUpdateDatetime) {
		this.groupUpdateDatetime = groupUpdateDatetime;
	}
	public String getGroupShow() {
		return groupShow;
	}
	public void setGroupShow(String groupShow) {
		this.groupShow = groupShow;
	}
	public Integer getGroupModifyCount() {
		return groupModifyCount;
	}
	public void setGroupModifyCount(Integer groupModifyCount) {
		this.groupModifyCount = groupModifyCount;
	}
	public String getGroupNote() {
		return groupNote;
	}
	public void setGroupNote(String groupNote) {
		this.groupNote = groupNote;
	}


	//////////////////////////////////    揪團通知    //////////////////////////////////
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getMemberListId() {
		return memberListId;
	}
	public void setMemberListId(Integer memberListId) {
		this.memberListId = memberListId;
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

	public Integer getGroupPoints() {
		return groupPoints;
	}
	public void setGroupPoints(Integer groupPoints) {
		this.groupPoints = groupPoints;
	}

//	 for join member_list from memberVO
//    public com.member.model.MemberVO getMemberVO() {
//	    com.member.model.DeptService deptSvc = new com.dept.model.DeptService();
//	    com.dept.model.DeptVO deptVO = deptSvc.getOneDept(deptno);
//	    return deptVO;
//    }

}
	