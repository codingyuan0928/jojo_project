package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.dao.ReplyDAO_interface;
import com.group1.gosports_jojo.model.ReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service
public class ReplyService {

    private ReplyDAO_interface dao;

    @Autowired
    public ReplyService(ReplyDAO_interface dao) {
        this.dao = dao;
    }
    
    // 新增留言
    public ReplyVO addReply(Integer user_id, Integer post_id, String reply_content) {

        ReplyVO replyVO = new ReplyVO();
        replyVO.setUser_id(user_id);
        replyVO.setPost_id(post_id);
        replyVO.setReply_content(reply_content);
        replyVO.setCreated_datetime(new Timestamp(System.currentTimeMillis()));

        dao.insert(replyVO);

        return replyVO;
    }

    // 更新留言
    public ReplyVO updateReply(Integer reply_id, Integer user_id, Integer post_id, Integer reply_status, String reply_content) {

        ReplyVO replyVO = new ReplyVO();
        replyVO.setReply_id(reply_id);
        replyVO.setUser_id(user_id);
        replyVO.setPost_id(post_id);
        replyVO.setReply_status(reply_status);
        replyVO.setReply_content(reply_content);
        replyVO.setUpdated_datetime(new Timestamp(System.currentTimeMillis()));

        dao.update(replyVO);

        return replyVO;
    }

    // 刪除留言
    public void deleteReply(Integer reply_id) {
        dao.delete(reply_id);
    }

    // 顯示單一留言
    public ReplyVO getOneReplyUpdate(Integer reply_id) {
        return dao.getOneReplyUpdate(reply_id);
    }

    // 顯示所有留言
    public List<ReplyVO> getOnePostReply(Integer post_id) {
        return dao.getOnePostReply(post_id);
    }

    public Integer getAllReplyAmount(Integer post_id){
        return dao.getAllReplyAmount(post_id);
    }


    public ReplyVO getOne(Integer reply_id) {
        return dao.findByPrimaryKey(reply_id);
    }

    public List<ReplyVO> getReplyByKeyWord(String keyword) {
        return dao.getReplyByKeyWord(keyword);
    }

    public void updateHidden(Integer reply_id) {
        dao.updateHidden(reply_id);
    }
}
