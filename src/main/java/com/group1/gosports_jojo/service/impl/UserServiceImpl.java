package com.group1.gosports_jojo.service.impl;


import com.group1.gosports_jojo.dao.UserDAO;
import com.group1.gosports_jojo.dto.UserListDTO;
import com.group1.gosports_jojo.dto.UserProfileUpdateRequest;
import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.security.PasswordUtil;
import com.group1.gosports_jojo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDAO dao;
    private final PasswordUtil passwordUtil;

    @Autowired
    public UserServiceImpl(UserDAO dao, PasswordUtil passwordUtil) {
        this.dao = dao;
        this.passwordUtil = passwordUtil;
    }


    public UserVO addUser(String username, String password, byte[] avatar, String email, Integer enabled, String providerName, String accessToken, String refreshToken, Timestamp accessTokenExpiry, Timestamp refreshTokenExpiry, Integer newsletterSubscriptionConsentField, Integer groupPoints, Integer interestsTag) {

        UserVO userVO = new UserVO();
        userVO.setUsername(username);
        userVO.setPassword(password);
        userVO.setAvatar(avatar);
        userVO.setEmail(email);
        userVO.setEnabled(enabled);
        userVO.setProviderName(providerName);
        userVO.setAccessToken(accessToken);
        userVO.setRefreshToken(refreshToken);
        userVO.setAccessTokenExpiry(accessTokenExpiry);
        userVO.setRefreshTokenExpiry(refreshTokenExpiry);
        userVO.setNewsletterSubscriptionConsentField(newsletterSubscriptionConsentField);
        userVO.setGroupPoints(groupPoints);
        userVO.setInterestsTag(interestsTag);
        dao.insert(userVO);
        return userVO;
    }

    public UserVO updateUser(Integer userId, String username, String password, byte[] avatar, String email, Integer enabled, String providerName, String accessToken, String refreshToken, Timestamp accessTokenExpiry, Timestamp refreshTokenExpiry, Integer newsletterSubscriptionConsentField, Integer groupPoints, Integer interestsTag) {
        UserVO userVO = new UserVO();

        userVO.setUserId(userId);
        userVO.setUsername(username);
        userVO.setPassword(password);
        userVO.setAvatar(avatar);
        userVO.setEmail(email);
        userVO.setEnabled(enabled);
        userVO.setProviderName(providerName);
        userVO.setAccessToken(accessToken);
        userVO.setRefreshToken(refreshToken);
        userVO.setAccessTokenExpiry(accessTokenExpiry);
        userVO.setRefreshTokenExpiry(refreshTokenExpiry);
        userVO.setNewsletterSubscriptionConsentField(newsletterSubscriptionConsentField);
        userVO.setGroupPoints(groupPoints);
        userVO.setInterestsTag(interestsTag);
        dao.update(userVO);
        return userVO;
    }

    public void deleteUser(Integer userId) {
        dao.delete(userId);
    }

    public UserVO getOneUser(Integer userId) {
        return dao.findById(userId);
    }

    public List<UserVO> getAll() {
        return dao.getAll();
    }

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //使用bcrypt生成雜湊值
        String hashedPassword = passwordUtil.encode(userRegisterRequest.getPassword());
        // 將 DTO (UserRegisterRequest) 的資料賦值給 VO (UserVO)
        UserVO userVO = new UserVO();
        userVO.setUsername(userRegisterRequest.getUsername());
        userVO.setPassword(hashedPassword);
        MultipartFile avatar = userRegisterRequest.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            try {
                userVO.setAvatar(avatar.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        userVO.setEmail(userRegisterRequest.getEmail());
        userVO.setEnabled(1);
        userVO.setProviderName(userRegisterRequest.getProviderName());
        userVO.setAccessToken(userRegisterRequest.getAccessToken());
        userVO.setRefreshToken(userRegisterRequest.getRefreshToken());
        userVO.setAccessTokenExpiry(userRegisterRequest.getAccessTokenExpiry());
        userVO.setNewsletterSubscriptionConsentField(userRegisterRequest.getNewsletterSubscriptionConsentField());
        userVO.setGroupPoints(0);
        userVO.setInterestsTag(userRegisterRequest.getInterestsTag());
        // 設定自動生成的時間戳欄位
        Timestamp now = new Timestamp(System.currentTimeMillis());
        userVO.setCreatedAt(now);
        userVO.setUpdatedAt(now);
        dao.insert(userVO);
        return userVO.getUserId();
    }

    @Override
    public UserVO findByEmail(String email) {
        return dao.findByEmail(email);
    }
    //會員頁面更新資料
    @Override
    public void updateUserProfile(Integer userId, UserProfileUpdateRequest userProfileUpdateRequest) {
        UserVO userVO = dao.findById(userId);

        if (userVO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userVO.setUsername(userProfileUpdateRequest.getUsername());
        userVO.setAvatar(userProfileUpdateRequest.getAvatar());
        userVO.setInterestsTag(userProfileUpdateRequest.getInterestsTag());

        dao.update(userVO);
    }


    @Override
    public List<UserListDTO> getAllUserDTOs() {
        List<UserVO> userVOs = dao.getAll();
        List<UserListDTO> userDTOs = new ArrayList<>();

        for (UserVO userVO : userVOs) {
            UserListDTO dto = convertToDTO(userVO);
            userDTOs.add(dto);
        }

        return userDTOs;
    }

    private UserListDTO convertToDTO(UserVO userVO) {
        UserListDTO dto = new UserListDTO();
        dto.setUserId(userVO.getUserId());
        dto.setUsername(userVO.getUsername());
        dto.setEmail(userVO.getEmail());
        dto.setEnabled(userVO.getEnabled());
        dto.setProviderName(userVO.getProviderName());
        dto.setNewsletterSubscriptionConsentField(userVO.getNewsletterSubscriptionConsentField());
        dto.setCreatedAt(userVO.getCreatedAt());
        dto.setUpdatedAt(userVO.getUpdatedAt());
        dto.setGroupPoints(userVO.getGroupPoints());
        dto.setInterestsTag(userVO.getInterestsTag());

        // 將 byte[] 圖像轉換為 Base64 字串
        if (userVO.getAvatar() != null) {
            String base64Avatar = Base64.getEncoder().encodeToString(userVO.getAvatar());
            dto.setAvatarBase64("data:image/png;base64," + base64Avatar);
        }

        return dto;
    }
}


