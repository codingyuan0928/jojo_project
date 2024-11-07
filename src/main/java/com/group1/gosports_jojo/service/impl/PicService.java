package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.dao.PicDAO_interface;
import com.group1.gosports_jojo.dao.impl.PicDAO;
import com.group1.gosports_jojo.model.PicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service
public class PicService {

    private PicDAO_interface dao;

    @Autowired
    public PicService(PicDAO_interface dao) {
        this.dao = dao;
    }

    // 新增圖片
    public PicVO addPic(Integer post_id, String post_pic) {

        PicVO picVO = new PicVO();
        picVO.setPost_id(post_id);
        picVO.setPost_pic(post_pic);
        picVO.setCreated_datetime(new Timestamp(System.currentTimeMillis()));

        dao.insert(picVO);

        return picVO;
    }

    // 更新圖片
    public PicVO updatePic(Integer pic_id, Integer post_id, String post_pic) {

        PicVO picVO = new PicVO();
        picVO.setPic_id(pic_id);
        picVO.setPost_id(post_id);
        picVO.setPost_pic(post_pic);
        picVO.setUpdated_datetime(new Timestamp(System.currentTimeMillis()));

        dao.update(picVO);

        return picVO;
    }

    // 刪除圖片
    public void deletePic(Integer pic_id) {
        dao.delete(pic_id);
    }

    // 查詢單張圖片
    public PicVO getOnePic(Integer pic_id) {
        return dao.findByPrimaryKey(pic_id);
    }

    // 查詢所有圖片
    public List<PicVO> getAll() {
        return dao.getAll();
    }
}
