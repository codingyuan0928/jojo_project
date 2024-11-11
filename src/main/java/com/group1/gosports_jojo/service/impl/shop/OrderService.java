package com.group1.gosports_jojo.service.impl.shop;

import com.group1.gosports_jojo.dao.OrderDAO_interface;
import com.group1.gosports_jojo.entity.Order;
import com.group1.gosports_jojo.model.OrderVO;
import com.group1.gosports_jojo.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

	@Autowired
	private OrderDAO_interface dao;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	public OrderService(OrderDAO_interface dao) {
		this.dao = dao;
	}

	public List<OrderVO> getAll(Integer orderStatus) {
		return dao.getAll(orderStatus);
	}

	public void UpdateOrder(Integer id, Integer status) {
		dao.update(id, status);
	}
//
//	public OrderVO getOneOrder(Integer orderno) {
//		return dao.findByPrimaryKey(orderno);
//	}

	public Set<OrderVO> getOrdersByTime(Timestamp beginTime, Timestamp endTime) {
		return dao.getOrdersByTime(beginTime, endTime);
	}

	public void deleteDept(Integer orderno) {
		dao.delete(orderno);
	}


	public List<Order> getMyOrder(Integer vendorId) {
		// 購物車成功下單的商品
		return orderRepository.findByVendor_VendorId(vendorId);
	}

	public List<OrderVO> getOrderByVendorId(Integer vendorId) {
		return  dao.findOrderByVendorId(vendorId);
	}
}

