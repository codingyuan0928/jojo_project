package com.group1.gosports_jojo.member.model;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

	@Autowired
	private MemberDAO_interface dao;

	public MemberService() {
//		dao = new EmpJDBCDAO();
//		dao = new MemberDAO();
	}

	//servlet回傳 groupId userId 引數,為了 insert, 使用dao的insert(memberVO)方法把成員新增
	//並且回傳Mdao.findByPrimaryKey(groupId),list到servlet
	public List<MemberVO> insert(Integer groupId,Integer userId) {

		MemberVO memberVO = new MemberVO();

		memberVO.setGroupId(groupId);
		memberVO.setUserId(userId);
		
		dao.insert(memberVO);

		return dao.findByPrimaryKey(groupId);
	}

	//for團長開團
	public List<MemberVO> insertLeader(Integer groupId,Integer userId) {

		MemberVO memberVO = new MemberVO();

		memberVO.setGroupId(groupId);
		memberVO.setUserId(userId);
		
		dao.insertLeader(memberVO);

		return dao.findByPrimaryKey(groupId);
	}



	//servlet回傳groupId ,userId 使用dao的updateHidden方法把成員隱藏,並且使用dao.updateHidden(groupId,userId)回傳所有list到servlet
	public List<MemberVO> updateHidden(Integer groupId, Integer userId) {

		MemberVO memberVO = new MemberVO();

		memberVO.setGroupId(groupId);
		memberVO.setUserId(userId);
		
		dao.updateHidden(groupId,userId);

		return dao.findByPrimaryKey(groupId);
	}


	public List<MemberVO> findByPrimaryKey(Integer groupId) {
		return dao.findByPrimaryKey(groupId);
	}
	
	public List<MemberVO> queryCurrentTeam(Integer userId) {
		return dao.queryCurrentTeam(userId);
	}
	
	public List<MemberVO> queryHistoryTeam(Integer userId) {
		return dao.queryHistoryTeam(userId);
	}
	
	public MemberVO queryPresentYes(Integer userId) {
		return dao.queryPresentYes(userId);
	}
	
	public MemberVO queryPresentNo(Integer userId) {
		return dao.queryPresentNo(userId);
	}
	
	public MemberVO countLeaderTimes(Integer userId) {
		return dao.countLeaderTimes(userId);
	}
	
	public MemberVO countLeaderNo(Integer userId) {
		return dao.countLeaderNo(userId);
	}
	
	
	
	public List<MemberVO> changePresentLog(String presentLog,Integer groupId, Integer userId) {

		MemberVO memberVO = new MemberVO();

		memberVO.setPresentLog(presentLog);
		memberVO.setGroupId(groupId);
		memberVO.setUserId(userId);
		
		dao.changePresentLog(presentLog,groupId,userId);

		return dao.findByPrimaryKey(groupId);
	}
	
	

	
}
