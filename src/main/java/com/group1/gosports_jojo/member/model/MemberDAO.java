package com.group1.gosports_jojo.member.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class MemberDAO implements MemberDAO_interface {

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
	    public MemberDAO(DataSource dataSource) {
	        this.ds = dataSource;
	    }
	
	
	private static final String INSERT_STMT = "INSERT INTO member_lists (group_id,user_id"
			+ ",member_role,updated_datetime)"
			+ "VALUES (?, ?, '報名', curtime())";
	
	private static final String INSERT_LEADER = "INSERT INTO member_lists (group_id,user_id"
			+ ",member_role,updated_datetime)"
			+ "VALUES (?, ?, '團長', curtime())";
	
	
	private static final String UPDATE_HIDDEN = "UPDATE member_lists set member_role='已退出',"
			+ "updated_datetime=curtime() where group_id = ? and  user_id=?";
	

	
	private static final String GET_TEAMMEMBER = "SELECT users.username as username,mem.user_id as user_id,member_role, updated_datetime, present_log, group_id, member_list_id, "
			+ "	 rank() over (partition by member_role order by updated_datetime) as rank_no "
			+ "	 FROM member_lists  mem left join users "
			+ "	on mem.user_id  = users.user_id where group_id = ? and member_role != '已退出' "
			+ "	 order by updated_datetime";
	
	
	 
			
//			"SELECT user_id,member_role FROM member_lists where group_id=? and member_role != '已退出' order by updated_datetime";
	
	private static final String QUERY_CURRENT_TEAM = "SELECT ml.group_id, member_role, present_log, group_name, "
			+ "group_status_desc, group_type, group_city, group_address, group_playing_datetime, "
			+ "group_primary_member, group_show "
			+ "from member_lists ml left join group_lists  gl on ml.group_id = gl.group_id where user_id = ? "
			+ " and (member_role in ('團長' , '報名'))"
			+ "and (current_time() <=group_playing_datetime)"
			+ "and group_show = 'YES' order by group_playing_datetime";
	
	private static final String QUERY_HISTORY_TEAM =  "SELECT ml.group_id, member_role, present_log, group_name, "
			+ "group_status_desc, group_type, group_city, group_address, group_playing_datetime, "
			+ "group_primary_member, group_show "
			+ "from member_lists ml left join group_lists  gl on gl.group_id  = ml.group_id where user_id = ? "
			+ " and (member_role in ('團長' , '報名'))"
			+ "and (current_time() > group_playing_datetime)"
			+ "and group_show  = 'YES' "
			+ "and group_playing_datetime>= DATE_SUB(now(),INTERVAL 2 MONTH)"
			+ "order by group_playing_datetime desc";
	
	private static final String QUERY_PRESENT_YES ="select count(*) count FROM member_lists "
			+ "where user_id = ? AND updated_datetime >= DATE_SUB(now(),INTERVAL 2 month) "
			+ "and present_log = 'YES' and member_role != '已退出'";
	
	private static final String QUERY_PRESENT_NO ="select count(*) count FROM member_lists "
			+ "where user_id = ? AND updated_datetime >= DATE_SUB(now(),INTERVAL 2 month) "
			+ "and present_log = 'NO'";
	
	
	private static final String COUNT_LEADER_TIMES ="SELECT  count(*) count, group_show "
			+ "from member_lists ml left join group_lists  gl on gl.group_id  = ml.group_id where user_id = ? "
			+ "and (member_role in ('團長'))  and group_show  = 'YES'";
	
	
	private static final String COUNT_LEADER_NO =" SELECT  count(*) count, group_show "
			+ "from member_lists ml left join group_lists  gl on gl.group_id  = ml.group_id where user_id = ? "
			+ " and (member_role in ('團長'))  and group_show  = 'NO' ";
	
	
	private static final String CHANGE_PRESENT_LOG ="UPDATE member_lists set present_log=? "
			+ "where group_id = ? and  user_id= ? and member_role = '報名'";
	
	
	 //團員加入參團
	@Override
	public void insert(MemberVO memberVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);
			
			pstmt.setInt(1, memberVO.getGroupId());
			pstmt.setInt(2, memberVO.getUserId());
			
			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	
	//團長開團
	@Override
	public void insertLeader(MemberVO memberVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_LEADER);

			
			pstmt.setInt(1, memberVO.getGroupId());
			pstmt.setInt(2, memberVO.getUserId());
			
			pstmt.executeUpdate();

			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	
	 //退出揪團
	@Override
	public void updateHidden(Integer groupId, Integer userId) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
		
			pstmt = con.prepareStatement(UPDATE_HIDDEN);
			
			pstmt.setInt(1, groupId);
			pstmt.setInt(2, userId);
			
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	
	//顯示group_join畫面，下方團員名單
	@Override
	public List<MemberVO> findByPrimaryKey(Integer groupId) {
		
		List<MemberVO> list = new ArrayList<MemberVO>();
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_TEAMMEMBER);

			pstmt.setInt(1, groupId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
			
				memberVO = new MemberVO();
				memberVO.setUserId(rs.getInt("user_id"));
				memberVO.setMemberRole(rs.getString("member_role"));
				memberVO.setRankNo(rs.getInt("rank_no"));
				memberVO.setPresentLog(rs.getString("present_log"));
				memberVO.setGroupId(rs.getInt("group_id"));
				memberVO.setMemberListId(rs.getInt("member_list_id"));
				memberVO.setUpdatedDatetime(rs.getTimestamp("updated_datetime"));
				
				memberVO.setUsername(rs.getString("username"));
				
				
				list.add(memberVO);
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
		return list;
		
	}

	
	//顯示group_histoty畫面，現在參團清單
	@Override
	public List<MemberVO> queryCurrentTeam(Integer userId) {
		
		List<MemberVO> list = new ArrayList<MemberVO>();
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(QUERY_CURRENT_TEAM);

			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				memberVO = new MemberVO();
				memberVO.setMemberRole(rs.getString("member_role"));
				memberVO.setGroupId(rs.getInt("group_id"));
				memberVO.setPresentLog(rs.getString("present_log"));
				memberVO.setGroupName(rs.getString("group_name"));
				memberVO.setGroupStatusDesc(rs.getString("group_status_desc"));
				memberVO.setGroupType(rs.getString("group_type"));
				memberVO.setGroupCity(rs.getString("group_city"));
				memberVO.setGroupAddress(rs.getString("group_address"));
				memberVO.setGroupPlayingDatetime(rs.getTimestamp("group_playing_datetime"));
				memberVO.setGroupPrimaryMember(rs.getInt("group_primary_member"));
				memberVO.setGroupShow(rs.getString("group_show"));
				
				list.add(memberVO);
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
		return list;
		
	
	}


	//顯示group_histoty畫面，過去參團清單
	@Override
	public List<MemberVO> queryHistoryTeam(Integer userId) {
		
		List<MemberVO> list = new ArrayList<MemberVO>();
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(QUERY_HISTORY_TEAM);

			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				memberVO = new MemberVO();
				memberVO.setMemberRole(rs.getString("member_role"));
				memberVO.setGroupId(rs.getInt("group_id"));
				memberVO.setPresentLog(rs.getString("present_log"));
				memberVO.setGroupName(rs.getString("group_name"));
				memberVO.setGroupStatusDesc(rs.getString("group_status_desc"));
				memberVO.setGroupType(rs.getString("group_type"));
				memberVO.setGroupCity(rs.getString("group_city"));
				memberVO.setGroupAddress(rs.getString("group_address"));
				memberVO.setGroupPlayingDatetime(rs.getTimestamp("group_playing_datetime"));
				memberVO.setGroupPrimaryMember(rs.getInt("group_primary_member"));
				memberVO.setGroupShow(rs.getString("group_show"));
				
				list.add(memberVO);
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
		return list;
		
	}

	
	
	
	@Override
	public MemberVO queryPresentYes(Integer userId) {
		
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(QUERY_PRESENT_YES);

			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				memberVO = new MemberVO();
				
				memberVO.setCount(rs.getInt("count"));
				
				
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
		return memberVO;
		
	}


	
	@Override
	public MemberVO queryPresentNo(Integer userId) {
			

		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(QUERY_PRESENT_NO);

			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				memberVO = new MemberVO();
				
				memberVO.setCount(rs.getInt("count"));
				
				
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
		return memberVO;

		
		
	}

	
	
	
	@Override
	public MemberVO countLeaderTimes(Integer userId) {
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(COUNT_LEADER_TIMES);

			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				memberVO = new MemberVO();
				
				memberVO.setCount(rs.getInt("count"));
				
				
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
		return memberVO;
	}
	
	
	
	
	
	

	@Override
	public MemberVO countLeaderNo(Integer userId) {
		
		MemberVO memberVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(COUNT_LEADER_NO);

			pstmt.setInt(1, userId);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				memberVO = new MemberVO();
				
				memberVO.setCount(rs.getInt("count"));
				
				
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
		return memberVO;

		
	}

	
	
	@Override
	public void changePresentLog(String presentLog, Integer groupId, Integer userId) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
		
			pstmt = con.prepareStatement(CHANGE_PRESENT_LOG);
			
			pstmt.setString(1, presentLog);
			pstmt.setInt(2, groupId);
			pstmt.setInt(3, userId);
			
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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