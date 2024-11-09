package com.group1.gosports_jojo.customersupport.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerSupportDAO implements CustomerSupportDAO_interface {
	
    private final DataSource ds;

    @Autowired
    public CustomerSupportDAO(DataSource ds) {
        this.ds = ds;
    }

	// -- 傳入廠商資格審核
	private static final String INSERT_VENDOR_STMT = "INSERT INTO customer_support_forms (administrator_id, vendor_id, category, title, issue_description) "
			+ "VALUES (1, ?, '廠商資格審核', '廠商資格審核', '廠商資格審核')";

	
	// -- 傳入客服表單
	private static final String INSERT_FEEDBACK_STMT = "INSERT INTO customer_support_forms (administrator_id, user_id, category, title, issue_description) VALUES (2, ?, ?, ?, ?)";

	// -- 後臺管理員處理客服表單後(2: 結案_毋須處理)，更新狀態
	private static final String UPDATE_FEEDBACK2_STMT = "UPDATE customer_support_forms set status=?, notification_content=?, reviewed_datetime = curtime() where form_id = ?";

	// -- 後臺管理員處理客服表單後(3：結案_發送通知信件、4: 發送通知信件(待處理))，更新狀態
	private static final String UPDATE_FEEDBACK3_STMT = "UPDATE customer_support_forms set status=?, reference_id=?, notification_content=?, reviewed_datetime = curtime() where form_id = ?";

	// -- 回傳客服表單傳送成功內容
	private static final String GET_NEW_FEEDBACK_STMT = "SELECT user_id, category, title, issue_description, submission_datetime FROM customer_support_forms "
			+ "WHERE user_id = ? AND (administrator_id = 2) ORDER BY submission_datetime DESC LIMIT 1";

	// 查詢administrator_id = 1廠商審核單，狀態「未處理、待處理」
	private static final String GET_PENDING_V_STMT = "SELECT administrator_id, vendor_id, submission_datetime, status FROM customer_support_forms "
			+ "WHERE administrator_id = 1 AND status IN ('1:未處理', '4：發送通知信件(待處理)') ORDER BY submission_datetime DESC";

	// 查詢所有administrator_id = 1廠商審核單，不分狀態
	public static final String GET_ALL_V_STMT = "SELECT administrator_id, vendor_id, submission_datetime, status FROM customer_support_forms "
			+ "WHERE administrator_id = 1 ORDER BY submission_datetime DESC";

	// 查詢administrator_id = 2客服表單，狀態「未處理、待處理」
	private static final String GET_PENDING_C_STMT = "SELECT form_id, administrator_id, user_id, category, title, issue_description, submission_datetime, status "
			+ "FROM customer_support_forms "
			+ "WHERE administrator_id = 2 AND status ='1: 未處理' ORDER BY submission_datetime DESC";

	// 查詢所有administrator_id = 2客服表單，不分狀態
	public static final String GET_ALL_C_STMT = "SELECT form_id, administrator_id, user_id, reference_id, category, title, issue_description, submission_datetime, "
			+ "status, notification_content, reviewed_datetime " + "FROM customer_support_forms "
			+ "WHERE administrator_id = 2 ORDER BY submission_datetime DESC";

	// 查詢過去1分鐘更新狀態，且須發送通知的「移除檢舉揪團」名單
	public static final String GET_REPORTED_GROUP_LIST = "SELECT reference_id, group_leader_id, group_name "
			+ "FROM customer_support_forms LEFT JOIN group_lists ON customer_support_forms.reference_id = group_lists.group_id "
			+ "WHERE administrator_id = 2 AND status='3：結案_發送通知信件' AND notification_content = '移除揪團' "
			+ "AND DATE_SUB(now(),INTERVAL 1 minute) <= reviewed_datetime AND reviewed_datetime < now() ";

	// 查詢過去1分鐘更新狀態，且須發送通知的「移除檢舉文章」名單
	public static final String GET_REPORTED_ARTICLE_LIST = "SELECT reference_id, posts.user_id as user_id, post_title "
			+ "FROM customer_support_forms LEFT JOIN posts ON customer_support_forms.reference_id = posts.post_id "
			+ "WHERE administrator_id = 2 AND status='3：結案_發送通知信件' AND notification_content = '移除文章' "
			+ "AND DATE_SUB(now(),INTERVAL 1 minute) <= reviewed_datetime AND reviewed_datetime < now() ";

	// 查詢過去1分鐘更新狀態，且須發送通知的「移除檢舉留言」名單
	// 欄位：留言編號/留言用戶/文章
	public static final String GET_REPORTED_REPLY_LIST = "SELECT reference_id, a.user_id AS user_id, posts.post_id AS post_id, post_title "
			+ "FROM (SELECT reference_id, reply_details.user_id, post_id "
			+ "FROM customer_support_forms LEFT JOIN reply_details ON customer_support_forms.reference_id = reply_details.reply_id "
			+ "WHERE administrator_id = 2 AND status='3：結案_發送通知信件' AND notification_content = '移除留言' "
			+ "AND DATE_SUB(now(),INTERVAL 1 minute) <= reviewed_datetime AND reviewed_datetime < now()) a, posts "
			+ "WHERE a.post_id = posts.post_id ";
	
	// 查詢過去1分鐘更新狀態，且須發送通知的「商品下架(經檢舉)(seller)」名單
	public static final String GET_REPORTED_PRODUCT_LIST = "SELECT reference_id, products.vendor_id as vendor_id, product_name "
			+ "FROM customer_support_forms LEFT JOIN products ON customer_support_forms.reference_id = products.product_id "
			+ "WHERE administrator_id = 2 AND status='3：結案_發送通知信件' AND notification_content = '商品下架(經檢舉)(seller)' "
			+ "AND DATE_SUB(now(),INTERVAL 1 minute) <= reviewed_datetime AND reviewed_datetime < now() ";

	
	// 查詢過去1分鐘更新狀態，且須發送通知的「廠商停權(經檢舉)(seller)」名單
	public static final String GET_REPORTED_VENDOR_LIST = "SELECT reference_id FROM customer_support_forms "
			+ "WHERE administrator_id = 2 AND status='3：結案_發送通知信件' AND notification_content = '廠商停權(經檢舉)(seller)' "
			+ "AND DATE_SUB(now(),INTERVAL 1 minute) <= reviewed_datetime AND reviewed_datetime < now()";
	
	
	
	
	

	@Override
	public void insertVendorForCheck(Integer vendorId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_VENDOR_STMT);

			pstmt.setInt(1, vendorId);

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

	@Override
	public void insertFeedback(CustomerSupportVO customerSupportVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_FEEDBACK_STMT);

			pstmt.setInt(1, customerSupportVO.getUserId());
			pstmt.setString(2, customerSupportVO.getCategory());
			pstmt.setString(3, customerSupportVO.getTitle());
			pstmt.setString(4, customerSupportVO.getIssueDescription());

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

@Override
	public List<CustomerSupportVO> findPendingCaseV() {
		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_PENDING_V_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// CustomerSupportDAO也稱為Domain objects
				customerSupportVO = new CustomerSupportVO();
				customerSupportVO.setAdministratorId(rs.getInt("administrator_id"));
				customerSupportVO.setVendorId(rs.getInt("vendor_id"));
				customerSupportVO.setSubmissionDatetime(rs.getTimestamp("submission_datetime"));
				customerSupportVO.setStatus(rs.getString("status"));
				list.add(customerSupportVO); // Store the row in the list

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

	@Override
	public List<CustomerSupportVO> getAllV() {
		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_V_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// CustomerSupportDAO 也稱為 Domain objects
				customerSupportVO = new CustomerSupportVO();
				customerSupportVO.setAdministratorId(rs.getInt("administrator_id"));
				customerSupportVO.setVendorId(rs.getInt("vendor_id"));
				customerSupportVO.setSubmissionDatetime(rs.getTimestamp("submission_datetime"));
				customerSupportVO.setStatus(rs.getString("status"));
				list.add(customerSupportVO); // Store the row in the list
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
	
	

	@Override
	public CustomerSupportVO findNewFeedback(Integer userId) {

		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_NEW_FEEDBACK_STMT);
			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// CustomerSupportDAO也稱為Domain objects
				customerSupportVO = new CustomerSupportVO();
				customerSupportVO.setUserId(rs.getInt("user_id"));
				customerSupportVO.setCategory(rs.getString("category"));
				customerSupportVO.setTitle(rs.getString("title"));
				customerSupportVO.setIssueDescription(rs.getString("issue_description"));
				customerSupportVO.setSubmissionDatetime(rs.getTimestamp("submission_datetime"));

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
		return customerSupportVO;
	}

	
	// -- 後臺管理員處理客服表單後(2: 結案_毋須處理)，更新狀態
	public CustomerSupportVO updateFeedback2(CustomerSupportVO customerSupportVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_FEEDBACK2_STMT);

			pstmt.setString(1, customerSupportVO.getStatus());
			pstmt.setString(2, customerSupportVO.getNotificationContent());
			pstmt.setInt(3, customerSupportVO.getFormId());

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
		return customerSupportVO;
	}
	
	

	// -- 後臺管理員處理客服表單後(3：結案_發送通知信件)，更新狀態
	public CustomerSupportVO updateFeedback3(CustomerSupportVO customerSupportVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE_FEEDBACK3_STMT);
			pstmt.setString(1, customerSupportVO.getStatus());
			pstmt.setInt(2, customerSupportVO.getReferenceId());
			pstmt.setString(3, customerSupportVO.getNotificationContent());
			pstmt.setInt(4, customerSupportVO.getFormId());

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
		return customerSupportVO;
	}
	
	

	@Override
	public List<CustomerSupportVO> findPendingCaseC() {
		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_PENDING_C_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// CustomerSupportDAO也稱為Domain objects
				customerSupportVO = new CustomerSupportVO();
				customerSupportVO.setFormId(rs.getInt("form_id"));
				customerSupportVO.setAdministratorId(rs.getInt("administrator_id"));
				customerSupportVO.setUserId(rs.getInt("user_id"));
				customerSupportVO.setCategory(rs.getString("category"));
				customerSupportVO.setTitle(rs.getString("title"));
				customerSupportVO.setIssueDescription(rs.getString("issue_description"));
				customerSupportVO.setSubmissionDatetime(rs.getTimestamp("submission_datetime"));
				customerSupportVO.setStatus(rs.getString("status"));
				list.add(customerSupportVO); // Store the row in the list

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

	@Override
	public List<CustomerSupportVO> getAllC() {
		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_C_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// CustomerSupportDAO 也稱為 Domain objects
				customerSupportVO = new CustomerSupportVO();
				customerSupportVO.setFormId(rs.getInt("form_id"));
				customerSupportVO.setAdministratorId(rs.getInt("administrator_id"));
				customerSupportVO.setUserId(rs.getInt("user_id"));
				customerSupportVO.setReferenceId(rs.getInt("reference_id"));
				customerSupportVO.setCategory(rs.getString("category"));
				customerSupportVO.setTitle(rs.getString("title"));
				customerSupportVO.setIssueDescription(rs.getString("issue_description"));
				customerSupportVO.setSubmissionDatetime(rs.getTimestamp("submission_datetime"));
				customerSupportVO.setStatus(rs.getString("status"));
				customerSupportVO.setNotificationContent(rs.getString("notification_content"));
				customerSupportVO.setReviewedDatetime(rs.getTimestamp("reviewed_datetime"));
				list.add(customerSupportVO); // Store the row in the list
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

	
	@Override
	public void updateVendorResult(CustomerSupportVO customerSupportVO) {

	}

	
	// 查詢過去1分鐘更新狀態，且須發送通知的「移除檢舉揪團」名單
	@Override
	public List<CustomerSupportVO> getReportedGroupList() {

		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_REPORTED_GROUP_LIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				customerSupportVO = new CustomerSupportVO();

				customerSupportVO.setReferenceId(rs.getInt("reference_id"));
				customerSupportVO.setGroupLeaderId(rs.getInt("group_leader_id"));
				customerSupportVO.setGroupName(rs.getString("group_name"));

				list.add(customerSupportVO); // Store the row in the list
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
	
	
	// 查詢過去1分鐘更新狀態，且須發送通知的「移除檢舉文章」名單
	@Override
	public List<CustomerSupportVO> getReportedArticleList() {

		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_REPORTED_ARTICLE_LIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				customerSupportVO = new CustomerSupportVO();

				customerSupportVO.setReferenceId(rs.getInt("reference_id"));
				customerSupportVO.setUser_id(rs.getInt("user_id"));
				customerSupportVO.setPost_title(rs.getString("post_title"));

				list.add(customerSupportVO); // Store the row in the list
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
	
	
	// 查詢過去1分鐘更新狀態，且須發送通知的「移除檢舉留言」名單
	@Override
	public List<CustomerSupportVO> getReportedReplyList() {

		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_REPORTED_REPLY_LIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				customerSupportVO = new CustomerSupportVO();

				customerSupportVO.setReferenceId(rs.getInt("reference_id"));
				customerSupportVO.setUserId(rs.getInt("user_id"));
				customerSupportVO.setPost_title(rs.getString("post_title"));

				list.add(customerSupportVO); // Store the row in the list
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
	
	
	// 查詢過去1分鐘更新狀態，且須發送通知的「商品下架(經檢舉)(seller)」名單
	@Override
	public List<CustomerSupportVO> getReportedProductList() {

		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_REPORTED_PRODUCT_LIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				customerSupportVO = new CustomerSupportVO();

				customerSupportVO.setReferenceId(rs.getInt("reference_id"));
				customerSupportVO.setVendorId(rs.getInt("vendor_id"));
				customerSupportVO.setProductName(rs.getString("product_name"));

				list.add(customerSupportVO); // Store the row in the list
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
	
	
	// 查詢過去1分鐘更新狀態，且須發送通知的「廠商停權(經檢舉)(seller)」名單
	public List<CustomerSupportVO> getReportedVendorList(){
		
		List<CustomerSupportVO> list = new ArrayList<CustomerSupportVO>();
		CustomerSupportVO customerSupportVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_REPORTED_VENDOR_LIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				customerSupportVO = new CustomerSupportVO();

				customerSupportVO.setReferenceId(rs.getInt("reference_id"));

				list.add(customerSupportVO); // Store the row in the list
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
