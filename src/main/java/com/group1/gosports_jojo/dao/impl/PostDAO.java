package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.PostDAO_interface;
import com.group1.gosports_jojo.model.PostVO;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
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
public class PostDAO implements PostDAO_interface {

	private final DataSource ds;

	@Autowired
	public PostDAO(DataSource dataSource){
		this.ds = dataSource;
	}


	// 新增文章
	private static final String INSERT_STMT = "INSERT INTO posts (user_id, post_title, post_category, post_content, created_datetime) VALUES (?, ?, ?, ?, ?)";
	// 查看全部文章(依時間新>舊)
	//private static final String GET_ALL_STMT_TIME = "SELECT post_id, user_id, post_title, post_category, post_content, created_datetime, updated_datetime FROM post where post_status = 1 ORDER BY created_datetime desc";
	private static final String GET_ALL_STMT_TIME = "SELECT p.post_id, p.user_id, p.post_title, p.post_category, "
			+ " p.post_content, p.created_datetime, p.updated_datetime, COALESCE(count, 0) AS count, "
			+ "COALESCE(good, 0) AS good "
			+ "FROM posts p LEFT JOIN (SELECT post_id, COUNT(*) AS count FROM reply_details WHERE reply_status =1 GROUP BY post_id) rd "
			+ "ON p.post_id = rd.post_id "
			+ "LEFT JOIN (SELECT post_id, COUNT(*) AS good FROM response_details WHERE response_status =1 GROUP BY post_id) r "
			+ " ON p.post_id = r.post_id "
			+ "WHERE p.post_status = 1 "
			+ "ORDER BY p.created_datetime DESC";
	

	// 查看全部文章(依回應多>少)
	private static final String GET_ALL_STMT_REPLY = "SELECT p.post_id, p.user_id, p.post_title, p.post_category, "
			+ " p.post_content, p.created_datetime, p.updated_datetime, COALESCE(count, 0) AS count, "
			+ "COALESCE(good, 0) AS good "
			+ "FROM posts p LEFT JOIN (SELECT post_id, COUNT(*) AS count FROM reply_details WHERE reply_status =1 GROUP BY post_id) rd "
			+ "ON p.post_id = rd.post_id "
			+ "LEFT JOIN (SELECT post_id, COUNT(*) AS good FROM response_details WHERE response_status =1 GROUP BY post_id) r "
			+ " ON p.post_id = r.post_id "
			+ "WHERE p.post_status = 1 "
			+ "ORDER BY count DESC, good DESC, p.created_datetime DESC ";
	// 查看單筆文章
	private static final String GET_ONE_STMT = "SELECT post_id, user_id, post_title, post_category, post_content, created_datetime, updated_datetime FROM postS WHERE post_id = ?";
	// 隱藏文章
	private static final String DELETE = "UPDATE postS SET post_status= 0 where post_id= ?;";
	// 編輯文章
	private static final String UPDATE = "UPDATE postS SET post_title=?, post_category=?, post_content=?, updated_datetime = curtime() WHERE post_id = ?";

    //搜尋欄：關鍵字查詢
    private static final String SEARCH_POST = "SELECT p.post_id, p.user_id, p.post_title, p.post_category, "
			 + "p.post_content, p.created_datetime, p.updated_datetime, COALESCE(count, 0) AS count, "
			+ "COALESCE(good, 0) AS good "
			+ "FROM posts p LEFT JOIN (SELECT post_id, COUNT(*) AS count FROM reply_details WHERE reply_status =1 GROUP BY post_id) rd "
			+ "ON p.post_id = rd.post_id "
			+ "LEFT JOIN (SELECT post_id, COUNT(*) AS good FROM response_details WHERE response_status =1 GROUP BY post_id) r "
			 + "ON p.post_id = r.post_id "
			+ "WHERE p.post_status = 1 AND post_title LIKE concat ('%',?,'%') or post_content LIKE  concat ('%',?,'%') ORDER BY created_datetime DESC ";

	// 搜尋文章(依回應多>少)
	private static final String SEARCH_POST_BY_POP = "SELECT p.post_id, p.user_id, p.post_title, p.post_category, "
			+ " p.post_content, p.created_datetime, p.updated_datetime, COALESCE(count, 0) AS count, "
			+ "COALESCE(good, 0) AS good "
			+ "FROM posts p LEFT JOIN (SELECT post_id, COUNT(*) AS count FROM reply_details WHERE reply_status =1 GROUP BY post_id) rd "
			+ "ON p.post_id = rd.post_id "
			+ "LEFT JOIN (SELECT post_id, COUNT(*) AS good FROM response_details WHERE response_status =1 GROUP BY post_id) r "
			+ " ON p.post_id = r.post_id "
			+ "WHERE p.post_status = 1 AND post_title LIKE concat ('%',?,'%') or post_content LIKE  concat ('%',?,'%') "
			+ "ORDER BY count DESC, good DESC, p.created_datetime DESC ";

	// 新增文章
	@Override
	public void insert(PostVO postVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			// pstmt.setInt(1, postVO.getPost_id());
			pstmt.setInt(1, postVO.getUser_id());
			pstmt.setString(2, postVO.getPost_title());
			pstmt.setString(3, postVO.getPost_category());
			pstmt.setString(4, postVO.getPost_content());
			pstmt.setTimestamp(5, postVO.getCreated_datetime());

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	// 編輯文章
	@Override
	public void update(PostVO postVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, postVO.getPost_title());
			pstmt.setString(2, postVO.getPost_category());
			pstmt.setString(3, postVO.getPost_content());
			pstmt.setInt(4, postVO.getPost_id());

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	// 隱藏文章
	@Override
	public void delete(Integer post_id) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, post_id);

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	// 查看單筆文章
	@Override
	public PostVO findByPrimaryKey(Integer post_id) {

		PostVO postVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, post_id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				postVO = new PostVO();
				postVO.setPost_id(rs.getInt("post_id"));
				postVO.setUser_id(rs.getInt("user_id"));
				postVO.setPost_title(rs.getString("post_title"));
				postVO.setPost_category(rs.getString("post_category"));
				postVO.setPost_content(rs.getString("post_content"));
				postVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				postVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, rs);
		}
		return postVO;
	}

	// 查看全部文章(依時間新>舊)
	@Override
	public List<PostVO> getAll() {
		List<PostVO> list = new ArrayList<PostVO>();
		PostVO postVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT_TIME);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				postVO = new PostVO();
				postVO.setPost_id(rs.getInt("post_id"));
				postVO.setUser_id(rs.getInt("user_id"));
				postVO.setPost_title(rs.getString("post_title"));
				postVO.setPost_category(rs.getString("post_category"));
				postVO.setPost_content(rs.getString("post_content"));
				postVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				postVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));
				postVO.setCount(rs.getInt("count"));
				postVO.setGood(rs.getInt("good"));

				// 必須將 postVO 加入到 list 中
				list.add(postVO);
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, rs);
		}
		return list;
	}

	// 查看全部文章(依留言>按讚>時間)
	@Override
	public List<PostVO> getAll2() {
		List<PostVO> list = new ArrayList<PostVO>();
		PostVO postVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_STMT_REPLY);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				postVO = new PostVO();
				postVO.setPost_id(rs.getInt("post_id"));
				postVO.setUser_id(rs.getInt("user_id"));
				postVO.setPost_title(rs.getString("post_title"));
				postVO.setPost_category(rs.getString("post_category"));
				postVO.setPost_content(rs.getString("post_content"));
				postVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				postVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));
				postVO.setGood(rs.getInt("good"));
				postVO.setCount(rs.getInt("count"));
				// 必須將 postVO 加入到 list 中
				list.add(postVO);
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, rs);
		}
		return list;
	}


	@Override
	public List<PostVO> SEARCH_POST  (String keyword, String keyword2) {
		List<PostVO> list = new ArrayList<PostVO>();
		PostVO postVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(SEARCH_POST);
			pstmt.setString(1,keyword);
			pstmt.setString(2,keyword2);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				postVO = new PostVO();
				postVO.setPost_id(rs.getInt("post_id"));
				postVO.setUser_id(rs.getInt("user_id"));
				postVO.setPost_title(rs.getString("post_title"));
				postVO.setPost_category(rs.getString("post_category"));
				postVO.setPost_content(rs.getString("post_content"));
				postVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				postVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));
				postVO.setCount(rs.getInt("count"));
				postVO.setGood(rs.getInt("good"));
//    proVO.setPicture(rs.getBytes("picture"));
				list.add(postVO);
			}

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
		return list;}

	// 查看全部文章(依留言>按讚>時間)
	@Override
	public List<PostVO> SEARCH_POST_BY_POP(String keyword, String keyword2) {
		List<PostVO> list = new ArrayList<PostVO>();
		PostVO postVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(SEARCH_POST_BY_POP);
			pstmt.setString(1,keyword);
			pstmt.setString(2,keyword2);
			rs = pstmt.executeQuery();


			while (rs.next()) {
				postVO = new PostVO();
				postVO.setPost_id(rs.getInt("post_id"));
				postVO.setUser_id(rs.getInt("user_id"));
				postVO.setPost_title(rs.getString("post_title"));
				postVO.setPost_category(rs.getString("post_category"));
				postVO.setPost_content(rs.getString("post_content"));
				postVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				postVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));
				postVO.setGood(rs.getInt("good"));
				postVO.setCount(rs.getInt("count"));
				// 必須將 postVO 加入到 list 中
				list.add(postVO);
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
