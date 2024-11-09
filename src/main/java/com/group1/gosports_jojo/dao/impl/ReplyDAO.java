package com.group1.gosports_jojo.dao.impl;

import com.group1.gosports_jojo.dao.ReplyDAO_interface;
import com.group1.gosports_jojo.model.ReplyVO;
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
public class ReplyDAO implements ReplyDAO_interface {

	private final DataSource ds;
	@Autowired
	public ReplyDAO(DataSource dataSource){
		this.ds = dataSource;
	}

	//新增留言
	private static final String INSERT_REPLY = "INSERT INTO reply_details (user_id, post_id, reply_content) VALUES (?, ?, ?)";
	
	//單篇留言總數
	private static final String GET_ALL_REPLY_AMOUNT = "SELECT count(reply_id) as count FROM reply_details WHERE post_id = ? and reply_status= 1";
	
	//取得單篇文章的所有留言
	private static final String GET_ONE_POST_REPLY = "SELECT reply_details.user_id, username, post_id, reply_content, reply_status, created_datetime, updated_datetime FROM reply_details LEFT JOIN users ON reply_details.user_id = users.user_id WHERE post_id = ? and reply_status = 1 ORDER by created_datetime desc";
	
	//取得單筆留言
	private static final String GET_ONE_REPLY_UPDATE = "SELECT user_id, post_id, reply_content, reply_status, created_datetime updated_datetime FROM reply_details WHERE reply_id = ?";
	
	//刪除
	private static final String DELETE = "UPDATE reply_details SET reply_status= 0 where reply_id= ?";
	
	//編輯
	private static final String UPDATE = "UPDATE reply_details SET reply_content = ? WHERE reply_id = ?";

	@Override //新增留言
	public void insert(ReplyVO replyVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_REPLY);

			pstmt.setInt(1, replyVO.getUser_id());
			pstmt.setInt(2, replyVO.getPost_id());
			pstmt.setString(3, replyVO.getReply_content());
			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	@Override //單篇留言總數
	public Integer getAllReplyAmount(Integer post_id) {
	  
//	  MemberVO memberVO = null;
	  Connection con = null;
	  PreparedStatement pstmt = null;
	  ResultSet rs = null;
	  Integer count = null;

	  try {

	   con = ds.getConnection();
	   pstmt = con.prepareStatement(GET_ALL_REPLY_AMOUNT);

	   pstmt.setInt(1, post_id);

	   rs = pstmt.executeQuery();

	   while (rs.next()) {

		   count= rs.getInt("count");
	    
	    
	   }

	   // Handle any driver errors
	  } catch (SQLException se) {
	   throw new RuntimeException("A database error occured. "
	     + se.getMessage());
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

	//編輯
	public void update(ReplyVO replyVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);
			pstmt.setString(1, replyVO.getReply_content());
			pstmt.setInt(2, replyVO.getReply_id());

			
			pstmt.executeUpdate();
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}
	
	
	@Override //刪除
	public void delete(Integer reply_id) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, reply_id);

			pstmt.executeUpdate();

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	@Override //取得單篇文章的所有留言
	public List<ReplyVO> getOnePostReply(Integer post_id) {
		List<ReplyVO> list = new ArrayList<ReplyVO>();
		ReplyVO replyVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_POST_REPLY);
			
			pstmt.setInt(1, post_id);
			
			rs = pstmt.executeQuery();
		

			while (rs!=null && rs.next()) {
				replyVO = new ReplyVO();
				replyVO.setUser_id(rs.getInt("user_id"));
				replyVO.setPost_id(rs.getInt("post_id"));
				replyVO.setUsername(rs.getString("username"));
				replyVO.setReply_content(rs.getString("reply_content"));
				replyVO.setReply_status(rs.getInt("reply_status"));
				replyVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				replyVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));

				// 必須將 postVO 加入到 list 中
				list.add(replyVO);
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, rs);
		}
		return list;
	}
	
	//取得單筆留言
	public ReplyVO getOneReplyUpdate(Integer reply_id) {

		ReplyVO replyVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_REPLY_UPDATE);

			pstmt.setInt(1, reply_id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				replyVO = new ReplyVO();
				replyVO.setUser_id(rs.getInt("user_id"));
				replyVO.setPost_id(rs.getInt("post_id"));
				replyVO.setReply_content(rs.getString("reply_content"));
				replyVO.setReply_status(rs.getInt("reply_status"));
				replyVO.setCreated_datetime(rs.getTimestamp("created_datetime"));
				replyVO.setUpdated_datetime(rs.getTimestamp("updated_datetime"));
			}

		} catch (SQLException se) {
			throw new RuntimeException("A database error occurred. " + se.getMessage());
		} finally {
			closeResources(con, pstmt, rs);
		}
		return replyVO;
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
