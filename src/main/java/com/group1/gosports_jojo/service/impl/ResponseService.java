package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.dao.ResponseDAO_interface;
import com.group1.gosports_jojo.model.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    private ResponseDAO_interface dao;

    @Autowired
    public ResponseService(ResponseDAO_interface dao) {
        this.dao = dao;
    }

 // 新增讚
    public ResponseVO insert(Integer post_id, Integer user_id) {
    	
    	ResponseVO responseVO = new ResponseVO();
    	responseVO.setPost_id(post_id);
    	responseVO.setUser_id(user_id);
    
        dao.insert(responseVO);

        return responseVO;
    }
    
 // 取消讚
    public void updateResponse(Integer response_id ) {

    	ResponseVO responseVO = new ResponseVO();
    	responseVO.setResponse_id(response_id);

        dao.updateResponse(response_id);
//        return responseVO;
    }
    

    // 顯示單一讚
    public ResponseVO getOneResponse(Integer reply_id) {
        return dao.getOneResponse(reply_id);
    }
    
    // 顯示全部讚數量
    public Integer getAllResponseAmount(Integer post_id) {
		return dao.getAllResponseAmount(post_id);
    }
  //顯示單篇文章所有按讚(用戶是否在某篇文章點讚)
    public ResponseVO getPostAllResponse (Integer post_id, Integer user_id) {
    	return dao.getPostAllResponse(post_id, user_id);
    }
    
}
