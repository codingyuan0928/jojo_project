package com.group1.gosports_jojo.model;

import java.sql.Timestamp;

public class PicVO implements java.io.Serializable{
	
	private Integer pic_id;
	private Integer post_id;
	private String post_pic;
	private Timestamp created_datetime;
	private Timestamp updated_datetime;
	
	
	public Integer getPic_id() {
		return pic_id;
	}
	public void setPic_id(Integer pic_id) {
		this.pic_id = pic_id;
	}
	public Integer getPost_id() {
		return post_id;
	}
	public void setPost_id(Integer post_id) {
		this.post_id = post_id;
	}
	public String getPost_pic() {
		return post_pic;
	}
	public void setPost_pic(String post_pic) {
		this.post_pic = post_pic;
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
