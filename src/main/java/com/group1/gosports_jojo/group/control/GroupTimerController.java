package com.group1.gosports_jojo.group.control;



import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.group1.gosports_jojo.group.model.GroupService;

@Component
public class GroupTimerController{
	TimeZone timeZone = TimeZone.getTimeZone("GMT");

	@Autowired
	GroupService groupService;
	
	

///////////////////////////現在時間大於揪團截止時間時將group_status_desc 改成 '報名截止'//////////////////////////////////
	@Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
	public void changeDeadlineStatus() {
       
		System.out.println("進行changeDeadlineStatus方法");
		groupService.changeDeadlineStatus();
		
	}
	
////////////////現在時間大於揪團截止時間時且無人參團,將 group_show 改成 'NO'///////////////////////////////
	@Scheduled(fixedRate = 30, timeUnit = TimeUnit.MINUTES)
	public void changeNobodyJoin() {
		
		System.out.println("進行changeNobodyJoin方法");
		groupService.changeNobodyJoin();
		
	}
	

		
	
		
////////////////////////////////////////////////////////////////////////////////////

}
		
		
	


