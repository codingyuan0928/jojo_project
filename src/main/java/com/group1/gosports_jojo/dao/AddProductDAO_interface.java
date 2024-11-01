package com.group1.gosports_jojo.dao;

import java.io.InputStream;

import com.group1.gosports_jojo.model.AddProductVO;

public interface AddProductDAO_interface {
	      public String insert(AddProductVO productDAO, int action); //新增
	      public void insert2(InputStream pic, int product_id); //新增
//	      public void insert(int picture_id, AddProductVO productDAO);
         // public void update(AddProductVO productDAO); //更新
          //public void delete(Integer producpId);  //刪除
          //public AddProductVO findByPrimaryKey(Integer producpId); //查詢單筆
	      //public List<AddProductVO> getAll();  //取得全部
	      //public Set<AddProductVO> getEmpsByDeptno(Integer producpId);  // 從某個資料查詢某個資料
}
