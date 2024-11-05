package com.group1.gosports_jojo.dao;

import java.sql.Timestamp;
import java.util.*;
import com.group1.gosports_jojo.model.OrderVO;

public interface OrderDAO_interface {
	      public void insert(OrderVO orderVO);
          public void update(Integer orderId, Integer orderStatus);
          public void delete(Integer orderId);
          public OrderVO findByPrimaryKey();
	      public List<OrderVO> getAll(Integer orderStatus);
	      public Set<OrderVO> getOrdersByTime(Timestamp begintTime, Timestamp endTime);
}