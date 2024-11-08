package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.ResponseDAO_interface;
import com.group1.gosports_jojo.model.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

@Component
public class ResponseDAO implements ResponseDAO_interface {

	private final DataSource ds;

	@Autowired
	public ResponseDAO(DataSource dataSource){
		this.ds = dataSource;
	}

	//新增讚
	private static final String NEW_RESPONSE = "INSERT INTO response_details (post_id, user_id, created_datetime) VALUES (?, ?, curtime());";
	//更新點讚狀態
	private static final String UPDATE_RESPONSE = "UPDATE response_details SET response_status= 0 where response_id= ?";
	//顯示單篇留言按讚
	private static final String GET_ONE_RESPONSE = "SELECT post_id, reply_id, response_id, response_status, created_datetime, updated_datetime FROM reply_details WHERE reply_id = ?";
	//顯示所有按讚數量
	private static final String GET_ALL_RESPONSE_AMOUNT = "SELECT count(response_id) as count FROM response_details WHERE post_id = ?";
	//顯示單篇文章所有按讚(用戶是否在某篇文章點讚)
	private static final String GET_POST_ALL_RESPONSE ="SELECT response_id, response_status FROM response_details WHERE  post_id = ? AND user_id =?";
	
	

	
	//新增讚
	
	public void insert(ResponseVO responseVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(NEW_RESPONSE);

			pstmt.setInt(1, responseVO.getPost_id());
			pstmt.setInt(2, responseVO.getUser_id());

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}
	
	//更新點讚狀態
	@Override
	public void updateResponse(Integer response_id) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_RESPONSE);
			pstmt.setInt(1, response_id);
			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}
	
	//顯示單個按讚
	@Override
	public  ResponseVO getOneResponse(Integer response_id) {

		ResponseVO responseVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ONE_RESPONSE);

				pstmt.setInt(1, response_id);

				rs = pstmt.executeQuery();

				if (rs.next()) {
					responseVO = new ResponseVO();
					responseVO.setPost_id(rs.getInt("post_id"));
					responseVO.setReply_id(rs.getInt("reply_id"));
					responseVO.setResponse_id(rs.getInt("response_id"));
					responseVO.setResponse_status(rs.getInt("response_status"));
					responseVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
					responseVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));
				}

			} catch (SQLException se) {
				throw new RuntimeException("A database error occurred. " + se.getMessage());
			} finally {
				closeResources(con, pstmt, rs);
			}
			return responseVO;
		}
	
	////顯示所有按讚數量
	@Override
	public Integer getAllResponseAmount(Integer post_id) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Integer count = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_RESPONSE_AMOUNT);
			pstmt.setInt(1, post_id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt("count");
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
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
		return count;

	}
	
	//顯示單篇文章所有按讚(用戶是否在某篇文章點讚)
	@Override
	public ResponseVO getPostAllResponse (Integer post_id, Integer user_id) {
		
		ResponseVO responseVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
	

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_POST_ALL_RESPONSE);
			pstmt.setInt(1, post_id);
			pstmt.setInt(2, user_id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				responseVO = new ResponseVO();
				responseVO.setResponse_id(rs.getInt("response_id"));
				responseVO.setResponse_status(rs.getInt("response_status"));
			}

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
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
		return responseVO;

	}

	// 關閉資源的輔助方法
	private void closeResources(Connection con, PreparedStatement pstmt, ResultSet rs) {
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





	}

