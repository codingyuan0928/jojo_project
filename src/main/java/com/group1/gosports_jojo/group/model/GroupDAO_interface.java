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
	     
	      public List<GroupVO> serchGroupName(String serchGroupName );
	      //模糊查詢球隊名稱
	      
	      public void changeDeadlineStatus();
	      
	      public void changeNobodyJoin();

}
