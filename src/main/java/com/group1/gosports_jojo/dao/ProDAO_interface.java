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
         
}