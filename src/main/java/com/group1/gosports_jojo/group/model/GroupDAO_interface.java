package com.group1.gosports_jojo.group.model;

import java.util.*;

public interface GroupDAO_interface {
	      public void insert(GroupVO groupVO);
	      //團長進行申請揪團頁面
          public void updateAll(GroupVO groupVO);
          //團長進行表格修改頁面
          public void updateHidden(Integer groupId);
          //團長點選取消揪團
          
          public GroupVO findByGroupLeadId(Integer groupLeaderId);
          //點選某團後跳轉頁面
          
          public GroupVO findByPrimaryKey(Integer groupId);
          //點選某團後跳轉頁面
	      public List<GroupVO> getAll();
	      //第一次進到揪團網時顯示所有運動及縣市頁面
	      public List<GroupVO> getCitySport(String groupType ,String groupCity );
	      //選擇球類及縣市後查詢頁面
	     
	      public List<GroupVO> serchGroupName(String serchGroupName1, String serchGroupName2, String serchGroupName3 );
	      //模糊查詢球隊名稱
	      
	      public void changeDeadlineStatus();
	      
	      public void changeNobodyJoin();

	//////////////////////////////////    成團通知    //////////////////////////////////

	public List<GroupVO> getGroupSuccessMemberlist();

//////////////////////////////    流團(取消揪團)通知    /////////////////////////////

	public List<GroupVO> getGroupCancelMemberlist();

//////////////////////////////    活動提醒通知    /////////////////////////////

	public List<GroupVO> getGroupStartMemberlist();

//////////////////////////////    候補失敗通知    /////////////////////////////

	public List<GroupVO> getGroupSecondaryList();

//////////////////////////////    提醒團長回覆團員出缺席通知    /////////////////////////////

	public List<GroupVO> getGroupPresentReplyLeader();

//////////////////////////////    移除檢舉揪團通知    /////////////////////////////

	public List<GroupVO> getGroupReport();

//////////////////////////////////查證檢舉揪團    //////////////////////////////////

	public List<GroupVO> getGroupByKeyWord(String keyword1, String keyword2, String keyword3);


}
