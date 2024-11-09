package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.model.ProVO;

import java.util.*;

public interface ProDAO_interface {
          public void insert(ProVO proVO);
          public void update(ProVO proVO);
          public void delete(Integer productId);
          public ProVO getProductById(Integer productId);
//          public List<ProVO> getOneStmt(Integer productId);
          public List<ProVO> getAll();
          public List<ProVO> getPopular();
          public List<ProVO> getPrice_LtoH();
          public List<ProVO> getNew();
          public List<ProVO> getSearchnam(String productName);
          public List<ProVO> getOverviewPicture(Integer productId);
          public List<ProVO> getAd();

        //查證檢舉商品
        public List<ProVO> getProductByKeyWord(String keyword1, String keyword2);

        //查詢近30分鐘成立訂單(buyer&seller)
        public List<ProVO> getOrderCreatedList();

        //查詢近30分鐘完成訂單(buyer)，order_status為1
        public List<ProVO> getOrderCompletedList();

        //隱藏商品(下架檢舉商品)
        public void updateHidden(Integer product_id);

        //新增推薦商品
        public void changeAdOn(Integer productId);

        //移除推薦商品
        public void changeAdOff(Integer productId);



}