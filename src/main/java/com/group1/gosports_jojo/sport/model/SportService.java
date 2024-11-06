package com.group1.gosports_jojo.sport.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("sportService")

public class SportService {
	
	@Autowired
	private SportDAO_interface dao;

	public SportService() {
//		dao = new EmpJDBCDAO();
//		dao = new SportDAO();
	}

	//servlet回傳 groupId userId 引數,為了 insert, 使用dao的insert(memberVO)方法把成員新增
	//並且回傳Mdao.findByPrimaryKey(groupId),list到servlet
	


	public List<SportVO> getAll() {
		return dao.getAll();
	}

	
}
