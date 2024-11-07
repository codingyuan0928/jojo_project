package com.group1.gosports_jojo.dao;


import com.group1.gosports_jojo.model.ResponseVO;

public interface ResponseDAO_interface {
	
	public void insert(ResponseVO responseVO);
	public Integer getAllResponseAmount(Integer post_id);
	public void updateResponse(Integer response_id);
	public ResponseVO getOneResponse(Integer response_id);
	public ResponseVO getPostAllResponse(Integer post_id, Integer user_id);
 
    //萬用複合查詢(傳入參數型態Map)(回傳 List)
//  public List<EmpVO> getAll(Map<String, String[]> map); 
}