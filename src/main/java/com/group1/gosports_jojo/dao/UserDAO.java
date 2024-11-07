package com.group1.gosports_jojo.dao;

import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.model.UserVO;

import java.util.List;

public interface UserDAO {
    void insert(UserVO userVO);

    boolean update(UserVO userVO);

    void delete(Integer userId);

    UserVO findById(Integer userId);

    List<UserVO> getAll();

    UserVO findByEmail(String email);


}
