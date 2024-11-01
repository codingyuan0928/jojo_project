package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.OrderDAO_interface;
import com.group1.gosports_jojo.model.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.sql.*;

import javax.sql.DataSource;

@Component
public class OrderDAO implements OrderDAO_interface {

	private final DataSource ds;

	@Autowired
	public OrderDAO(DataSource dataSource){
		this.ds = dataSource;
	}


	//private static final String INSERT_STMT = "INSERT INTO dept2 (order_id,loc) VALUES (?, ?)";  //嚙編嚙磕
	//private static final String GET_ALL_STMT = "SELECT order_id , dname, loc FROM dept2"; //嚙踝蕭嚙踝蕭嚙踝蕭
	//private static final String GET_STATUS = "SELECT order_status FROM orders ";
	//~~~~~~~~~~~~~~~~~~~~~~這裡 要找找問題~~~

	
	//private static final String GET_ALL = "SELECT orderId ,userId, totalAmount,orderStatus, createdDatetime FROM orders where orderStatus = ? order by orderId"; 
	private static final String GET_ALL = "SELECT order_id, user_id, order_status,total_amount, created_datetime, pickup_date  FROM orders where order_status = ? order by order_id";
	private static final String UPDATE = "UPDATE orders set order_status = ?, updated_datetime = curtime() where order_id = ?"; 
	private static final String GET_BY_TIME ="SELECT order_id, user_id, order_status, total_amount, created_datetime, updated_datetime, pickup_date  FROM orders WHERE created_datetime >= ? AND created_datetime < DATE_ADD(?, INTERVAL 1 DAY) ORDER BY order_id;";	

	
	@Override
	public void delete(Integer orderId) {
	}
	

	@Override
	public void update(Integer orderId, Integer orderStatus) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setInt(1, orderStatus);
			pstmt.setInt(2, orderId);
//			pstmt.setTimestamp(2, orderVO.getUpdatedDatetime());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}

	}
	@Override
	public void insert(OrderVO orderVO) {
	}

	@Override
	public OrderVO findByPrimaryKey() {
		return null;
	}

	@Override
	public List<OrderVO> getAll(Integer orderStatus) {
		List<OrderVO> list = new ArrayList<OrderVO>();
		OrderVO orderVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL);
			pstmt.setInt(1, orderStatus);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				orderVO = new OrderVO();
				orderVO.setOrderId(rs.getInt("order_id"));
				orderVO.setUserId(rs.getInt("user_id"));
				orderVO.setOrderStatus(rs.getInt("order_status"));
				orderVO.setTotalAmount(rs.getInt("total_amount"));
				orderVO.setCreatedDatetime(rs.getTimestamp("created_datetime"));
				orderVO.setPickupDate(rs.getTimestamp("pickup_date"));
				
			
				
			
				list.add(orderVO); // Store the row in the list
			}

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	@Override
	public Set<OrderVO> getOrdersByTime(Timestamp begin_time, Timestamp end_time) {
	
		Set<OrderVO> set = new LinkedHashSet<OrderVO>();
		OrderVO orderVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	
		try {
	
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_BY_TIME);
			//pstmt.setInt(1, order_status);
			pstmt.setTimestamp(1, begin_time);
			pstmt.setTimestamp(2, end_time);
			rs = pstmt.executeQuery();
	
			while (rs.next()) {
				orderVO = new OrderVO();
				orderVO.setOrderId(rs.getInt("order_id"));
				orderVO.setUserId(rs.getInt("user_id"));
				orderVO.setOrderStatus(rs.getInt("order_status"));
				orderVO.setTotalAmount(rs.getInt("total_amount"));
				orderVO.setCreatedDatetime(rs.getTimestamp("created_datetime"));
				orderVO.setPickupDate(rs.getTimestamp("pickup_date"));
				set.add(orderVO); // Store the row in the vector
			}
	
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured."
					+ se.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return set;
	}
}