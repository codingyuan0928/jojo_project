package com.group1.gosports_jojo.member.model;

import java.util.*;

public interface MemberDAO_interface {
	   
	
		  public void insert(MemberVO memberVO);
	      //團員加入參團
		  
		  public void insertLeader(MemberVO memberVO);
	      //團長開團
		  
		  public void updateHidden(Integer groupId,Integer userId);
		  //退出揪團
		  
		  public List<MemberVO> findByPrimaryKey(Integer groupId);
	      //顯示group_join畫面，下方團員名單
		  
		  public List<MemberVO> queryCurrentTeam(Integer userId);
	      //顯示group_histoty畫面，現在參團清單
		  
		  public List<MemberVO> queryHistoryTeam(Integer userId);
	      //顯示group_histoty畫面，過去參團清單
		  
		  public MemberVO queryPresentYes(Integer userId);
		  //查present log 為 YES 的 count 為多少
		  
		  public MemberVO queryPresentNo(Integer userId);
		  //查present log 為 No 的 count 為多少
		  
		  public MemberVO countLeaderTimes(Integer userId);
		  //計算過去兩個月當團長次數
		  
		  public MemberVO countLeaderNo(Integer userId);
		  //計算過去兩個月group_show = NO的次數
		  
		  public void changePresentLog (String presentLog,Integer groupId,Integer userId);
		  //退出揪團
		  
		
}
