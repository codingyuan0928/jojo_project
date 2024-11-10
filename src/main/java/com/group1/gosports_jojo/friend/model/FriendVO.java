package com.group1.gosports_jojo.friend.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FriendVO implements java.io.Serializable {
	private Integer friendsId;
	private Integer userId;
	private Integer friendId;
	private String status;
	private Timestamp requestedDatetime;
	private Timestamp respondedDatetime;
	
	
	//加入userVO的username
	private String usernaame;



	public FriendVO() {
		super();
	}

	public Integer getFriendsId() {
		return friendsId;
	}

	public void setFriendsId(Integer friendsId) {
		this.friendsId = friendsId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getFriendId() {
		return friendId;
	}

	public void setFriendId(Integer friendId) {
		this.friendId = friendId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getRequestedDatetime() {
		return requestedDatetime;
	}

	public void setRequestedDatetime(Timestamp requestedDatetime) {
		this.requestedDatetime = requestedDatetime;
	}

	public Timestamp getRespondedDatetime() {
		return respondedDatetime;
	}

	public void setRespondedDatetime(Timestamp respondedDatetime) {
		this.respondedDatetime = respondedDatetime;
	}


	public String getUsernaame() {
		return usernaame;
	}

	public void setUsernaame(String usernaame) {
		this.usernaame = usernaame;
	}

}
