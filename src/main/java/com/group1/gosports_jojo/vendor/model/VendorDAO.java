package com.group1.gosports_jojo.vendor.model;

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
public class VendorDAO implements VendorDAO_interface {
	
    private final DataSource ds;
	
    @Autowired
    public VendorDAO(DataSource ds) {
        this.ds = ds;
    }
	
	//查詢單一廠商圖檔
	private static final String GET_ONE_VENDOR_IMAGE = "SELECT vendor_id, registration_document FROM vendors WHERE vendor_id = ?";
	
	
	//查詢所有廠商
	private static final String GET_ALL_VENDOR = "SELECT vendor_id, username, email, company_name, company_address, company_phone, "
			+ "company_email, registration_document, shop_name, unified_business_number, status, updated_at "
			+ "FROM vendors";
	
	
	//查詢近1分鐘，新註冊的廠商名單 (須到CustomerSupportDao加入審核名單 & 須到NotificationDao加入「17.廠商資格審核(seller)-1」通知)
	private static final String GET_NEW_VENDOR = "SELECT vendor_id FROM vendors WHERE enabled=1 AND status = 0 AND DATE_SUB(now(),INTERVAL 2 minute) <= created_at AND created_at < now()";
	
	
	//查詢審核狀態於3天前更新為2的廠商名單
	private static final String GET_NOT_PASS_VENDOR = "SELECT vendor_id FROM vendors WHERE status = 2 AND updated_at <= DATE_SUB(now(),INTERVAL 3 day)";
	
	
	//查詢審核狀態為0(未審核)或2(審核為通過)的廠商
	private static final String GET_PENDING_VENDOR = "SELECT vendor_id, username, email, company_name, company_address, company_phone, "
			+ "company_email, registration_document, shop_name, unified_business_number, status, updated_at "
			+ "FROM vendors WHERE enabled=1 AND status IN ('0', '2')";

	
	//廠商審核通過
	private static final String UPDATE_PASS = "UPDATE vendors set status='1', updated_at=curtime() where vendor_id = ?";
	
	
	//廠商審核未通過
	private static final String UPDATE_HIDDEN = "UPDATE vendors set status='2', updated_at=curtime() where vendor_id = ?";
	
	
	//設定廠商停權(使enabled=0)
	private static final String UPDATE_ENABLED_0 = "UPDATE vendors set enabled=0, updated_at=curtime() where vendor_id =?  ";
	
	
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------

	
	
	
	//查詢單一廠商圖檔
	public VendorVO getOneVendorImage(Integer vendorId) {
		VendorVO vendorVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_VENDOR_IMAGE);
			pstmt.setInt(1, vendorId);
			
			rs = pstmt.executeQuery();

			while (rs.next()) {
				vendorVO = new VendorVO();
				vendorVO.setVendorId(rs.getInt("vendor_id"));
				vendorVO.setRegistrationDocument(rs.getBytes("registration_document"));
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
		return vendorVO;
		
	}
	
	
	//查詢所有廠商
	public List<VendorVO> getAllVendor(){
		List<VendorVO> list = new ArrayList<VendorVO>();
		VendorVO vendorVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_VENDOR);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				vendorVO = new VendorVO();
				
				vendorVO.setVendorId(rs.getInt("vendor_id"));
				vendorVO.setUsername(rs.getString("username"));
				vendorVO.setEmail(rs.getString("email"));
				vendorVO.setCompanyName(rs.getString("company_name"));
				vendorVO.setCompanyAddress(rs.getString("company_address"));
				vendorVO.setCompanyPhone(rs.getString("company_phone"));
				vendorVO.setCompanyEmail(rs.getString("company_email"));
				vendorVO.setRegistrationDocument(rs.getBytes("registration_document"));
				vendorVO.setShopName(rs.getString("shop_name"));
				vendorVO.setUnifiedBusinessNumber(rs.getString("unified_business_number"));
				vendorVO.setStatus(rs.getShort("status"));
				vendorVO.setUpdatedAt(rs.getTimestamp("updated_at"));

				list.add(vendorVO); // Store the row in the list

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
	
	//查詢近1分鐘，新註冊的廠商名單
	public List<VendorVO> getNewVendor(){
		List<VendorVO> list = new ArrayList<VendorVO>();
		VendorVO vendorVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_NEW_VENDOR);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				vendorVO = new VendorVO();
				
				vendorVO.setVendorId(rs.getInt("vendor_id"));
				
				list.add(vendorVO); // Store the row in the list
				
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
	
	//查詢審核狀態於3天前更新為2的廠商名單
	public List<VendorVO> getNotPassVendor(){
		List<VendorVO> list = new ArrayList<VendorVO>();
		VendorVO vendorVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_NOT_PASS_VENDOR);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				vendorVO = new VendorVO();
				
				vendorVO.setVendorId(rs.getInt("vendor_id"));
				
				list.add(vendorVO); // Store the row in the list
				
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
	
	
	//查詢審核狀態為0(未審核)或2(審核為通過)的廠商
	public List<VendorVO> getPendingVendor(){
		List<VendorVO> list = new ArrayList<VendorVO>();
		VendorVO vendorVO = null;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_PENDING_VENDOR);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				vendorVO = new VendorVO();
				
				vendorVO.setVendorId(rs.getInt("vendor_id"));
				vendorVO.setUsername(rs.getString("username"));
				vendorVO.setEmail(rs.getString("email"));
				vendorVO.setCompanyName(rs.getString("company_name"));
				vendorVO.setCompanyAddress(rs.getString("company_address"));
				vendorVO.setCompanyPhone(rs.getString("company_phone"));
				vendorVO.setCompanyEmail(rs.getString("company_email"));
				vendorVO.setRegistrationDocument(rs.getBytes("registration_document"));
				vendorVO.setShopName(rs.getString("shop_name"));
				vendorVO.setUnifiedBusinessNumber(rs.getString("unified_business_number"));
				vendorVO.setStatus(rs.getShort("status"));
				vendorVO.setUpdatedAt(rs.getTimestamp("updated_at"));
				
				list.add(vendorVO); // Store the row in the list
				
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

	
	// 更新審核狀態，審核通過(改為1)
	public void updatePass(Integer vendorId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE_PASS);

			pstmt.setInt(1, vendorId);

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

	
	// 更新審核狀態，審核未通過(改為2)
	public void updateHidden(Integer vendorId) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();

			pstmt = con.prepareStatement(UPDATE_HIDDEN);

			pstmt.setInt(1, vendorId);

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

	
	//設定廠商停權(使enabled=0)
	public void updateEnabled0(Integer vendorId) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(UPDATE_ENABLED_0);
			pstmt.setInt(1, vendorId);
			
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
