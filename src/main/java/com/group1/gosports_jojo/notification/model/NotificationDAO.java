package com.group1.gosports_jojo.notification.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationDAO implements NotificationDAO_interface {

	private final DataSource ds;

	@Autowired
	public NotificationDAO(DataSource ds) {
		this.ds = ds;
	}

	// 新增成團通知
	private static final String INSERT_GROUP_SUCCESS_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '揪團', '1.成團', curtime());";

	// 新增取消揪團通知
	private static final String INSERT_GROUP_CANCEL_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '揪團', '2.流團', curtime())";

	// 新增活動提醒通知
	private static final String INSERT_GROUP_START_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '揪團', '3.活動提醒', curtime())";

	// 新增候補失敗通知
	private static final String INSERT_GROUP_SECONDARY_LIST_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '揪團', '4.候補失敗', curtime())";

	// 新增提醒團長回覆團員出缺席通知
	private static final String INSERT_GROUP_PRESENT_REPLY_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '揪團', '6.提醒團長回覆團員出缺席', curtime())";

	// 查詢缺席警告名單
	private static final String GET_GROUP_ABSENCE_ALERT_LIST = "SELECT m2.user_id as user_id FROM "
			+ "(SELECT user_id, count(present_log) FROM member_lists WHERE DATE_SUB(now(),INTERVAL 2 minute) <= updated_datetime AND updated_datetime < now() AND present_log = 'NO' GROUP BY user_id HAVING count(present_log) = 1) m1 "
			+ "JOIN (SELECT user_id, count(present_log) FROM member_lists WHERE updated_datetime > DATE_SUB(now(),INTERVAL 30 day) AND present_log = 'NO' GROUP BY user_id HAVING count(present_log) = 4) m2 "
			+ "ON m1.user_id = m2.user_id";

	// 新增缺席警告通知
	private static final String INSERT_GROUP_ABSENCE_ALERT_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, notification_category, notification_item, send_datetime) VALUES (?, '揪團', '5.缺席警告', curtime())";

	// 新增: 移除揪團通知
	private static final String INSERT_GROUP_REPORT_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '揪團', '7.移除揪團', curtime())";

	// 新增: 移除文章通知
	private static final String INSERT_ARTICLE_REPORT_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '論壇', '10.移除文章', curtime())";

	// 新增: 移除留言通知
	private static final String INSERT_REPLY_REPORT_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '論壇', '11.移除留言', curtime())";

	// 新增: 移除商品通知
	private static final String INSERT_PRODUCT_REPORT_NOTIFICATION = "INSERT INTO notification_lists (notified_vendor_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '商城', '15.商品下架(經檢舉)(seller)', curtime())";

	// 新增訂單成立通知(buyer)
	private static final String INSERT_ORDER_CREATED_BUYER_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, notification_category, notification_item, send_datetime) VALUES (?, ?, '商城', '12.訂單成立(buyer)', curtime())";

	// 新增訂單成立通知(seller)
	private static final String INSERT_ORDER_CREATED_SELLER_NOTIFICATION = "INSERT INTO notification_lists (notified_vendor_id, reference_id, notification_category, notification_item, send_datetime) VALUES (?, ?, '商城', '13.訂單成立(seller)', curtime())";

	// 新增訂單完成通知(buyer)
	private static final String INSERT_ORDER_COMPLETED_BUYER_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, notification_category, notification_item, send_datetime) VALUES (?, ?, '商城', '14.訂單完成(buyer)', curtime())";

	// 新增廠商停權(經檢舉)(seller)
	private static final String INSERT_VENDOR_DEACTIVATE_NOTIFICATION = "INSERT INTO notification_lists (notified_vendor_id, notification_category, notification_item, send_datetime) VALUES (?, '商城', '16.廠商停權(經檢舉)(seller)', curtime())";

	// 新增「17.廠商資格審核(seller)-1」通知 (近1分鐘，註冊的廠商名單)
	private static final String INSERT_VENDOR_CHECK_NOTIFICATION_1 = "INSERT INTO notification_lists (notified_vendor_id, notification_category, notification_item, send_datetime) VALUES (?, '廠商資格審核', '17.廠商資格審核(seller)-1', curtime())";

	// 新增「18.廠商資格審核(seller)-2 」通知 (近1分鐘，審核結果為「審核未通過(待補件)」)
	private static final String INSERT_VENDOR_CHECK_NOTIFICATION_2 = "INSERT INTO notification_lists (notified_vendor_id, notification_category, notification_item, send_datetime) VALUES (?, '廠商資格審核', '18.廠商資格審核(seller)-2', curtime())";

	// 新增好友邀請通知
	private static final String INSERT_FRIEND_PENDING_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '交友', '8.好友邀請', curtime())";

	// 新增好友成立通知
	private static final String INSERT_FRIEND_ACCEPTED_NOTIFICATION = "INSERT INTO notification_lists (notified_user_id, reference_id, reference_id_desc, notification_category, notification_item, send_datetime) VALUES (?, ?, ?, '交友', '9.成為好友', curtime())";

	// 使用USER ID 查詢所有通知表
	private static final String GET_NOTIFICATION_BY_USER = "SELECT notification_id, notified_user_id, reference_id, reference_id_desc, "
			+ "notification_category, notification_item, send_datetime "
			+ "FROM notification_lists WHERE display='Y' AND notified_user_id = ? order by send_datetime desc";

	// 使用vendor ID 查詢所有通知表
	private static final String GET_NOTIFICATION_BY_VENDOR = "SELECT notification_id, notified_vendor_id, reference_id, reference_id_desc, "
			+ "notification_category, notification_item, send_datetime "
			+ "FROM notification_lists WHERE display='Y' AND notified_vendor_id = ? order by send_datetime desc";

	// 用戶/廠商隱藏通知(設定display=N)
	private static final String HIDDEN_NOTIFICATION = "UPDATE notification_lists set display='N', update_datetime=curtime() where notification_id = ?";

	// 將「用戶未讀的所有通知id」，更新為已讀(設定readed ='Y')
	private static final String UPDATE_NOTIFICATION_READED_C = "UPDATE notification_lists set readed ='Y' WHERE notified_user_id = ? AND display='Y' AND readed ='N'";

	// 查詢用戶未讀通知id
	private static final String GET_UNREAD_NOTIFICATION_C = "SELECT notification_id FROM notification_lists WHERE notified_user_id = ? AND display='Y' AND readed ='N' ";

	// 查詢用戶未讀通知數
	private static final String GET_COUNT_UNREAD_NOTIFICATION_C = "SELECT count(*) as count FROM notification_lists WHERE notified_user_id = ? AND display='Y' AND readed ='N' ";

	// 將「廠商未讀的所有通知id」，更新為已讀(設定readed ='Y')
	private static final String UPDATE_NOTIFICATION_READED_V = "UPDATE notification_lists set readed ='Y' WHERE notified_vendor_id = ? AND display='Y' AND readed ='N'";

	// 查詢廠商未讀通知id
	private static final String GET_UNREAD_NOTIFICATION_V = "SELECT notification_id FROM notification_lists WHERE notified_vendor_id = ? AND display='Y' AND readed ='N'";

	// 查詢廠商未讀通知數
	private static final String GET_COUNT_UNREAD_NOTIFICATION_V = "SELECT count(*) as count FROM notification_lists WHERE notified_vendor_id = ? AND display='Y' AND readed ='N'";

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------

//新增成團通知
	@Override
	public void insertGroupSuccessNotification(NotificationVO notificationVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_GROUP_SUCCESS_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增候補失敗通知
	@Override
	public void insertGroupSecondaryListNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_GROUP_SECONDARY_LIST_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增取消揪團通知
	@Override
	public void insertGroupCancelNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_GROUP_CANCEL_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增活動提醒通知
	@Override
	public void insertGroupStartNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_GROUP_START_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增提醒團長回覆團員出缺席通知
	@Override
	public void insertGroupPresentReplyNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_GROUP_PRESENT_REPLY_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//查詢缺席警告通知名單
	@Override
	public List<NotificationVO> getGroupAbsenceAlertList() {
		List<NotificationVO> list = new ArrayList<NotificationVO>();
		NotificationVO notificationVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_GROUP_ABSENCE_ALERT_LIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				notificationVO = new NotificationVO();

				notificationVO.setUserId(rs.getInt("user_id"));
				list.add(notificationVO); // Store the row in the list

			}

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return list;

	}

//新增缺席警告通知
	@Override
	public void insertGroupAbsenceAlertyNotification(Integer notifiedUserId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_GROUP_ABSENCE_ALERT_NOTIFICATION);

			pstmt.setInt(1, notifiedUserId);

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增: 移除揪團通知
	@Override
	public void insertGroupReportNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_GROUP_REPORT_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增: 移除文章通知
	@Override
	public void insertArticleReportNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_ARTICLE_REPORT_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增: 移除留言通知
	@Override
	public void insertReplyReportNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_REPLY_REPORT_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增: 移除商品通知
	@Override
	public void insertProductReportNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_PRODUCT_REPORT_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedVendorId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增好友邀請通知
	@Override
	public void insertFriendPendingNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_FRIEND_PENDING_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增好友成立通知
	@Override
	public void insertFriendAcceptedNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_FRIEND_ACCEPTED_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());
			pstmt.setString(3, notificationVO.getReferenceIdDesc());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增訂單成立通知(buyer)
	@Override
	public void insertOrderCreatedBuyerNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_ORDER_CREATED_BUYER_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增訂單成立通知(seller)
	@Override
	public void insertOrderCreatedSellerNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_ORDER_CREATED_SELLER_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedVendorId());
			pstmt.setInt(2, notificationVO.getReferenceId());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增訂單完成通知(buyer)
	@Override
	public void insertOrderCompletedBuyerNotification(NotificationVO notificationVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_ORDER_COMPLETED_BUYER_NOTIFICATION);

			pstmt.setInt(1, notificationVO.getNotifiedUserId());
			pstmt.setInt(2, notificationVO.getReferenceId());

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增廠商停權(經檢舉)(seller)
	@Override
	public void insertVendorDeactivateNotification(Integer notifiedVendorId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_VENDOR_DEACTIVATE_NOTIFICATION);

			pstmt.setInt(1, notifiedVendorId);

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增「17.廠商資格審核(seller)-1」通知
	@Override
	public void insertVendorCheckNotification1(Integer notifiedVendorId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_VENDOR_CHECK_NOTIFICATION_1);

			pstmt.setInt(1, notifiedVendorId);

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//新增「18.廠商資格審核(seller)-2」通知
	@Override
	public void insertVendorCheckNotification2(Integer notifiedVendorId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_VENDOR_CHECK_NOTIFICATION_2);

			pstmt.setInt(1, notifiedVendorId);

			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

///////////////////////////////////////////////////////////////////////////////////

//使用USER ID 查詢所有通知表
	@Override
	public List<NotificationVO> getNotificationByUser(Integer notifiedUserId) {
		List<NotificationVO> list = new ArrayList<NotificationVO>();
		NotificationVO notificationVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_NOTIFICATION_BY_USER);

			pstmt.setInt(1, notifiedUserId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				notificationVO = new NotificationVO();

				notificationVO.setNotificationId(rs.getInt("notification_id"));
				notificationVO.setNotifiedUserId(rs.getInt("notified_user_id"));
				notificationVO.setReferenceId(rs.getInt("reference_id"));
				notificationVO.setReferenceIdDesc(rs.getString("reference_id_desc"));
				notificationVO.setNotificationCategory(rs.getString("notification_category"));
				notificationVO.setNotificationItem(rs.getString("notification_item"));
				notificationVO.setSendDatetime(rs.getTimestamp("send_datetime"));
				list.add(notificationVO); // Store the row in the list

			}

// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return list;

	}

//使用VENDOR ID 查詢所有通知表
	@Override
	public List<NotificationVO> getNotificationByVendor(Integer notifiedVendorId) {
		List<NotificationVO> list = new ArrayList<NotificationVO>();
		NotificationVO notificationVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_NOTIFICATION_BY_VENDOR);

			pstmt.setInt(1, notifiedVendorId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				notificationVO = new NotificationVO();

				notificationVO.setNotificationId(rs.getInt("notification_id"));
				notificationVO.setNotifiedVendorId(rs.getInt("notified_vendor_id"));
				notificationVO.setReferenceId(rs.getInt("reference_id"));
				notificationVO.setReferenceIdDesc(rs.getString("reference_id_desc"));
				notificationVO.setNotificationCategory(rs.getString("notification_category"));
				notificationVO.setNotificationItem(rs.getString("notification_item"));
				notificationVO.setSendDatetime(rs.getTimestamp("send_datetime"));

				list.add(notificationVO); // Store the row in the list

			}

// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return list;

	}

///////////////////////////////////////////////////////////////////////////////////

	// 用戶/廠商隱藏通知(設定display=N)
	public void hiddenNotification(Integer notificationId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(HIDDEN_NOTIFICATION);

			pstmt.setInt(1, notificationId);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

	// 查詢用戶未讀通知id
	@Override
	public List<NotificationVO> getUnreadNotificationC(Integer notifiedUserId) {
		List<NotificationVO> list = new ArrayList<NotificationVO>();
		NotificationVO notificationVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_UNREAD_NOTIFICATION_C);

			pstmt.setInt(1, notifiedUserId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				notificationVO = new NotificationVO();

				notificationVO.setNotificationId(rs.getInt("notification_id"));

				list.add(notificationVO); // Store the row in the list
			}

// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return list;

	}

	// 查詢用戶未讀通知數
	public NotificationVO getCountUnreadNotificationC(Integer notifiedUserId) {

		NotificationVO notificationVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_COUNT_UNREAD_NOTIFICATION_C);
			pstmt.setInt(1, notifiedUserId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				notificationVO = new NotificationVO();
				notificationVO.setCount(rs.getInt("count"));

			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return notificationVO;
	}

	// 將「用戶未讀的所有通知id」，更新為已讀(設定readed ='Y')
	public void updateNotificationReadedC(Integer notifiedUserId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_NOTIFICATION_READED_C);

			pstmt.setInt(1, notifiedUserId);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

//	//查詢廠商未讀通知id
	@Override
	public List<NotificationVO> getUnreadNotificationV(Integer notifiedVendorId) {
		List<NotificationVO> list = new ArrayList<NotificationVO>();
		NotificationVO notificationVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_UNREAD_NOTIFICATION_V);

			pstmt.setInt(1, notifiedVendorId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				notificationVO = new NotificationVO();

				notificationVO.setNotificationId(rs.getInt("notification_id"));

				list.add(notificationVO); // Store the row in the list
			}

// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return list;

	}

	// 查詢廠商未讀通知數
	public NotificationVO getCountUnreadNotificationV(Integer notifiedVendorId) {

		NotificationVO notificationVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			pstmt = con.prepareStatement(GET_COUNT_UNREAD_NOTIFICATION_V);
			pstmt.setInt(1, notifiedVendorId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				notificationVO = new NotificationVO();
				notificationVO.setCount(rs.getInt("count"));

			}
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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
		return notificationVO;
	}

	// 將「廠商未讀的所有通知id」，更新為已讀(設定readed ='Y')
	public void updateNotificationReadedV(Integer notifiedVendorId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_NOTIFICATION_READED_V);

			pstmt.setInt(1, notifiedVendorId);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
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

}