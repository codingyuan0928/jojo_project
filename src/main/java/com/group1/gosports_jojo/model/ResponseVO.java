package com.group1.gosports_jojo.model;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
@Component
public class ResponseVO implements java.io.Serializable{
	private Integer response_id;
	private Integer post_id;
	private Integer reply_id;
	private Integer user_id;
	private Integer response_status;
	private Timestamp created_datetime;
	private Timestamp updated_datetime;
	
	
	public Integer getResponse_id() {
		return response_id;
	}
	public void setResponse_id(Integer response_id) {
		this.response_id = response_id;
	}
	public Integer getPost_id() {
		return post_id;
	}
	public void setPost_id(Integer post_id) {
		this.post_id = post_id;
	}
	public Integer getReply_id() {
		return reply_id;
	}
	public void setReply_id(Integer reply_id) {
		this.reply_id = reply_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getResponse_status() {
		return response_status;
	}
	public void setResponse_status(Integer response_status) {
		this.response_status = response_status;
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

	
}
