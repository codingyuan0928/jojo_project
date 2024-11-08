package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.PicDAO_interface;
import com.group1.gosports_jojo.model.PicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

@Component
public class PicDAO implements PicDAO_interface {


	private final DataSource ds;


	@Autowired
	public PicDAO(DataSource dataSource){
		this.ds = dataSource;
	}




	private static final String INSERT_STMT = "INSERT INTO posts (pic_id, post_id, post_pic, created_datetime, updated_datetime ) VALUES (?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT post_id, post_pic, created_datetime, updated_datetime FROM posts ORDER BY pic_id";
	private static final String GET_ONE_STMT = "SELECT post_id, post_pic, created_datetime, updated_datetime FROM posts WHERE pic_id = ?";
	private static final String DELETE = "DELETE FROM posts WHERE pic_id = ?";
	private static final String UPDATE = "UPDATE posts SET post_id=?, post_pic=?, updated_datetime = ? WHERE pic_id = ?";

	@Override
	public void insert(PicVO picVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

		
			pstmt.setInt(1, picVO.getPost_id());
			pstmt.setString(2, picVO.getPost_pic());
			pstmt.setTimestamp(3, picVO.getCreated_datetime());

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	@Override
	public void update(PicVO picVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setInt(1, picVO.getPost_id());
			pstmt.setString(2, picVO.getPost_pic());
			pstmt.setTimestamp(3, picVO.getUpdated_datetime());

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	@Override
	public void delete(Integer pic_id) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, pic_id);

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	@Override
	public PicVO findByPrimaryKey(Integer pic_id) {

		PicVO picVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, pic_id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				picVO = new PicVO();
				picVO.setPic_id(rs.getInt("pic_id"));
				picVO.setPost_id(rs.getInt("post_id"));
				picVO.setPost_pic(rs.getString("post_pic"));
				picVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				picVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, rs);
		}
		return picVO;
	}

	@Override
	public List<PicVO> getAll() {
		List<PicVO> list = new ArrayList<PicVO>();
		PicVO picVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				picVO = new PicVO();
				picVO.setPic_id(rs.getInt("pic_id"));
				picVO.setPost_id(rs.getInt("post_id"));
				picVO.setPost_pic(rs.getString("post_pic"));
				picVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				picVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));

				// 必須將 postVO 加入到 list 中
				list.add(picVO);
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, rs);
		}
		return list;
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
