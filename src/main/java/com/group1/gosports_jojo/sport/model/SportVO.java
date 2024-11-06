package com.group1.gosports_jojo.sport.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component

public class SportVO  implements java.io.Serializable{
	private String sportType;

	public String getSportType() {
		return sportType;
	}

	public void setSportType(String sportType) {
		this.sportType = sportType;
	}

	public SportVO() {
		super();
	}

	
	
}
	