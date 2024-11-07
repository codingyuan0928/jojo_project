package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.model.PicVO;

import java.util.List;

public interface PicDAO_interface {
          public void insert(PicVO picVO);
          public void update(PicVO picVO);
          public void delete(Integer pic_id);
          public PicVO findByPrimaryKey(Integer pic_id);
          public List<PicVO> getAll();
          //萬用複合查詢(傳入參數型態Map)(回傳 List)
//        public List<EmpVO> getAll(Map<String, String[]> map); 
}
