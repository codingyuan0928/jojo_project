package com.group1.gosports_jojo.group.model;

import java.util.*;
import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class GroupDAO implements GroupDAO_interface {

//	private static DataSource ds = null;
//	static {
//		try {
//			Context ctx = new InitialContext();
//			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/Gosport");
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
//	}
//	
	
	
	
	 private final DataSource ds;

	    @Autowired
	    public GroupDAO(DataSource dataSource) {
	        this.ds = dataSource;
	    }

	
	
	
	

	private static final String INSERT_STMT = "INSERT INTO group_lists (group_leader_id,group_name"
			+ ",group_type,group_city,group_address,group_playing_datetime,group_join_deadline"
			+ ",group_primary_member,secondary_member,group_pic,group_note)"
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?)";
	
	private static final String UPDATE = "UPDATE group_lists set group_name=?, group_address=?"
			+ ", group_playing_datetime=?, group_join_deadline=?, group_primary_member=?,"
			+ " secondary_member=0, group_pic=?,group_note=?,group_updated_datetime=curtime() where group_id  = ?";
	
	private static final String UPDATE_HIDDEN = "UPDATE group_lists set group_show='NO',"
			+ "group_updated_datetime=curtime() where group_id = ?";
	
	
	private static final String GET_ONE_TEAM = "SELECT group_id,group_leader_id,group_name,group_status_desc,"
			+ "group_type,group_city,group_address,group_playing_datetime,group_join_deadline,"
			+ "group_primary_member,secondary_member,group_pic,group_note FROM group_lists where group_id=?";
	  //點選某團後跳轉頁面
	
	private static final String GET_ONE_TEAM_FROM_LEADERID = "SELECT group_id,group_leader_id,group_name,group_status_desc,"
			+ "group_type,group_city,group_address,group_playing_datetime,group_join_deadline,"
			+ "group_primary_member,secondary_member,group_pic,group_note FROM group_lists where group_leader_id=? "
			+ "order by group_updated_datetime desc limit 1 ";
	  //團長開團後後跳轉頁面
	
	
	private static final String GET_ALL_TEAM = "SELECT group_id,group_leader_id,group_name,"
			+ "group_status_desc,group_type,group_city,group_address,group_playing_datetime,"
			+ "group_join_deadline,group_primary_member,secondary_member "
			+ "FROM group_lists where group_show='YES'"
			+ "and group_join_deadline >=current_time() order by group_playing_datetime";
	//第一次進到揪團網時顯示所有運動及縣市頁面
	
	
	private static final String GET_CITY_SPORT = "SELECT group_id,group_leader_id,group_name,group_status_desc,group_address"
			+ ",group_playing_datetime,group_primary_member,group_type,group_city,secondary_member FROM group_lists "
			+ "where group_type=? and group_city=? and group_show='YES'"
			+ "and group_join_deadline >=current_time() order by group_playing_datetime";
	//選擇球類及縣市後查詢頁面
	
	
	private static final String CHANGE_DEADLINE_STATUS = "update group_lists  set group_status_desc = '報名截止'"
			+ " where curtime() > group_join_deadline and group_status_desc = '揪團中'";
			//現在時間大於揪團截止時間時將group_status_desc 改成 '報名截止'

	private static final String CHANGE_NOBODY_JOIN = "update group_lists set group_show = 'NO' "
			+ ",group_updated_datetime = curtime() where  curtime() > group_join_deadline "
			+ "and group_show = 'YES' and group_id  IN (select group_id  from member_lists "
			+ "where member_role != '已退出' group by group_id having count(user_id) = 1 )" ;
		//現在時間大於揪團截止時間時且無人參團,將 group_show 改成 'NO'
	
	private static final String SERCH_GROUP_NAME = "SELECT group_id,group_leader_id,group_name,group_status_desc,group_address"
			+ ",group_playing_datetime,group_primary_member,group_type,group_city,secondary_member FROM group_lists "
			+ "where group_name like '%' ? '%' OR group_address like '%' ? '%' OR group_note like '%' ? '%'"
			+ "and group_join_deadline >=current_time() and group_show='YES' order by group_playing_datetime";
	//選擇球類及縣市後查詢頁面serchGroupName

	//////////////////////////////////    成團通知    //////////////////////////////////

	// 查詢近1分鐘group_join_deadline，狀態為報名截止的group_id，查詢member_list
	private static final String GET_GROUP_SUCCESS_MEMBERLIST = "SELECT g2.group_id as group_id, g2.group_name as group_name, user_id "
			+ "FROM group_lists g1, "
			+ "(SELECT member_lists.group_id, member_role, user_id, group_name,  rank() over (partition by group_id, member_role order by updated_datetime) as rank_no FROM member_lists, group_lists WHERE member_lists.group_id = group_lists.group_id "
			+ "AND member_lists.group_id IN (SELECT group_id FROM group_lists WHERE group_status_desc = '報名截止' AND group_show = 'YES' AND DATE_SUB(now(),INTERVAL 1 minute) <= group_join_deadline AND group_join_deadline < now()) "
			+ "AND member_role IN ('團長', '報名')) g2 " + "WHERE g1.group_id = g2.group_id "
			+ "AND g2.rank_no <= g1.group_primary_member";

//////////////////////////////////    候補失敗通知 (與成團通知同時發送)    //////////////////////////////////

	// 查詢狀態為成團，且近1分鐘group_join_deadline，查詢候補的member_list
	private static final String GET_GROUP_SECONDARY_MEMBERLIST = "SELECT g2.group_id as group_id, g2.group_name as group_name, user_id "
			+ "FROM group_lists g1, "
			+ "(SELECT member_lists.group_id, member_role, user_id, group_name,  rank() over (partition by group_id, member_role order by updated_datetime) as rank_no "
			+ "FROM member_lists, group_lists " + "WHERE member_lists.group_id = group_lists.group_id "
			+ "AND member_lists.group_id IN (SELECT group_id FROM group_lists WHERE group_status_desc = '報名截止' AND group_show = 'YES' AND DATE_SUB(now(),INTERVAL 1 minute) <= group_join_deadline AND group_join_deadline < now()) "
			+ "AND member_role IN ('報名')) g2 " + "WHERE g1.group_id = g2.group_id "
			+ "AND g2.rank_no > g1.group_primary_member";

//////////////////////////////////    流團(取消揪團)通知    //////////////////////////////////

	// 查詢近1分鐘group_updated_datetime，狀態為流團(取消揪團)group_show =
	// 'NO'的group_id，查詢member_list
	private static final String GET_GROUP_CANCEL_MEMBERLIST = "SELECT member_lists.group_id, group_name, user_id FROM member_lists, group_lists "
			+ "WHERE member_lists.group_id = group_lists.group_id "
			+ "AND member_lists.group_id IN (SELECT group_id FROM group_lists WHERE group_show = 'NO' AND DATE_SUB(now(),INTERVAL 1 minute) <= group_updated_datetime AND group_updated_datetime < now()) "
			+ "AND member_role IN ('團長', '報名')";

//////////////////////////////////    活動提醒通知    //////////////////////////////////

	// 查詢狀態為報名截止，且now() + 6hour <= group_playing_datetime < now() +
	// 6.5hour的group_id，查詢團長及正取member_list
	private static final String GET_GROUP_START_MEMBERLIST = "SELECT g2.group_id as group_id, g2.group_name as group_name, user_id "
			+ "FROM group_lists g1, "
			+ "(SELECT member_lists.group_id, member_role, user_id, group_name,  rank() over (partition by group_id, member_role order by updated_datetime) as rank_no "
			+ "FROM member_lists, group_lists " + "WHERE member_lists.group_id = group_lists.group_id "
			+ "AND member_lists.group_id IN (SELECT group_id FROM group_lists WHERE group_status_desc = '報名截止' AND group_show = 'YES' AND DATE_ADD(now(),INTERVAL 360 minute) <= group_playing_datetime AND group_playing_datetime < DATE_ADD(now(),INTERVAL 361 minute)) "
			+ "AND member_role IN ('團長', '報名')) g2 " + "WHERE g1.group_id = g2.group_id "
			+ "AND g2.rank_no <= g1.group_primary_member";

//////////////////////////////////    提醒團長回覆團員出缺席通知    //////////////////////////////////

	// 查詢狀態為報名截止，且now() - 1 minute <= group_playing_datetime < now()
	// 的group_id，查詢團長名單
	private static final String GET_GROUP_PRESENT_REPLY_LEADER = "SELECT g2.group_id as group_id, g2.group_name as group_name, user_id "
			+ "FROM group_lists g1, "
			+ "(SELECT member_lists.group_id, member_role, user_id, group_name,  rank() over (partition by group_id, member_role order by updated_datetime) as rank_no "
			+ "FROM member_lists, group_lists " + "WHERE member_lists.group_id = group_lists.group_id "
			+ "AND member_lists.group_id IN (SELECT group_id FROM group_lists WHERE group_status_desc = '報名截止' AND group_show = 'YES' AND DATE_SUB(now(),INTERVAL 1 minute) <= group_playing_datetime AND group_playing_datetime < now()"
			+ "AND member_role IN ('團長'))) g2 " + "WHERE g1.group_id = g2.group_id";

//////////////////////////////////查證檢舉揪團    //////////////////////////////////

	// 查證檢舉揪團
	private static final String GET_GROUP_BY_KEYWORD = "SELECT group_id, group_name, group_note, group_status_desc, group_type, group_city, group_address, "
			+ "group_playing_datetime, group_join_deadline, group_show "
			+ "FROM group_lists WHERE group_name like '%' ? '%' OR group_address like '%' ? '%' OR group_note like '%' ? '%' ";

///////////////////////////////////////////////////////////////////////////////////





	@Override
	public void insert(GroupVO groupVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(INSERT_STMT);

			
			pstmt.setInt(1, groupVO.getGroupLeaderId());
			pstmt.setString(2, groupVO.getGroupName());
			pstmt.setString(3, groupVO.getGroupType());
			pstmt.setString(4, groupVO.getGroupCity());
			pstmt.setString(5, groupVO.getGroupAddress());
			pstmt.setTimestamp(6, groupVO.getGroupPlayingDatetime());
			pstmt.setTimestamp(7, groupVO.getGroupJoinDeadline());
			pstmt.setInt(8, groupVO.getGroupPrimaryMember());
//			pstmt.setInt(9, groupVO.getSecondaryMember());
			pstmt.setBytes(9, groupVO.getGroupPic());
			pstmt.setString(10, groupVO.getGroupNote());

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
	

	@Override
	public void updateAll(GroupVO groupVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, groupVO.getGroupName());
			pstmt.setString(2, groupVO.getGroupAddress());
			pstmt.setTimestamp(3, groupVO.getGroupPlayingDatetime());
			pstmt.setTimestamp(4, groupVO.getGroupJoinDeadline());
			pstmt.setInt(5, groupVO.getGroupPrimaryMember());
//			pstmt.setInt(6, groupVO.getSecondaryMember());
			pstmt.setBytes(6, groupVO.getGroupPic());
			pstmt.setString(7, groupVO.getGroupNote());
			pstmt.setInt(8, groupVO.getGroupId());
			
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

	@Override
	public void updateHidden(Integer groupId) {

		
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
		
			pstmt = con.prepareStatement(UPDATE_HIDDEN);
			
			pstmt.setInt(1, groupId);
			
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

	@Override
	public GroupVO findByPrimaryKey(Integer groupId) {
//		return null;
		
		
		
		GroupVO groupVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ONE_TEAM);

			pstmt.setInt(1, groupId);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo �]�٬� Domain objects
				groupVO = new GroupVO();
				
				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setGroupLeaderId(rs.getInt("group_leader_id"));
				groupVO.setGroupName(rs.getString("group_name"));
				groupVO.setGroupStatusDesc(rs.getString("group_status_desc"));
				groupVO.setGroupType(rs.getString("group_type"));
				groupVO.setGroupCity(rs.getString("group_city"));
				groupVO.setGroupAddress(rs.getString("group_address"));
				groupVO.setGroupPlayingDatetime(rs.getTimestamp("group_playing_datetime"));
				groupVO.setGroupJoinDeadline(rs.getTimestamp("group_join_deadline"));
				groupVO.setGroupPrimaryMember(rs.getInt("group_primary_member"));
				groupVO.setSecondaryMember(rs.getInt("secondary_member"));
				groupVO.setGroupPic(rs.getBytes("group_pic"));
				groupVO.setGroupNote(rs.getString("group_note"));
				
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
		return groupVO;
		
		
		
		
		
		
	}

	@Override
	public List<GroupVO> getAll() {
		
		
		
		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_ALL_TEAM);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVO 也稱為 Domain objects
				groupVO = new GroupVO();
				
				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setGroupLeaderId(rs.getInt("group_leader_id"));
				groupVO.setGroupName(rs.getString("group_name"));
				groupVO.setGroupStatusDesc(rs.getString("group_status_desc"));
				groupVO.setGroupType(rs.getString("group_type"));
				groupVO.setGroupCity(rs.getString("group_city"));
				groupVO.setGroupAddress(rs.getString("group_address"));
				groupVO.setGroupPlayingDatetime(rs.getTimestamp("group_playing_datetime"));
				groupVO.setGroupJoinDeadline(rs.getTimestamp("group_join_deadline"));
				groupVO.setGroupPrimaryMember(rs.getInt("group_primary_member"));
				groupVO.setSecondaryMember(rs.getInt("secondary_member"));
							
				list.add(groupVO); // Store the row in the list
				
				
				
				
				
				
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

	
	
	public GroupVO findByGroupLeadId(Integer groupLeaderId) {

			GroupVO groupVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {

				con = ds.getConnection();
				pstmt = con.prepareStatement(GET_ONE_TEAM_FROM_LEADERID);

				pstmt.setInt(1, groupLeaderId);

				rs = pstmt.executeQuery();

				while (rs.next()) {

					groupVO = new GroupVO();
					
					groupVO.setGroupId(rs.getInt("group_id"));
					groupVO.setGroupLeaderId(rs.getInt("group_leader_id"));
					groupVO.setGroupName(rs.getString("group_name"));
					groupVO.setGroupStatusDesc(rs.getString("group_status_desc"));
					groupVO.setGroupType(rs.getString("group_type"));
					groupVO.setGroupCity(rs.getString("group_city"));
					groupVO.setGroupAddress(rs.getString("group_address"));
					groupVO.setGroupPlayingDatetime(rs.getTimestamp("group_playing_datetime"));
					groupVO.setGroupJoinDeadline(rs.getTimestamp("group_join_deadline"));
					groupVO.setGroupPrimaryMember(rs.getInt("group_primary_member"));
					groupVO.setSecondaryMember(rs.getInt("secondary_member"));
					groupVO.setGroupPic(rs.getBytes("group_pic"));
					groupVO.setGroupNote(rs.getString("group_note"));
					
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
			return groupVO;
			
	  }
	
	
	
	@Override
	public List<GroupVO> getCitySport(String groupType, String groupCity) {
		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_CITY_SPORT);

			pstmt.setString(1, groupType);
			pstmt.setString(2, groupCity);

			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// empVo 也稱為 Domain objects
				groupVO = new GroupVO();
				
				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setGroupLeaderId(rs.getInt("group_leader_id"));
				groupVO.setGroupName(rs.getString("group_name"));
				groupVO.setGroupType(rs.getString("group_type"));
				groupVO.setGroupCity(rs.getString("group_city"));
				groupVO.setGroupStatusDesc(rs.getString("group_status_desc"));
				groupVO.setGroupAddress(rs.getString("group_address"));
				groupVO.setGroupPlayingDatetime(rs.getTimestamp("group_playing_datetime"));
				groupVO.setGroupPrimaryMember(rs.getInt("group_primary_member"));
				groupVO.setSecondaryMember(rs.getInt("secondary_member"));
				
				list.add(groupVO); // Store the row in the list
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
	public void changeDeadlineStatus() {
		
		
		
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
		
			pstmt = con.prepareStatement(CHANGE_DEADLINE_STATUS);
			
			
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



	
	@Override
	public void changeNobodyJoin() {
		
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			con = ds.getConnection();
		
			pstmt = con.prepareStatement(CHANGE_NOBODY_JOIN);
			
			
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


	@Override
	public List<GroupVO> serchGroupName(String serchGroupName1, String serchGroupName2, String serchGroupName3) {
		
		
		
		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;
	
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(SERCH_GROUP_NAME);

			pstmt.setString(1, serchGroupName1);
			pstmt.setString(2, serchGroupName2);
			pstmt.setString(3, serchGroupName3);


			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				groupVO = new GroupVO();

				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setGroupLeaderId(rs.getInt("group_leader_id"));
				groupVO.setGroupName(rs.getString("group_name"));
				groupVO.setGroupType(rs.getString("group_type"));
				groupVO.setGroupCity(rs.getString("group_city"));
				groupVO.setGroupStatusDesc(rs.getString("group_status_desc"));
				groupVO.setGroupAddress(rs.getString("group_address"));
				groupVO.setGroupPlayingDatetime(rs.getTimestamp("group_playing_datetime"));
				groupVO.setGroupPrimaryMember(rs.getInt("group_primary_member"));
				groupVO.setSecondaryMember(rs.getInt("secondary_member"));
				
				list.add(groupVO); // Store the row in the list
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



//////////////////////////////////    成團通知    //////////////////////////////////

	//	查詢近1分鐘，狀態為成團的group_id
	@Override
	public List<GroupVO> getGroupSuccessMemberlist() {

		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_GROUP_SUCCESS_MEMBERLIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// empVo 也稱為 Domain objects
				groupVO = new GroupVO();

				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setUserId(rs.getInt("user_id"));
				groupVO.setGroupName(rs.getString("group_name"));

				list.add(groupVO); // Store the row in the list
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

//////////////////////////////////    流團(取消揪團)通知    //////////////////////////////////

	//查詢近1分鐘，group_show為No的group_id
	@Override
	public List<GroupVO> getGroupCancelMemberlist() {

		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_GROUP_CANCEL_MEMBERLIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				groupVO = new GroupVO();

				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setUserId(rs.getInt("user_id"));
				groupVO.setGroupName(rs.getString("group_name"));

				list.add(groupVO); // Store the row in the list
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
		return list;

	}

//////////////////////////////////    候補失敗通知    //////////////////////////////////

	//查詢狀態為成團，且now() + 6hour <= group_playing_datetime < now() + 6.5hour 的group_id，查詢member_list
	@Override
	public List<GroupVO> getGroupSecondaryList() {

		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_GROUP_SECONDARY_MEMBERLIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				groupVO = new GroupVO();

				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setUserId(rs.getInt("user_id"));
				groupVO.setGroupName(rs.getString("group_name"));

				list.add(groupVO); // Store the row in the list
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
		return list;

	}

//////////////////////////////////    活動提醒通知    //////////////////////////////////

	// 查詢狀態為成團，且now() + 6hour <= group_playing_datetime < now() + 6.5hour 的group_id，查詢member_list
	@Override
	public List<GroupVO> getGroupStartMemberlist() {

		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_GROUP_START_MEMBERLIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				groupVO = new GroupVO();

				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setUserId(rs.getInt("user_id"));
				groupVO.setGroupName(rs.getString("group_name"));

				list.add(groupVO); // Store the row in the list
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
		return list;

	}

//////////////////////////////    提醒團長回覆團員出缺席通知    /////////////////////////////

	public List<GroupVO> getGroupPresentReplyLeader() {
		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_GROUP_PRESENT_REPLY_LEADER);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				groupVO = new GroupVO();

				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setUserId(rs.getInt("user_id"));
				groupVO.setGroupName(rs.getString("group_name"));

				list.add(groupVO); // Store the row in the list
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
		return list;
	};

//////////////////////////////    移除檢舉揪團通知    /////////////////////////////

	public List<GroupVO> getGroupReport() {
		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_GROUP_SECONDARY_MEMBERLIST);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				groupVO = new GroupVO();

				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setUserId(rs.getInt("user_id"));
				groupVO.setGroupName(rs.getString("group_name"));

				list.add(groupVO); // Store the row in the list
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
		return list;
	};

//////////////////////////////////查證檢舉揪團    //////////////////////////////////

	public List<GroupVO> getGroupByKeyWord(String keyword1, String keyword2, String keyword3) {
		List<GroupVO> list = new ArrayList<GroupVO>();
		GroupVO groupVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(GET_GROUP_BY_KEYWORD);

			pstmt.setString(1, keyword1);
			pstmt.setString(2, keyword2);
			pstmt.setString(3, keyword3);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				groupVO = new GroupVO();

				groupVO.setGroupId(rs.getInt("group_id"));
				groupVO.setGroupName(rs.getString("group_name"));
				groupVO.setGroupNote(rs.getString("group_note"));
				groupVO.setGroupStatusDesc(rs.getString("group_status_desc"));
				groupVO.setGroupType(rs.getString("group_type"));
				groupVO.setGroupCity(rs.getString("group_city"));
				groupVO.setGroupAddress(rs.getString("group_address"));
				groupVO.setGroupPlayingDatetime(rs.getTimestamp("group_playing_datetime"));
				groupVO.setGroupJoinDeadline(rs.getTimestamp("group_join_deadline"));
				groupVO.setGroupShow(rs.getString("group_show"));

				list.add(groupVO); // Store the row in the list

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
		return list;
	}




}