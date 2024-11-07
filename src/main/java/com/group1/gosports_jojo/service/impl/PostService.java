package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.dao.PostDAO_interface;
import com.group1.gosports_jojo.model.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
@Service
public class PostService {

	private PostDAO_interface dao;

	@Autowired
	public PostService(PostDAO_interface dao) {
		this.dao = dao;
	}

	// 新增
	public PostVO addPost(Integer user_id, String post_category, String post_title, String post_content) {

		PostVO postVO = new PostVO();
		postVO.setUser_id(Integer.valueOf(user_id));
		postVO.setPost_category(post_category);
		postVO.setPost_title(post_title);
		postVO.setPost_content(post_content);
		postVO.setCreated_datetime(new Timestamp(System.currentTimeMillis()));

		dao.insert(postVO);

		return postVO;
	}

	// 查詢所有文章
	public List<PostVO> getAll() {
		return dao.getAll();
	}
	
	public List<PostVO> getAll2() {
		return dao.getAll2();
	}

	// 查詢單篇文章
	public PostVO getOnePost(Integer post_id) {
		return dao.findByPrimaryKey(post_id);
	}
	
	// 隱藏文章
		public void deletePost(Integer post_id) {
			dao.delete(post_id);

		}

	// 更新文章
	public PostVO updatePost2(String post_title, String post_category, String post_content, Integer post_id) {

		PostVO postVO = new PostVO();
		postVO.setPost_title(post_title);
		postVO.setPost_category(post_category);
		postVO.setPost_content(post_content);
		postVO.setPost_id(post_id);

		dao.update(postVO);

		return postVO;
	}

	public List<PostVO> SEARCH_POST  (String keyword, String keyword2){

		return dao.SEARCH_POST(keyword, keyword2);
	}

	public List<PostVO> SEARCH_POST_BY_POP  (String keyword, String keyword2){

		return dao.SEARCH_POST_BY_POP(keyword, keyword2);
	}



}
