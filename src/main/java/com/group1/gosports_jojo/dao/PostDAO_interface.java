package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.model.PostVO;

import java.util.List;

public interface PostDAO_interface {
          public void insert(PostVO postVO);
          public void update(PostVO postVO);
          public void delete(Integer post_id);
          public PostVO findByPrimaryKey(Integer post_id);
          public List<PostVO> getAll();
          public List<PostVO> getAll2();
          public List<PostVO> SEARCH_POST  (String keyword, String keyword2);
          public List<PostVO> SEARCH_POST_BY_POP(String keyword, String keyword2);
          //萬用複合查詢(傳入參數型態Map)(回傳 List)
//        public List<EmpVO> getAll(Map<String, String[]> map); 
}
