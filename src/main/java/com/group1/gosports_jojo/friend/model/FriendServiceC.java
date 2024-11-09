package com.group1.gosports_jojo.friend.model;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendServiceC {

	private FriendDAO_interface dao;
	
	@Autowired
	public FriendServiceC(FriendDAO_interface dao) {
//		dao = new FriendDAO();
		this.dao = dao;

	}

	// 查詢好友邀請
	public List<FriendVO> getFriendPending() {
		return dao.getFriendPending();
	}

	// 查詢好友成立
	public List<FriendVO> getFriendAccepted(){
		return dao.getFriendAccepted();

	}
	
	
	
}
