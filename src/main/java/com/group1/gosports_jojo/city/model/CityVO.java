package com.group1.gosports_jojo.city.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

@Component
public class CityVO  implements java.io.Serializable{
	private String cityType;

	public CityVO() {
		super();
	}

	public String getCityType() {
		return cityType;
	}

	public void setCityType(String cityType) {
		this.cityType = cityType;
	}
	
	
}
	