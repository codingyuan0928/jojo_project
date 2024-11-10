package com.group1.gosports_jojo.notificationitem.model;

import java.util.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class NotificationItemListDAO implements NotificationItemListDAO_interface {

	// 一個應用程式中,針對一個資料庫 ,共用一個DataSource即可
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
    public NotificationItemListDAO(DataSource ds) {
        this.ds = ds;
    }

	private static final String GET_ALL_STMT = 
		"SELECT notification_id,notification_item FROM notification_item_lists order by notification_id";


	@Override
	public List<NotificationItemListVO> getAll() {
		List<NotificationItemListVO> list = new ArrayList<NotificationItemListVO>();
		NotificationItemListVO notificationItemListVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				notificationItemListVO = new NotificationItemListVO();
				notificationItemListVO.setNotificationId(rs.getInt("notification_id"));
				notificationItemListVO.setNotificationItem(rs.getString("notification_item"));

				list.add(notificationItemListVO); // Store the row in the list
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