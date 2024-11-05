package com.group1.gosports_jojo.service.impl.shop;

import com.group1.gosports_jojo.dao.OrderDAO_interface;
import com.group1.gosports_jojo.model.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class OrderService{
	

	private OrderDAO_interface dao;

	@Autowired
	public OrderService(OrderDAO_interface dao) {
		this.dao = dao;
	}

	public List<OrderVO> getAll(Integer orderStatus) {
		return dao.getAll(orderStatus);
	}
	
//
//	public OrderVO getOneOrder(Integer orderno) {
//		return dao.findByPrimaryKey(orderno);
//	}

	public Set<OrderVO> getOrdersByTime(Timestamp beginTime, Timestamp endTime) {
		return dao.getOrdersByTime(beginTime ,endTime);
	}

	public void deleteDept(Integer orderno) {
		dao.delete(orderno);
	}
}

