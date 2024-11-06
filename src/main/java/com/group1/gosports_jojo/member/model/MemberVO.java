package com.group1.gosports_jojo.member.model;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.group1.gosports_jojo.city.model.CityService;
import com.group1.gosports_jojo.group.model.GroupService;
import com.group1.gosports_jojo.service.UserService;

@Component
public class MemberVO  implements java.io.Serializable{
	private Integer memberListId;
	private Integer groupId;
	private Integer userId;
	private String memberRole;
	private String presentLog;
	private Timestamp updatedDatetime;
	
	////////////////////For group_history 頁面使用///////////////////////////
	private String groupName;
	private String groupStatusDesc;
	private String groupType;
	private String groupCity;
	private String groupAddress;
	private Timestamp groupPlayingDatetime;
	private Integer groupPrimaryMember;
	private String groupShow;
	//////////////////////////////////////////////////////////////////////
	
	private Integer rankNo; // for 正取備取篩選新增使用
	private Integer count;  // for present log 紀錄 yes no 的數量
	private String username; // for group_join 確認個人username
	
	
	@Autowired
	UserService userSv;
	@Autowired
	GroupService groupSv;
	

	public MemberVO() {
		super();
	}
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
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
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
	public Integer getGroupPrimaryMember() {
		return groupPrimaryMember;
	}
	public void setGroupPrimaryMember(Integer groupPrimaryMember) {
		this.groupPrimaryMember = groupPrimaryMember;
	}
	public String getGroupShow() {
		return groupShow;
	}
	public void setGroupShow(String groupShow) {
		this.groupShow = groupShow;
	}
	
	
	public com.group1.gosports_jojo.model.UserVO getUserVO() {
//		    com.user.model.UserService userSvc = new com.user.model.UserService();
		com.group1.gosports_jojo.model.UserVO userVO = userSv.getOneUser(userId);
		    return userVO;
	    }
	  
	  public com.group1.gosports_jojo.group.model.GroupVO getGroupVO() {
//		    com.group.model.GroupService groupSvc = new com.group.model.GroupService();
		  com.group1.gosports_jojo.group.model.GroupVO groupVO = groupSv.findByPrimaryKey(groupId);
		    return groupVO;
	    }
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	
}