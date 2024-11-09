package com.group1.gosports_jojo.model;

import java.sql.Timestamp;

public class PostVO implements java.io.Serializable{
	
	private Integer post_id;
	private Integer user_id;
	private String post_title;
	private String post_category;
	private String post_content;
	private Timestamp created_datetime;
	private Timestamp updated_datetime;
	private Integer post_status;
	
	private Integer reply_id;
	private Integer reply_status;
	private String reply_content;
	private Integer count; 
	private Integer good;


	private String 	username;
	
	public Integer getPost_id() {
		return post_id;
	}
	public void setPost_id(Integer post_id) {
		this.post_id = post_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getPost_title() {
		return post_title;
	}
	public void setPost_title(String post_title) {
		this.post_title = post_title;
	}
	public String getPost_category() {
		return post_category;
	}
	public void setPost_category(String post_category) {
		this.post_category = post_category;
	}
	public String getPost_content() {
		return post_content;
	}
	public void setPost_content(String post_content) {
		this.post_content = post_content;
	}
	public Timestamp getCreated_datetime() {
		return created_datetime;
	}
	public void setCreated_datetime(Timestamp created_datetime) {
		this.created_datetime = created_datetime;
	}
	public Timestamp getUpdated_datetime() {
		return updated_datetime;
	}
	public void setUpdated_datetime(Timestamp updated_datetime) {
		this.updated_datetime = updated_datetime;
	}
	public Integer getPost_status() {
		return post_status;
	}
	public void setPost_status(Integer post_status) {
		this.post_status = post_status;
	}
	
	
	
	
	public Integer getReply_id() {
		return reply_id;
	}
	public void setReply_id(Integer reply_id) {
		this.reply_id = reply_id;
	}

	public Integer getReply_status() {
		return reply_status;
	}
	public void setReply_status(Integer reply_status) {
		this.reply_status = reply_status;
	}
	public String getReply_content() {
		return reply_content;
	}
	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
		
		
		public Integer getGood() {
			return good;
		}
		public void setGood(Integer good) {
			this.good = good;
		}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


}
