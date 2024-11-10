package com.group1.gosports_jojo.group.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class GroupService {
	

	
	

	private GroupDAO_interface dao;
	
	@Autowired
	public GroupService(GroupDAO_interface dao) {
//		dao = new EmpJDBCDAO();
		this.dao=dao;
		
	}

	//servlet回傳以下引數為了 insert, 使用dao的insert(groupVO)方法把球隊新增,並且回傳GroupVO物件到servlet
	public GroupVO insert(Integer groupLeaderId, String groupName,String groupType,String groupCity,
			String groupAddress, java.sql.Timestamp groupPlayingDatetime, java.sql.Timestamp groupJoinDeadline, Integer groupPrimaryMember,
			Integer secondaryMember, byte[] groupPic, String groupNote) {

		GroupVO groupVO = new GroupVO();

		groupVO.setGroupLeaderId(groupLeaderId);
		groupVO.setGroupName(groupName);
		groupVO.setGroupType(groupType);
		groupVO.setGroupCity(groupCity);
		groupVO.setGroupAddress(groupAddress);
		groupVO.setGroupPlayingDatetime(groupPlayingDatetime);
		groupVO.setGroupJoinDeadline(groupJoinDeadline);
		groupVO.setGroupPrimaryMember(groupPrimaryMember);
		groupVO.setSecondaryMember(secondaryMember);
		groupVO.setGroupPic(groupPic);
		groupVO.setGroupNote(groupNote);
		
		
		dao.insert(groupVO);

		return groupVO;
	}


	//servlet回傳以下引數為了update , 使用dao的updateAll方法把球隊內容進行改變,
    //並且使用dao.findByPrimaryKey(groupId)回傳GroupVO物件到servlet
	public GroupVO updateAll(Integer groupId,String groupName,String groupAddress, java.sql.Timestamp groupPlayingDatetime, 
			java.sql.Timestamp groupJoinDeadline, Integer groupPrimaryMember,Integer secondaryMember, byte[] groupPic, 
			String groupNote) {

		GroupVO groupVO = new GroupVO();

		groupVO.setGroupId(groupId);
		groupVO.setGroupName(groupName);
		groupVO.setGroupAddress(groupAddress);
		groupVO.setGroupPlayingDatetime(groupPlayingDatetime);
		groupVO.setGroupJoinDeadline(groupJoinDeadline);
		groupVO.setGroupPrimaryMember(groupPrimaryMember);
		groupVO.setSecondaryMember(secondaryMember);
		groupVO.setGroupPic(groupPic);
		groupVO.setGroupNote(groupNote);
		dao.updateAll(groupVO);

		return dao.findByPrimaryKey(groupId);
	}
	

	//servlet回傳groupId , 使用dao的updateHidden方法把球隊隱藏,並且使用dao.getAll回傳所有list到servlet
	public List<GroupVO> updateHidden(Integer groupId) {

		GroupVO groupVO = new GroupVO();

		groupVO.setGroupId(groupId);
		
		dao.updateHidden(groupId);

		return dao.getAll();
	}


	public GroupVO findByPrimaryKey(Integer groupId) {
		return dao.findByPrimaryKey(groupId);
	}
	
	public GroupVO findByGroupLeadId(Integer groupLeaderId) {
		return dao.findByGroupLeadId(groupLeaderId);
	}

	
	public List<GroupVO> getAll() {
		return dao.getAll();
	}
	
	
	public List<GroupVO> getCitySport(String groupType, String groupCity) {
		return dao.getCitySport(groupType, groupCity);
	}
	
    public void changeDeadlineStatus() {
    	 dao.changeDeadlineStatus();
    }
    
    public void changeNobodyJoin() {
    	dao.changeNobodyJoin();
    }
    
    public List<GroupVO> serchGroupName(String serchGroupName1, String serchGroupName2, String serchGroupName3){
    	return dao.serchGroupName(serchGroupName1, serchGroupName2, serchGroupName3);
    }


	//////////////////////////////////    成團通知    //////////////////////////////////

	//	查詢近30分鐘，狀態為成團的group_id
	public List<GroupVO> getGroupSuccessMemberlist() {
		return dao.getGroupSuccessMemberlist();
	}

//////////////////////////////////    流團(取消揪團)通知    //////////////////////////////////

	//查詢近30分鐘，group_show為No的group_id
	public List<GroupVO> getGroupCancelMemberlist() {
		return dao.getGroupCancelMemberlist();
	}


//////////////////////////////////    活動提醒通知    //////////////////////////////////

	//查詢狀態為成團，且now() + 6hour <= group_playing_datetime < now() + 6.5hour 的group_id，查詢member_list
	public List<GroupVO> getGroupStartMemberlist() {
		return dao.getGroupStartMemberlist();
	}


//////////////////////////////////    候補失敗通知    //////////////////////////////////

	//查詢狀態為成團，且now() + 6hour <= group_playing_datetime < now() + 6.5hour 的group_id，查詢member_list
	public List<GroupVO> getGroupSecondaryList() {
		return dao.getGroupSecondaryList();
	}


//////////////////////////////////    提醒團長回覆團員出缺席通知    //////////////////////////////////

	//查詢狀態為成團，且now() - 30 minute <= group_playing_datetime < now() 的group_id，查詢團長名單
	public List<GroupVO> getGroupPresentReplyLeader() {
		return dao.getGroupPresentReplyLeader();
	}


//////////////////////////////////    查證檢舉揪團    //////////////////////////////////

	//查證檢舉揪團
	public List<GroupVO> getGroupByKeyWord(String keyword1, String keyword2, String keyword3) {
		return dao.getGroupByKeyWord(keyword1, keyword2, keyword3);
	}




}
