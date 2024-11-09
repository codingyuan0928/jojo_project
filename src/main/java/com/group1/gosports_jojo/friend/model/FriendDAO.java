package com.group1.gosports_jojo.friend.model;

import java.util.*;
import java.util.Date;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FriendDAO implements FriendDAO_interface {

//	private static DataSource ds = null;
//	static {
//		try {
//			Context ctx = new InitialContext();
//			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/GoSport");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//	}
	
    private final DataSource ds;

    @Autowired
    public FriendDAO(DataSource ds) {
        this.ds = ds;
    }
	
	//查詢好友邀請--A向B發出好友邀請，通知B
	private static final String GET_FRIEND_PENDING = "SELECT friends.user_id as user_id, friend_id, username "
			+ "FROM friends LEFT JOIN users ON friends.user_id = users.user_id "
			+ "WHERE status = 'PENDING' AND DATE_SUB(now(),INTERVAL 2 minute) <= requested_at AND requested_at < now() ";

	
	//查詢好友成立--B接受A的好友邀請，通知A
	private static final String GET_FRIEND_ACCEPTED = "SELECT friends.user_id as user_id, friend_id, username "
			+ "FROM friends LEFT JOIN users ON friends.friend_id = users.user_id "
			+ "WHERE status = 'ACCEPTED' AND DATE_SUB(now(),INTERVAL 2 minute) <= responded_at AND responded_at < now() ";

	

	//查詢好友邀請
	@Override
    public List<FriendVO> getFriendPending(){
		
		List<FriendVO> list = new ArrayList<FriendVO>();
		FriendVO friendVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_FRIEND_PENDING);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				friendVO = new FriendVO();

				friendVO.setUserId(rs.getInt("user_id"));
				friendVO.setFriendId(rs.getInt("friend_id"));
				friendVO.setUsernaame(rs.getString("username"));

				list.add(friendVO); // Store the row in the list

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
		return list;
	}
	
	
	
    //查詢好友成立
	@Override
    public List<FriendVO> getFriendAccepted(){
		
		List<FriendVO> list = new ArrayList<FriendVO>();
		FriendVO friendVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_FRIEND_ACCEPTED);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				
				friendVO = new FriendVO();

				friendVO.setUserId(rs.getInt("user_id"));
				friendVO.setFriendId(rs.getInt("friend_id"));
				friendVO.setUsernaame(rs.getString("username"));

				list.add(friendVO); // Store the row in the list

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
		return list;
	}
	
}
