package com.group1.gosports_jojo.city.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cityService")
public class CityService {
	
	
@Autowired
	private CityDAO_interface dao;

	public CityService() {
//		dao = new EmpJDBCDAO();
//		dao = new CityDAO();
	}

	//servlet回傳 groupId userId 引數,為了 insert, 使用dao的insert(memberVO)方法把成員新增
	//並且回傳Mdao.findByPrimaryKey(groupId),list到servlet
	


	public List<CityVO> getAll() {
		return dao.getAll();
	}

	
}
