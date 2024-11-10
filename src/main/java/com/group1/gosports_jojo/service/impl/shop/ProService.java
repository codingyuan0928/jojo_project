package com.group1.gosports_jojo.service.impl.shop;

import com.group1.gosports_jojo.dao.ProDAO_interface;
import com.group1.gosports_jojo.dao.impl.ProDAO;
import com.group1.gosports_jojo.model.ProVO;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ProService {

    private ProDAO_interface dao;

    public ProService(ProDAO_interface dao) {
        this.dao = dao;
    }

    public ProVO addPro(Integer productId, Integer vendorId, String productName, String productContent,
                        Integer price, String productSpec, Integer stock, Integer productStatus, Timestamp createdDatetime,
                        Timestamp removedDatetime, Timestamp productUpdatedDatetime) {

        ProVO proVO = new ProVO();

        proVO.setProductId(productId);
        proVO.setVendorId(vendorId);
        proVO.setProductName(productName);
        proVO.setProductContent(productContent);
        proVO.setPrice(price);
        proVO.setProductSpec(productSpec);
        proVO.setStock(stock);
        proVO.setCreated_datetime(createdDatetime);
        proVO.setRemoved_datetime(removedDatetime);
        proVO.setProductStatus(productStatus);
        proVO.setProduct_updated_datetime(productUpdatedDatetime);
        dao.insert(proVO);

        return proVO;
    }

    public ProVO updatePro(Integer productId,Integer vendorId,String productName,String productContent,
                           Integer price,String productSpec,Integer stock,Integer productStatus,Timestamp createdDatetime,
                           Timestamp removedDatetime,Timestamp productUpdatedDatetime,byte[] picture) {

        ProVO proVO = new ProVO();

        proVO.setProductId(productId);
        proVO.setVendorId(vendorId);
        proVO.setProductName(productName);
        proVO.setProductContent(productContent);
        proVO.setPrice(price);
        proVO.setProductSpec(productSpec);
        proVO.setStock(stock);
        proVO.setCreated_datetime(createdDatetime);
        proVO.setRemoved_datetime(removedDatetime);
        proVO.setProductStatus(productStatus);
        proVO.setProduct_updated_datetime(productUpdatedDatetime);
        proVO.setPicture(picture);

        dao.insert(proVO);

        return proVO;

    }

    public void deletePro(Integer productId) {
        dao.delete(productId);
    }

    public ProVO getProductById(Integer productId) {
        return dao.getProductById(productId);
    }

    public List<ProVO> getAll(){
        return dao.getAll();
    }
    public List<ProVO> getPopular(){
        return dao.getPopular();
    }
    public List<ProVO> getPrice_LtoH(){
        return dao.getPrice_LtoH();
    }
    public List<ProVO> getNew(){
        return dao.getNew();
    }

    public List<ProVO> getSearchnam(String productName){
        return dao.getSearchnam(productName);
    }


    public List<ProVO> getOverviewPicture(Integer productId){
        return dao.getOverviewPicture(productId);
    }

    public List<ProVO> getAd(){
        return dao.getAd();
    }

    //查證檢舉商品
    public List<ProVO> getProductByKeyWord(String keyword1, String keyword2){
        return dao.getProductByKeyWord(keyword1, keyword2);
    }

    //查詢近30分鐘成立訂單(buyer&seller)
    public List<ProVO> getOrderCreatedList(){
        return dao.getOrderCreatedList();
    }

    //查詢近30分鐘完成訂單(buyer)，order_status為1
    public List<ProVO> getOrderCompletedList(){
        return dao.getOrderCompletedList();
    }

    //隱藏商品(下架檢舉商品)
    public void updateHidden(Integer product_id) {
        dao.updateHidden(product_id);
    }

    //新增推薦商品
    public void changeAdOn(Integer productId) {
        dao.changeAdOn(productId);
    }

    //移除推薦商品
    public void changeAdOff(Integer productId) {
        dao.changeAdOff(productId);
    }




}
