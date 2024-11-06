package com.group1.gosports_jojo.city.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CityDAO implements CityDAO_interface {

//	private static DataSource ds = null;
//	static {
//		try {
//			Context ctx = new InitialContext();
//			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/Gosport");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	 private final DataSource ds;

	    @Autowired
	    public CityDAO(DataSource dataSource) {
	        this.ds = dataSource;
	    }

	    
	    
	private static final String GET_ALL_STMT = "SELECT city_type  FROM city_list order by city_id";


	@Override
	public List<CityVO> getAll() {
		List<CityVO> list = new ArrayList<CityVO>();
		CityVO cityVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				cityVO = new CityVO();
				cityVO.setCityType(rs.getString("city_type"));
				list.add(cityVO); // Store the row in the list
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

}