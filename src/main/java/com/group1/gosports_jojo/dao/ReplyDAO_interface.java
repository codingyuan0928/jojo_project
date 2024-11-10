package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.model.ReplyVO;

import java.util.List;

public interface ReplyDAO_interface {
	
	 public void insert(ReplyVO replyVO);
	 public Integer getAllReplyAmount(Integer post_id);
	 public List<ReplyVO> getOnePostReply(Integer post_id);
	 public ReplyVO getOneReplyUpdate(Integer reply_id);
	 public void delete(Integer reply_id);
	 public void update(ReplyVO replyVO);


	public ReplyVO findByPrimaryKey(Integer post_id);

	public List<ReplyVO> getReplyByKeyWord(String keyword);

	// 隱藏留言
	public void updateHidden(Integer reply_id);

     //萬用複合查詢(傳入參數型態Map)(回傳 List)
//   public List<EmpVO> getAll(Map<String, String[]> map); 

}
